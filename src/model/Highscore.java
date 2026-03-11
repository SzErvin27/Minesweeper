package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A Highscore osztály a játék eredménytábláját kezeli.
 */
public class Highscore {
    private final List<ScoreEntry> entries; //az eredmények listája

    /**
     * Konstruktor, ami létrehoz egy üres highscore listát.
     */
    public Highscore() {
        entries = new ArrayList<>();
    }

    /**
     * Hozzáad egy új eredményt a listához
     *
     * @param e: a hozzáadandó eredmény
     */
    public void addScore(ScoreEntry e) {
        entries.add(e);
        sort();
        trimToTen();
    }

    /**
     * A lista rendezése:
     * 1. Nehézség
     * 2. Idő
     */
    private void sort() {
        entries.sort(
                Comparator.comparingInt((ScoreEntry e) -> e.getDifficulty().getRank())
                        .thenComparingInt(ScoreEntry::getTime)
        );
    }

    /**
     * A 10. hely utáni elemeket törli.
     */
    private void trimToTen() {
        if (entries.size() > 10) {
            entries.subList(10, entries.size()).clear();
        }
    }

    /**
     * Másolatot ad vissza az eredmények listájáról.
     *
     * @return: a highscore bejegyzések másolata
     */
    public List<ScoreEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    /**
     * Elmenti az eredményeket szerializálva egy fájlba.
     *
     * @param f: a fájl, ahova menteni kell
     */
    public void saveToFile(File f) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(entries);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Betölti az eredményeket a megadott fájlból.
     *
     * @param f: a betöltendő fájl
     */
    public void loadFromFile(File f) {
        if (!f.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            List<ScoreEntry> loaded = (List<ScoreEntry>) ois.readObject();
            entries.clear();
            entries.addAll(loaded);
            sort();
            trimToTen();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
