package view;

import model.Highscore;

import javax.swing.*;
import java.awt.*;

/**
 * A HighscoreDialog a játék eredménytábláját jeleníti meg.
 */
public class HighscoreDialog extends JDialog {
    private final Highscore highscore;
    private JTable table;

    /**
     * Létrehoz egy új highscore ablakot.
     *
     * @param owner: a párbeszédablak tulajdonosa (általában a főablak)
     * @param highscore: a megjelenítendő highscore adatok
     */
    public HighscoreDialog(Frame owner, Highscore highscore) {
        super(owner, "Ranglista", true);
        this.highscore = highscore;

        setLayout(new BorderLayout());

        table = new JTable(new HighscoreTableModel(highscore.getEntries()));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton closeBtn = new JButton("OK");
        closeBtn.addActionListener(e -> setVisible(false));
        add(closeBtn, BorderLayout.SOUTH);

        setSize(380, 300);
        setLocationRelativeTo(owner);
    }

    /**
     * Frissíti a táblázat tartalmát a jelenlegi highscore adatok alapján.
     */
    public void refresh() {
        table.setModel(new HighscoreTableModel(highscore.getEntries()));
    }

    /**
     * Megjeleníti a párbeszédablakot.
     */
    public void showDialog() {
        refresh();
        setVisible(true);
    }
}
