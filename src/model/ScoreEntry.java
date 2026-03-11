package model;

import java.io.Serializable;

/**
 * A ScoreEntry osztály egyetlen bejegyzést reprezentál a highscore táblában.
 */
public class ScoreEntry implements Serializable {
    private final String name;
    private final int time;
    private final Difficulty difficulty;

    /**
     * Konstruktor, ami létrehoz egy új highscore bejegyzést.
     *
     * @param name: a játékos neve
     * @param time: a teljesítési idő
     * @param difficulty: a nehézségi szint
     */
    public ScoreEntry(String name, int time, Difficulty difficulty) {
        this.name = name;
        this.time = time;
        this.difficulty = difficulty;
    }

    /**
     * @return: a játékos neve
     */
    public String getName() {
        return name;
    }

    /**
     * @return: a játék befejezéséhez szükséges idő.
     */
    public int getTime() {
        return time;
    }

    /**
     * @return: a nehézségi szint
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }
}
