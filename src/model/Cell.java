package model;

/**
 * A Cell osztály a játéktábla egyetlen mezőjét reprezentálja.
 */
public class Cell {
    private boolean revealed;
    private BombType bombType;
    private int adjacentBombs;
    private FlagState flagState = FlagState.NONE;

    /**
     * Konstruktor amely egy üres, fel nem fedett cellát hoz létre.
     */
    public Cell() {
        this.revealed = false;
        this.bombType = BombType.NONE;
        this.adjacentBombs = 0;
    }

    /**
     * Megvizsgálja, hogy a cella tartalmaz-e bármilyen bombát.
     *
     * @return true, ha a cellában van bomba
     */
    public boolean hasAnyBomb() {
        return bombType != BombType.NONE;
    }

    /**
     * @return a cellában lévő bombatípus
     */
    public BombType getBombType() {
        return bombType;
    }

    /**
     * Beállítja a cella bombatípusát.
     *
     * @param bombType a beállítandó bombatípus
     */
    public void setBombType(BombType bombType) {
        this.bombType = bombType;
    }

    /**
     * @return a cellát körülvevő bombák száma(A FAKE bombákat nem számolja :) )
     */
    public int getAdjacentBombCount() {
        return adjacentBombs;
    }

    /**
     * Beállítja a szomszédos bombák számát.
     *
     * @param adjacentBombs a szomszédos bombák száma
     */
    public void setAdjacentBombCount(int adjacentBombs) {
        this.adjacentBombs = adjacentBombs;
    }

    /**
     * @return true, ha a cella fel van fedve
     */
    public boolean isRevealed() {
        return revealed;
    }

    /**
     * A cella felfedése
     */
    public void reveal() {
        this.revealed = true;
        this.flagState = FlagState.NONE;
    }

    /**
     * @return a cellán levő zászló állapota
     */
    public FlagState getFlagState() {
        return flagState;
    }

    /**
     * @return true, ha a cellán bármilyen zászló van
     */
    public boolean isFlagged() {
        return flagState != FlagState.NONE;
    }

    /**
     * A zászló állapotát lépteti.
     */
    public void cycleFlag() {
        if (revealed)
            return;

        switch (flagState) {
            case NONE:
                flagState = FlagState.FLAG1;
                break;
            case FLAG1:
                flagState = FlagState.FLAG2;
                break;
            case FLAG2:
                flagState = FlagState.NONE;
                break;
        }

    }
}
