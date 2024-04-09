package mediathek.gui.tasks;

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import com.google.common.base.Stopwatch;
import mediathek.config.Daten;
import mediathek.daten.DatenFilm;
import mediathek.daten.IndexedFilmList;
import mediathek.mainwindow.MediathekGui;
import mediathek.tool.ApplicationConfiguration;
import mediathek.tool.SwingErrorDialog;
import mediathek.tool.datum.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;

public class LuceneIndexWorker extends SwingWorker<Void, Void> {
    private static final Logger logger = LogManager.getLogger();
    private final JProgressBar progressBar;
    private final JLabel progLabel;
    private int oldProgress = 0;
    LanguageDetector languageDetector;


    public LuceneIndexWorker(@NotNull JLabel progLabel, @NotNull JProgressBar progressBar) {
        this.progressBar = progressBar;
        this.progLabel = progLabel;

        //FIXME false by default!
        var config = ApplicationConfiguration.getConfiguration();
        if (config.getBoolean(ApplicationConfiguration.LUCENE_DETECT_LANGUAGE, true)) {
            //FIXME disable more unused languages
            languageDetector = LanguageDetectorBuilder
                    .fromAllLanguagesWithout(Language.LATIN,
                            Language.AMHARIC,
                            Language.BENGALI,
                            Language.BOKMAL,
                            Language.CHINESE,
                            Language.ESPERANTO,
                            Language.GANDA,
                            Language.GUJARATI,
                            Language.INDONESIAN,
                            Language.IRISH,
                            Language.JAPANESE,
                            Language.KAZAKH,
                            Language.KOREAN,
                            Language.MALAY,
                            Language.MAORI,
                            Language.MARATHI,
                            Language.MONGOLIAN,
                            Language.OROMO,
                            Language.PUNJABI,
                            Language.SHONA,
                            Language.SINHALA,
                            Language.SOMALI,
                            Language.SOTHO,
                            Language.SWAHILI,
                            Language.TAGALOG,
                            Language.TAMIL,
                            Language.TELUGU,
                            Language.TSONGA,
                            Language.TIGRINYA,
                            Language.TSWANA,
                            Language.URDU,
                            Language.VIETNAMESE,
                            Language.WELSH,
                            Language.XHOSA,
                            Language.ZULU)
                    .withPreloadedLanguageModels()
                    .withLowAccuracyMode().build();
        }

        SwingUtilities.invokeLater(() -> {
            final var ui = MediathekGui.ui();
            ui.toggleBlacklistAction.setEnabled(false);
            ui.editBlacklistAction.setEnabled(false);
            ui.loadFilmListAction.setEnabled(false);

            progLabel.setText("Blacklist anwenden");
            progressBar.setIndeterminate(true);
        });
    }

    private void indexFilm(@NotNull IndexWriter writer, @NotNull DatenFilm film) throws IOException {
        var doc = new Document();
        // store fields for debugging, otherwise they should stay disabled
        doc.add(new StringField(LuceneIndexKeys.ID, Integer.toString(film.getFilmNr()), Field.Store.YES));
        doc.add(new StringField(LuceneIndexKeys.NEW, Boolean.toString(film.isNew()), Field.Store.NO));
        doc.add(new TextField(LuceneIndexKeys.SENDER, film.getSender(), Field.Store.NO));
        doc.add(new TextField(LuceneIndexKeys.TITEL, film.getTitle(), Field.Store.NO));
        doc.add(new TextField(LuceneIndexKeys.THEMA, film.getThema(), Field.Store.NO));
        doc.add(new IntPoint(LuceneIndexKeys.FILM_LENGTH, film.getFilmLength()));
        doc.add(new IntPoint(LuceneIndexKeys.FILM_SIZE, film.getFileSize().toInteger()));

        doc.add(new TextField(LuceneIndexKeys.BESCHREIBUNG, film.getDescription(), Field.Store.NO));
        doc.add(new StringField(LuceneIndexKeys.LIVESTREAM, Boolean.toString(film.isLivestream()), Field.Store.NO));
        doc.add(new StringField(LuceneIndexKeys.HIGH_QUALITY, Boolean.toString(film.isHighQuality()), Field.Store.NO));
        doc.add(new StringField(LuceneIndexKeys.SUBTITLE, Boolean.toString(film.hasSubtitle() || film.hasBurnedInSubtitles()), Field.Store.NO));
        doc.add(new StringField(LuceneIndexKeys.TRAILER_TEASER, Boolean.toString(film.isTrailerTeaser()), Field.Store.NO));
        doc.add(new StringField(LuceneIndexKeys.AUDIOVERSION, Boolean.toString(film.isAudioVersion()), Field.Store.NO));
        doc.add(new StringField(LuceneIndexKeys.SIGN_LANGUAGE, Boolean.toString(film.isSignLanguage()), Field.Store.NO));
        final Language res = getFilmLanguage(film);
        doc.add(new StringField(LuceneIndexKeys.FILM_LANGUAGE, res.getIsoCode639_1().toString(), Field.Store.NO));

        addSendeDatum(doc, film);

        writer.addDocument(doc);
    }

