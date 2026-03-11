package test;

import model.Difficulty;
import model.Highscore;
import model.ScoreEntry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HighscoreTest {

    @Test
    void testAddingAndSortingScores() {
        Highscore hs = new Highscore();

        hs.addScore(new ScoreEntry("A", 30, Difficulty.HARD));
        hs.addScore(new ScoreEntry("B", 20, Difficulty.MEDIUM));
        hs.addScore(new ScoreEntry("C", 10, Difficulty.EASY));

        List<ScoreEntry> entries = hs.getEntries();

        assertEquals(3, entries.size());
        assertEquals("A", entries.get(0).getName());
        assertEquals("B", entries.get(1).getName());
        assertEquals("C", entries.get(2).getName());
    }
}
