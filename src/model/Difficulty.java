package model;

/**
 * A Difficulty enum a játék nehézségi szintjeit reprezentálja.
 */
public enum Difficulty {
    HARD(0, "Nehéz"),
    MEDIUM(1, "Közepes"),
    EASY(2, "Könnyű"),
    CUSTOM(3, "Egyéni");

    private final int rank; //A nehézség rendezési prioritása (alacsonyabb = nehezebb).
    private final String displayName; //A nehézség neve

    /**
     * konstruktor, amely beállítja a nehézség rendezési rangját és megjelenítendő nevét.
     *
     * @param rank: a nehézség rendezési prioritása
     * @param displayName: a felhasználói felületen megjelenített név
     */
    Difficulty(int rank, String displayName) {
        this.rank = rank;
        this.displayName = displayName;
    }

    /**
     * Visszaadja a nehézségi szint rangját.
     *
     * @return: a nehézség rangja
     */
    public int getRank() {
        return rank;
    }
    
    /**
     * @return: a nehézségi szint neve
     */
    public String getDisplayName() {
        return displayName;
    }
}
