package test;

import model.*;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {

    @Test
    void testInitialStatus() {
        Board b = new Board(5, 5, 1, new Random());
        GameModel gm = new GameModel(b, 3);

        assertEquals(GameStatus.RUNNING, gm.getStatus());
        assertEquals(3, gm.getLives());
    }

    @Test
    void testWinConditionOnAllSafeCellsRevealed() {
        Board b = new Board(2, 2, 0, new Random());
        GameModel gm = new GameModel(b, 3);

        gm.revealCell(0, 0);
        gm.revealCell(0, 1);
        gm.revealCell(1, 0);
        gm.revealCell(1, 1);

        assertEquals(GameStatus.WON, gm.getStatus());
    }

    @Test
    void testFlagPreventsReveal() {
        Board b = new Board(3, 3, 0, new Random());
        GameModel gm = new GameModel(b, 3);

        gm.toggleFlag(1, 1);
        gm.revealCell(1, 1);

        assertFalse(b.getCell(1, 1).isRevealed());
    }
}
