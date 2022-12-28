package mediathek.gui.dialogEinstellungen;

import mediathek.config.Daten;
import mediathek.daten.GeoblockingField;
import mediathek.file.GetFile;
import mediathek.gui.dialog.DialogHilfe;
import mediathek.gui.messages.BlacklistChangedEvent;
import mediathek.gui.messages.GeoStateChangedEvent;
import mediathek.tool.ApplicationConfiguration;
import mediathek.tool.MessageBus;
import mediathek.tool.SVGIconUtilities;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class PanelEinstellungenGeo extends JPanel {
    private final JFrame parentComponent;

    public PanelEinstellungenGeo(JFrame pparentComponent) {
        parentComponent = pparentComponent;

        initComponents();
        init();
    }

    private void init() {
        switch (ApplicationConfiguration.getInstance().getGeographicLocation()) {
            case GeoblockingField.GEO_CH -> jRadioButtonCH.setSelected(true);
            case GeoblockingField.GEO_AT -> jRadioButtonAt.setSelected(true);
            case GeoblockingField.GEO_EU -> jRadioButtonEu.setSelected(true);
            case GeoblockingField.GEO_WELT -> jRadioButtonSonst.setSelected(true);
            default -> jRadioButtonDe.setSelected(true);
        }
        jRadioButtonDe.addActionListener(e -> {
            ApplicationConfiguration.getInstance().setGeographicLocation(GeoblockingField.GEO_DE);
            filterBlacklistAndNotifyChanges();
        });
        jRadioButtonCH.addActionListener(e -> {
            ApplicationConfiguration.getInstance().setGeographicLocation(GeoblockingField.GEO_CH);
            filterBlacklistAndNotifyChanges();
        });
        jRadioButtonAt.addActionListener(e -> {
            ApplicationConfiguration.getInstance().setGeographicLocation(GeoblockingField.GEO_AT);
            filterBlacklistAndNotifyChanges();
        });
        jRadioButtonEu.addActionListener(e -> {
            ApplicationConfiguration.getInstance().setGeographicLocation(GeoblockingField.GEO_EU);
            filterBlacklistAndNotifyChanges();
        });
        jRadioButtonSonst.addActionListener(e -> {
            ApplicationConfiguration.getInstance().setGeographicLocation(GeoblockingField.GEO_WELT);
            filterBlacklistAndNotifyChanges();
        });

        jButtonHilfe.setIcon(SVGIconUtilities.createSVGIcon("icons/fontawesome/circle-question.svg"));
        jButtonHilfe.addActionListener(e -> new DialogHilfe(parentComponent, true, new GetFile().getHilfeSuchen(GetFile.PFAD_HILFETEXT_GEO)).setVisible(true));
    }

    /**
     * Filter blacklist and notify other components that changes were made.
     */
    private void filterBlacklistAndNotifyChanges() {
        final var daten = Daten.getInstance();

        daten.getListeBlacklist().filterListe();
        MessageBus.getMessageBus().publishAsync(new GeoStateChangedEvent());
        MessageBus.getMessageBus().publishAsync(new BlacklistChangedEvent());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    private void initComponents() {
        var panel1 = new JPanel();
        jRadioButtonDe = new JRadioButton();
        jRadioButtonCH = new JRadioButton();
        jRadioButtonAt = new JRadioButton();
        jRadioButtonEu = new JRadioButton();
        jRadioButtonSonst = new JRadioButton();
        jButtonHilfe = new JButton();

        //======== this ========
        setLayout(new MigLayout(
            new LC().insets("5").hideMode(3).gridGap("5", "5"), //NON-NLS
            // columns
            new AC()
                .grow().fill(),
            // rows
            new AC()
                .gap()
                .fill()));

        //======== panel1 ========
        {
            panel1.setBorder(new TitledBorder("Mein Standort")); //NON-NLS
            panel1.setLayout(new VerticalLayout());

            //---- jRadioButtonDe ----
            jRadioButtonDe.setSelected(true);
            jRadioButtonDe.setText("Deutschland (DE)"); //NON-NLS
            panel1.add(jRadioButtonDe);

            //---- jRadioButtonCH ----
            jRadioButtonCH.setText("Schweiz (CH)"); //NON-NLS
            panel1.add(jRadioButtonCH);

            //---- jRadioButtonAt ----
            jRadioButtonAt.setText("\u00d6sterreich (AT)"); //NON-NLS
            panel1.add(jRadioButtonAt);

            //---- jRadioButtonEu ----
            jRadioButtonEu.setText("EU (EBU - European Broadcasting Union)"); //NON-NLS
            panel1.add(jRadioButtonEu);

            //---- jRadioButtonSonst ----
            jRadioButtonSonst.setText("Sonstiger"); //NON-NLS
            panel1.add(jRadioButtonSonst);
        }
        add(panel1, new CC().cell(0, 0));

        //---- jButtonHilfe ----
        jButtonHilfe.setIcon(null);
        jButtonHilfe.setToolTipText("Hilfe anzeigen"); //NON-NLS
        add(jButtonHilfe, new CC().cell(0, 1).alignX("right").growX(0)); //NON-NLS

        //---- buttonGroup1 ----
        var buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(jRadioButtonDe);
        buttonGroup1.add(jRadioButtonCH);
        buttonGroup1.add(jRadioButtonAt);
        buttonGroup1.add(jRadioButtonEu);
        buttonGroup1.add(jRadioButtonSonst);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JRadioButton jRadioButtonDe;
    private JRadioButton jRadioButtonCH;
    private JRadioButton jRadioButtonAt;
    private JRadioButton jRadioButtonEu;
    private JRadioButton jRadioButtonSonst;
    private JButton jButtonHilfe;
    // End of variables declaration//GEN-END:variables

}
