package view;

import model.ScoreEntry;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * A HighscoreTableModel feladata a ranglista megjelenítése.
 */
public class HighscoreTableModel extends AbstractTableModel {
    private final List<ScoreEntry> entries;

    /**
     * Konstruktor, ami létrehoz egy új táblamodellt
     *
     * @param entries: a táblázatban megjelenítendő eredmények
     */
    public HighscoreTableModel(List<ScoreEntry> entries) {
        this.entries = entries;
    }

    /**
     * @return: a táblázat sorainak száma
     */
    @Override
    public int getRowCount() {
        return entries.size();
    }

    /**
     * @return: a táblázat oszlopainak száma
     */
    @Override
    public int getColumnCount() {
        return 3;
    }

    /**
     * Visszaadja az adott cellába tartozó értéket.
     *
     * @param row: a sorindex
     * @param col: az oszlopindex
     * @return: a cella megjelenítendő értéke
     */
    @Override
    public Object getValueAt(int row, int col) {
        ScoreEntry e = entries.get(row);

        switch (col) {
            case 0:
                return e.getName();
            case 1:
                return e.getTime() + " s";
            case 2:
                return e.getDifficulty().getDisplayName();
            default:
                return "";
        }
    }

    /**
     * Visszaadja az oszlop nevét.
     *
     * @param col: az oszlopindex
     * @return: az oszlop neve
     */
    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Név";
            case 1:
                return "Idő";
            case 2:
                return "Nehézség";
            default:
                return "";
        }
    }
}
