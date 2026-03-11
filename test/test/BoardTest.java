package test;

import model.Board;
import model.BombType;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void testBoardSize() {
        Board b = new Board(10, 8, 5, new Random(1));
        assertEquals(10, b.getWidth());
        assertEquals(8, b.getHeight());
    }

    @Test
    void testBombCountRoughlyMatchesRequested() {
        int width = 10;
        int height = 10;
        int bombs = 8;
        Board b = new Board(width, height, bombs, new Random(1));

        int count = 0;
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (b.getCell(r, c).getBombType() != BombType.NONE) {
                    count++;
                }
            }
        }

        assertEquals(bombs, count);
    }

    @Test
    void testInsideBoard() {
        Board b = new Board(5, 5, 1, new Random());
        assertTrue(b.isInside(0, 0));
        assertTrue(b.isInside(4, 4));
        assertFalse(b.isInside(5, 0));
        assertFalse(b.isInside(-1, 3));
    }
}
