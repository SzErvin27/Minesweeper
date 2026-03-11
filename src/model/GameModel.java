package model;

import java.util.ArrayDeque;
import java.util.Queue;

public class GameModel {
    private final Board board;
    private final int maxLives;

    private int lives;
    private int elapsedSeconds;
    private GameStatus status;

    private int totalSafeCells;
    private int revealedSafeCells;

    /**
     * A GameModel konstruktora, ami létrehoz egy új játékot.
     *
     * @param board: A játékban használt tábla
     * @param maxLives: A maximális életek száma
     * @throws IllegalArgumentException: ha a board null, vagy maxLives ≤ 0
     */
    public GameModel(Board board, int maxLives) {
        if (board == null) {
            throw new IllegalArgumentException("Board nem lehet null.");
        }
        if (maxLives <= 0) {
            throw new IllegalArgumentException("Az életek száma legyen pozitív.");
        }
        this.board = board;
        this.maxLives = maxLives;
        resetGameState();
    }
    
    /**
     * A játék állapotát alaphelyzetbe állítja.
     */
    private void resetGameState() {
        this.lives = maxLives;
        this.elapsedSeconds = 0;
        this.status = GameStatus.RUNNING;
        this.revealedSafeCells = 0;
        this.totalSafeCells = calculateTotalSafeCells();
    }

    /**
     * Megszámolja, hány cella nem tartalmaz bombát.
     *
     * @return a biztonságos cellák száma
     */
    private int calculateTotalSafeCells() {
        int height = board.getHeight();
        int width = board.getWidth();
        int count = 0;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell cell = board.getCell(row, col);
                if (!cell.hasAnyBomb()) {   
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Növeli az eltelt időt egy másodperccel.
     */
    public void tickOneSecond() {
        if (status == GameStatus.RUNNING) {
            elapsedSeconds++;
        }
    }

    /**
     * A megadott mezőt felfedi.
     * 
     * @param row: a cella sorindexe
     * @param col: a cella oszlopindexe
     */
    public void revealCell(int row, int col) {
        if (status != GameStatus.RUNNING) {
            return;
        }
        if (!board.isInside(row, col)) {
            return;
        }

        Cell cell = board.getCell(row, col);

        if (cell.isRevealed() || cell.isFlagged()) {
            return; 
        }

        if (!cell.hasAnyBomb()) {
            if (cell.getAdjacentBombCount() == 0) {
                int newlyRevealed = revealSafeRegion(row, col);
                revealedSafeCells += newlyRevealed;
            } else {
                cell.reveal();
                revealedSafeCells++;
            }
            checkWinCondition();
        } else {
            cell.reveal();
            int damage = cell.getBombType().getDamage();
            if (damage > 0) {
                lives -= damage;
                if (lives <= 0) {
                    lives = 0;
                    status = GameStatus.LOST;
                    revealAllBombs();
                }
            }
        }
    }

    /**
     * A cellán levő zászlót váltja.
     *
     * @param row: a sorindex
     * @param col: az oszlopindex
     */
    public void toggleFlag(int row, int col) {
        if (status != GameStatus.RUNNING) {
            return;
        }
        if (!board.isInside(row, col)) {
            return;
        }
        Cell cell = board.getCell(row, col);
        cell.cycleFlag();
    }
   
    /**
     * Ellenőrzi, hogy a játékos megnyerte-e a játékot.
     */
    private void checkWinCondition() {
        if (revealedSafeCells >= totalSafeCells && status == GameStatus.RUNNING) {
            status = GameStatus.WON;
        }
    }

    /**
     * Felfedi az összes bombát. (amikor a játékos elveszítette a játékot)
     */
    private void revealAllBombs() {
        int height = board.getHeight();
        int width = board.getWidth();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell cell = board.getCell(row, col);
                if (cell.hasAnyBomb() && !cell.isRevealed()) {
                    cell.reveal();
                }
            }
        }
    }

    /**
     * Felfedi az összefüggő biztonságos területet, valamint a terület szélén található pozitív szomszédszámú cellákat.
     * A művelet BFS-szerű körbejárást használ.
     *
     * @param startRow: a kezdő sorindex
     * @param startCol: a kezdő oszlopindex
     * @return: hány új cellát fedtünk fel
     */
    private int revealSafeRegion(int startRow, int startCol) {
        int newlyRevealed = 0;

        Queue<int[]> queue = new ArrayDeque<>(); //felfedésre váró cellák
        queue.add(new int[]{startRow, startCol});

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int row = pos[0];
            int col = pos[1];

            if (!board.isInside(row, col)) {
                continue;
            }

            Cell cell = board.getCell(row, col);

            if (cell.isRevealed() || cell.hasAnyBomb()) {
                continue;
            }

            cell.reveal();
            newlyRevealed++;

            if (cell.getAdjacentBombCount() == 0) {
                for (int x = -1; x <= 1; x++) { //sor
                    for (int y = -1; y <= 1; y++) { //oszlop
                        if (x == 0 && y == 0) {
                            continue;
                        }
                        int nx = row + x; //szomszédos sor
                        int ny = col + y; //szomszédos oszlop
                        if (!board.isInside(nx, ny)) {
                            continue;
                        }
                        Cell neighbour = board.getCell(nx, ny);

                        if (neighbour.isRevealed() || neighbour.isFlagged()) {
                            continue;
                        }

                        if (!neighbour.hasAnyBomb()) {
                            if (neighbour.getAdjacentBombCount() == 0) {
                                queue.add(new int[]{nx, ny});
                            } else {
                                neighbour.reveal();
                                newlyRevealed++;
                            }
                        }
                    }
                }
            }
        }

        return newlyRevealed;
    }

    /** 
     * @return: a tábla 
     **/
    public Board getBoard() {
        return board;
    }

    /**
     * @return: az aktuális életek 
     **/
    public int getLives() {
        return lives;
    }

    /** 
     * @return: a maximális életszám
     **/
    public int getMaxLives() {
        return maxLives;
    }

    /**
     * @return: az eltelt idő
     */
    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    /**
     * @return: a játék állapota
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * @return true, ha véget ért a játék
     */
    public boolean isGameOver() {
        return status == GameStatus.WON || status == GameStatus.LOST;
    }

    /**
     * @return true, ha megnyerte a játékot 
     */
    public boolean isWon() {
        return status == GameStatus.WON;
    }
}
