package mediathek.gui.tabs.tab_film;

import com.formdev.flatlaf.util.ScaledImageIcon;
import mediathek.daten.DatenFilm;
import mediathek.gui.dialog.DialogFilmBeschreibung;
import mediathek.gui.tabs.AGuiTabPanel;
import mediathek.mainwindow.MediathekGui;
import mediathek.tool.GuiFunktionen;
import mediathek.tool.sender_icon_cache.MVSenderIconCache;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXHyperlink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;

public class FilmDescriptionPanel extends JPanel {
    private final AGuiTabPanel currentTab;
    private final JScrollPane scrollPane1 = new JScrollPane();
    private final JPopupMenu popupMenu = new JPopupMenu();
    private final SenderIconLabel lblIcon = new SenderIconLabel();
    private final JLabel lblThema = new JLabel();
    private final JLabel lblTitel = new JLabel();
    private final JTextArea textArea = new JTextArea();
    private final JXHyperlink hyperlink = new JXHyperlink();
    private DatenFilm currentFilm;

    public FilmDescriptionPanel(@NotNull AGuiTabPanel currentTab) {
        this.currentTab = currentTab;

        initComponents();

        createPopupMenu();

        setAllFieldsEmpty();
    }

    private void createPopupMenu() {
        var item = new JMenuItem("Beschreibung ändern...");
        item.addActionListener(l -> {
            DialogFilmBeschreibung dialog = new DialogFilmBeschreibung(MediathekGui.ui(), currentFilm);
            dialog.setVisible(true);
        });
        popupMenu.add(item);
        popupMenu.addSeparator();

        item = new JMenuItem("Beschreibung in Zwischenablage kopieren");
        item.addActionListener(l -> GuiFunktionen.copyToClipboard(currentFilm.getDescription()));
        popupMenu.add(item);

        item = new JMenuItem("Filmbasisinformationen in Zwischenablage kopieren");
        item.addActionListener(l -> {
            String sb = currentFilm.getSender() +
                    " - " +
                    currentFilm.getThema() +
                    " - " +
                    currentFilm.getTitle();

            GuiFunktionen.copyToClipboard(sb);
        });
        popupMenu.add(item);

        setComponentPopupMenu(popupMenu);
        textArea.setComponentPopupMenu(popupMenu);
    }

    private void initComponents() {
        setLayout(new MigLayout(
                new LC().hideMode(3),
                new AC()
                        .fill().gap()
                        .grow().fill(),
                new AC()
                        .gap()
                        .gap()
                        .gap()
        ));

        lblIcon.setPreferredSize(new Dimension(96, 96));
        lblIcon.setVerticalAlignment(SwingConstants.TOP);
        add(lblIcon, new CC().cell(0, 0, 1, 3).alignX("center").alignY("top").grow(0, 0)); //NON-NLS

        lblThema.setFont(lblThema.getFont().deriveFont(lblThema.getFont().getStyle() | Font.BOLD));
        add(lblThema, new CC().cell(1, 0));

        lblTitel.setFont(lblTitel.getFont().deriveFont(lblTitel.getFont().getStyle() | Font.BOLD));
        add(lblTitel, new CC().cell(1, 1));

        scrollPane1.setPreferredSize(new Dimension(299, 75));
        scrollPane1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        scrollPane1.setMinimumSize(new Dimension(23, 75));
        scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        scrollPane1.setViewportView(textArea);
        add(scrollPane1, new CC().cell(1, 2).grow());

        //add(hyperlink, new CC().cell(0, 3, 2, 1));
        add(hyperlink, new CC().cell(1, 3));
    }

    public void install(@NotNull JTabbedPane tabbedPane, @NotNull JTable tabelle) {
        tabbedPane.add("Beschreibung", this);

        tabelle.getSelectionModel().addListSelectionListener(e ->
                currentTab.getCurrentlySelectedFilm().ifPresentOrElse(film -> {
                    showFilmDescription(film);
                    currentFilm = film;
                }, () -> {
                    setAllFieldsEmpty();
                    currentFilm = null;
                }));
    }

    private void setAllFieldsEmpty() {
        hyperlink.setVisible(false);
        hyperlink.setText("");
        hyperlink.setToolTipText("");
        hyperlink.setComponentPopupMenu(null);

        textArea.setText("");
        lblIcon.setIcon(null);
        lblThema.setText("");
        lblTitel.setText("");
    }

    private void showFilmDescription(@NotNull DatenFilm film) {
        lblThema.setText(film.getThema());
        lblTitel.setText(film.getTitle());

        hyperlink.setVisible(true);
        try {
            hyperlink.setURI(new URI(film.getWebsiteUrl()));
            hyperlink.setText("Link zur Webseite");
            hyperlink.setClicked(false);
            JPopupMenu popup = new JPopupMenu();
            popup.add(new CopyToCLipboardAction(film.getWebsiteUrl()));
            hyperlink.setComponentPopupMenu(popup);
        } catch (Exception e) {
            //logger
            hyperlink.setText("Link nicht verfügbar");
        }
        hyperlink.setToolTipText(film.getWebsiteUrl());

        textArea.setText(film.getDescription());
        SwingUtilities.invokeLater(() -> scrollPane1.getVerticalScrollBar().setValue(0));
        lblIcon.setSender(film.getSender());
    }

    static class CopyToCLipboardAction extends AbstractAction {
        private final String webSiteUrl;

        public CopyToCLipboardAction(@NotNull String url) {
            webSiteUrl = url;
            putValue(Action.NAME, "In Zwischenablage kopieren");
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                GuiFunktionen.copyToClipboard(webSiteUrl);
            }
            catch (Exception ignored) {
            }
        }
    }

    static class SenderIconLabel extends JLabel {
        private static final Dimension ICON_DIMENSION = new Dimension(96, 96);

        public SenderIconLabel() {
            setText("");
            setIcon(null);
        }

        private void sizeToIcon(@NotNull Icon icon) {
            int height = icon.getIconHeight();
            int width = icon.getIconWidth();

            Dimension d = new Dimension(width, height);
            setPreferredSize(d);
        }

        public void setSender(@Nullable String sender) {
            if (sender == null) {
                setIcon(null);
            } else {
                MVSenderIconCache.get(sender).ifPresentOrElse(icon -> {
                    var imageDim = new Dimension(icon.getIconWidth(), icon.getIconHeight());
                    var destDim = GuiFunktionen.calculateFittedDimension(imageDim, ICON_DIMENSION);
                    var origIcon = new ScaledImageIcon(icon, destDim.width, destDim.height);
                    setIcon(origIcon);
                    sizeToIcon(origIcon);
                }, () -> setIcon(null));
            }
        }
    }
}