    private @NotNull Language getFilmLanguage(@NotNull DatenFilm film) {
        Language res;
        switch (film.getSender()) {
            case "ARTE.DE", "Funk.net", "KiKA", "Radio Bremen TV", "PHOENIX" -> res = Language.GERMAN;
            case "ARTE.EN" -> res = Language.ENGLISH;
            case "ARTE.ES" -> res = Language.SPANISH;
            case "ARTE.FR" -> res = Language.FRENCH;
            case "ARTE.IT" -> res = Language.ITALIAN;
            case "ARTE.PL" -> res = Language.POLISH;
            default -> res = languageDetector.detectLanguageOf(film.getDescription());// Language.GERMAN;
        }
        return res;
    }

    private void addSendeDatum(@NotNull Document doc, @NotNull DatenFilm film) {
        String sendeDatumStr = DateTools.timeToString(DateUtil.convertFilmDateToLuceneDate(film),
                DateTools.Resolution.DAY);
        doc.add(new StringField(LuceneIndexKeys.SENDE_DATUM, sendeDatumStr, Field.Store.NO));
    }

    @Override
    protected Void doInBackground() {
        var filmListe = (IndexedFilmList) Daten.getInstance().getListeFilmeNachBlackList();
        SwingUtilities.invokeLater(() -> {
            progLabel.setText("Indiziere Filme");
            progressBar.setMinimum(0);
            progressBar.setMaximum(100);
            progressBar.setValue(0);
            progressBar.setIndeterminate(false);
        });

        //index filmlist after blacklist only
        var writer = filmListe.getWriter();
        var totalSize = (float) filmListe.size();

        try {
            int counter = 0;
            Stopwatch watch = Stopwatch.createStarted();
            //for safety delete all entries
            writer.deleteAll();

            for (var film : filmListe) {
                counter++;
                indexFilm(writer, film);

                final var progress = (int) (100.0f * (counter / totalSize));
                if (progress != oldProgress) {
                    oldProgress = progress;
                    SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                }
            }
            writer.commit();
            watch.stop();
            logger.trace("Lucene index creation took {}", watch);

            var reader = filmListe.getReader();
            if (reader != null) {
                reader.close();
            }
            reader = DirectoryReader.open(filmListe.getLuceneDirectory());
            filmListe.setReader(reader);

            filmListe.setIndexSearcher(new IndexSearcher(reader));
        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> {
                SwingErrorDialog.showExceptionMessage(MediathekGui.ui(),
                        "Fehler bei der Erstellung des Filmindex.\nDas Programm wird beendet da es nicht lauffähig ist.", ex);
                MediathekGui.ui().quitApplication();
            });
        }

        return null;
    }

    @Override
    protected void done() {
        final var ui = MediathekGui.ui();
        ui.toggleBlacklistAction.setEnabled(true);
        ui.editBlacklistAction.setEnabled(true);
        ui.loadFilmListAction.setEnabled(true);
    }

}
