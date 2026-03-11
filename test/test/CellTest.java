package test;

import model.BombType;
import model.Cell;
import model.FlagState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CellTest {
	
    @Test
    void testInitialState() {
        Cell c = new Cell();
        assertFalse(c.isRevealed());
        assertEquals(0, c.getAdjacentBombCount());
        assertEquals(BombType.NONE, c.getBombType());
        assertEquals(FlagState.NONE, c.getFlagState());
    }

    @Test
    void testRevealClearsFlag() {
        Cell c = new Cell();
        c.cycleFlag();
        c.reveal();
        assertTrue(c.isRevealed());
        assertEquals(FlagState.NONE, c.getFlagState());
    }

    @Test
    void testCycleFlagThreeStates() {
        Cell c = new Cell();

        c.cycleFlag();
        assertEquals(FlagState.FLAG1, c.getFlagState());

        c.cycleFlag();
        assertEquals(FlagState.FLAG2, c.getFlagState());

        c.cycleFlag();
        assertEquals(FlagState.NONE, c.getFlagState());
    }

    @Test
    void testBombAndAdjacency() {
        Cell c = new Cell();
        c.setBombType(BombType.WEAK);
        c.setAdjacentBombCount(3);

        assertTrue(c.hasAnyBomb());
        assertEquals(3, c.getAdjacentBombCount());
    }
}
