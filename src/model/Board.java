package model;

import java.util.Random;

/**
 * A Board osztály tartalmazza a mezőket, elhelyezi a bombákat és kiszámítja a
 * szomszédos bombák számát
 */
public class Board {
    private final int width;
    private final int height;
    private final Cell[][] cells;

    /**
     * Létrehoz egy új aknamezőt a megadott paraméterek alapján
     *
     * @param width:     oszlopok száma
     * @param height:    sorok száma
     * @param bombCount: bombák száma
     * @param random:    véletlenszám generátor a bombák elhelyezéséhez
     * @throws IllegalArgumentException: ha width vagy height nem nagyobb mint 0,
     *                                   vagy a bombaszám negatív vagy túl nagy,
     *                                   vagy a random értéke null
     */
    public Board(int width, int height, int bombCount, Random random) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("A tábla mérete legyen pozitív!");
        }
        if (bombCount < 0 || bombCount > width * height) {
            throw new IllegalArgumentException("Érvénytelen bombaszám: " + bombCount);
        }
        if (random == null) {
            throw new IllegalArgumentException("Random nem lehet null.");
        }

        this.width = width;
        this.height = height;
        this.cells = new Cell[height][width];

        initEmptyBoard();
        placeBombs(bombCount, random);
        calculateAdjacentBombCounts();
    }

    /**
     * Inicializálja a táblát üres mezőkkel
     */
    private void initEmptyBoard() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                cells[row][col] = new Cell();
                cells[row][col].setBombType(BombType.NONE);
                cells[row][col].setAdjacentBombCount(0);
            }
        }
    }

    /**
     * Véletlenszerűen elhelyez adott számú bombát a táblán
     *
     * @param bombCount: a bombák száma
     * @param random:    véletlenszám generátor
     */
    private void placeBombs(int bombCount, Random random) {
        int placed = 0;
        while (placed < bombCount) {
            int row = random.nextInt(height);
            int col = random.nextInt(width);

            Cell cell = cells[row][col];
            if (cell.getBombType() == BombType.NONE) {
                BombType type = randomBombType(random);
                cell.setBombType(type);
                placed++;
            }
        }
    }

    /**
     * Véletlenszerűen választ egy bombatípust.
     *
     * @param random: véletlenszám-generátor
     * @return: generált bombatípus
     */
    private BombType randomBombType(Random random) {
        int r = random.nextInt(4);

        switch (r) {
            case 0:
                return BombType.WEAK;
            case 1:
                return BombType.MEDIUM;
            case 2:
                return BombType.STRONG;
            default:
                return BombType.FAKE;
        }
    }

    /**
     * Kiszámítja, hogy az egyes mezők szomszédságában hány valódi bomba található
     * A FAKE bomba nem növeli a szomszédos mezők értékét :)
     */
    private void calculateAdjacentBombCounts() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (cells[row][col].getBombType() != BombType.NONE && cells[row][col].getBombType() != BombType.FAKE) {
                    incrementNeighbours(row, col);
                }
            }
        }
    }

    /**
     * Egy bomba körüli 8 szomszéd mező szomszédos bombaszámát növeli.
     *
     * @param row: bomba sorindexe
     * @param col: bomba oszlopindexe
     */
    private void incrementNeighbours(int row, int col) {
        for (int x = -1; x <= 1; x++) { // sor
            for (int y = -1; y <= 1; y++) { // oszlop
                if (x == 0 && y == 0) {
                    continue;
                }
                int nx = row + x; // szomszédos sor
                int ny = col + y; // szomszédos oszlop
                if (isInside(nx, ny)) {
                    Cell neighbour = cells[nx][ny];
                    int current = neighbour.getAdjacentBombCount();
                    neighbour.setAdjacentBombCount(current + 1);
                }
            }
        }
    }

    /**
     * @return: oszlopok száma
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return: sorok száma
     */
    public int getHeight() {
        return height;
    }

    /**
     * Megvizsgálja hogy az adott koordináták a táblán belül vannak-e
     *
     * @param row: a sor indexe
     * @param col: az oszlop indexe
     * @return: true, ha a cella érvényes pozíción van, különben false
     */
    public boolean isInside(int row, int col) {
        return row >= 0 && row < height && col >= 0 && col < width;
    }

    /**
     * Visszaadja a megadott cellát
     *
     * @param row: a sor indexe
     * @param col: az oszlop indexe
     * @return: a kért cella
     * @throws IndexOutOfBoundsException: ha a pozíció érvénytelen
     */
    public Cell getCell(int row, int col) {
        if (!isInside(row, col)) {
            throw new IndexOutOfBoundsException("Érvénytelen cella: (" + row + ", " + col + ")");
        }
        return cells[row][col];
    }
}
