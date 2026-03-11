package view;

import javax.swing.*;
import java.awt.*;

/**
 * A MainMenuDialog a játék főmenüje, ahol a felhasználó kiválaszthatja
 * a nehézségi szintet, megadhat egyéni tábla méretet és elindíthatja a játékot.
 */
public class MainMenuDialog extends JDialog {
	
	/**
     * A ranglista megnyitását végző interfész.
     */
    public interface HighscoreOpener {
        void openHighscores();
    }

    /**
     * A kiválasztott játékbeállításokat tartalmazó adatszerkezet.
     */
    public static class Settings {
        public final int width;
        public final int height;
        public final int bombs;

        public Settings(int width, int height, int bombs) {
            this.width = width;
            this.height = height;
            this.bombs = bombs;
        }
    }

    private final JRadioButton easyButton;
    private final JRadioButton mediumButton;
    private final JRadioButton hardButton;
    private final JRadioButton customButton;

    private final JSpinner widthSpinner;
    private final JSpinner heightSpinner;
    private final JSpinner bombsSpinner;

    private Settings result;

    private static final int EASY_W = 8;
    private static final int EASY_H = 8;
    private static final int EASY_B = 10;

    private static final int MED_W = 16;
    private static final int MED_H = 16;
    private static final int MED_B = 40;

    private static final int HARD_W = 24;
    private static final int HARD_H = 24;
    private static final int HARD_B = 99;

    private static final Settings EASY   = new Settings(EASY_W,  EASY_H,  EASY_B);
    private static final Settings MEDIUM = new Settings(MED_W,   MED_H,   MED_B);
    private static final Settings HARD   = new Settings(HARD_W,  HARD_H,  HARD_B);

    /**
     * Létrehoz egy új főmenü dialógust, amelyben a felhasználó
     * kiválaszthatja a játék indítási beállításait.
     *
     * @param owner: a dialógust birtokló ablak
     * @param defWidth: az egyéni beállításoknál használt alap szélesség
     * @param defHeight: az egyéni beállításoknál használt alap magasság
     * @param defBombs: az egyéni beállításoknál használt alap bombaszám
     * @param hsOpener: callback a ranglista megnyitásához
     */
    public MainMenuDialog(Frame owner,
                          int defWidth,
                          int defHeight,
                          int defBombs,
                          HighscoreOpener hsOpener) {

        super(owner, "Minesweeper - Főmenü", true);
        setLayout(new BorderLayout(10, 10));
        
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JPanel difficultyPanel = new JPanel(new GridLayout(4, 1));
        difficultyPanel.setBorder(BorderFactory.createTitledBorder("Nehézségi szint"));

        easyButton = new JRadioButton("Könnyű (8×8, 10 bomba)");
        mediumButton = new JRadioButton("Közepes (16×16, 40 bomba)");
        hardButton = new JRadioButton("Nehéz (24×24, 99 bomba)");
        customButton = new JRadioButton("Egyéni");

        ButtonGroup difficultyGroup = new ButtonGroup();
        difficultyGroup.add(easyButton);
        difficultyGroup.add(mediumButton);
        difficultyGroup.add(hardButton);
        difficultyGroup.add(customButton);

        easyButton.setSelected(true);

        difficultyPanel.add(easyButton);
        difficultyPanel.add(mediumButton);
        difficultyPanel.add(hardButton);
        difficultyPanel.add(customButton);

        JPanel customPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        customPanel.setBorder(BorderFactory.createTitledBorder("Egyéni beállítások"));

        customPanel.add(new JLabel("Szélesség (oszlopok):"));
        widthSpinner = new JSpinner(new SpinnerNumberModel(defWidth, 5, 50, 1));
        customPanel.add(widthSpinner);

        customPanel.add(new JLabel("Magasság (sorok):"));
        heightSpinner = new JSpinner(new SpinnerNumberModel(defHeight, 5, 50, 1));
        customPanel.add(heightSpinner);

        customPanel.add(new JLabel("Bombák száma:"));
        bombsSpinner = new JSpinner(new SpinnerNumberModel(defBombs, 1, defWidth * defHeight - 1, 1));
        customPanel.add(bombsSpinner);

        setCustomEnabled(false);

        easyButton.addActionListener(e -> setCustomEnabled(false));
        mediumButton.addActionListener(e -> setCustomEnabled(false));
        hardButton.addActionListener(e -> setCustomEnabled(false));
        customButton.addActionListener(e -> setCustomEnabled(true));

        mainPanel.add(difficultyPanel);
        mainPanel.add(customPanel);

        add(mainPanel, BorderLayout.CENTER);

        JButton highscoresButton = new JButton("Ranglista");
        highscoresButton.addActionListener(e -> {
            if (hsOpener != null) hsOpener.openHighscores();
        });

        JButton exitButton = new JButton("Kilépés");
        exitButton.addActionListener(e -> {
            result = null;
            dispose();
            System.exit(0);
        });

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> onStart());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(highscoresButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(startButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    /**
     * Bekapcsolja vagy kikapcsolja az egyéni spinnereket.
     *
     * @param enabled: engedélyezve legyen-e az egyéni beállítás
     */
    private void setCustomEnabled(boolean enabled) {
        widthSpinner.setEnabled(enabled);
        heightSpinner.setEnabled(enabled);
        bombsSpinner.setEnabled(enabled);
    }

    /**
     * Beolvassa az egyéni (custom) beállításokat a spinnerekből,
     * majd ellenőrzi, hogy a bombák száma nem haladja-e meg a
     * megengedett maximumot.
     *
     * @return: a beolvasott Settings példány, vagy null, ha a bemenet érvénytelen
     */
    private Settings readCustomSettings() {
        int w = (Integer) widthSpinner.getValue();
        int h = (Integer) heightSpinner.getValue();
        int b = (Integer) bombsSpinner.getValue();

        int maxBombs = w * h - 1;
        if (b > maxBombs) {
            JOptionPane.showMessageDialog(
                    this,
                    "Túl sok bomba! Maximum " + maxBombs + " lehet.",
                    "Hiba",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
        return new Settings(w, h, b);
    }

    /**
     * A Start gomb eseménykezelője.
     */
    private void onStart() {
        Settings s;

        if (easyButton.isSelected()) {
            s = EASY;
        } else if (mediumButton.isSelected()) {
            s = MEDIUM;
        } else if (hardButton.isSelected()) {
            s = HARD;
        } else {
            s = readCustomSettings();
            if (s == null) {
                return;
            }
        }

        result = s;
        setVisible(false);
    }

    /**
     * Megjeleníti a dialógust és visszatér a felhasználó által választott beállításokkal.
     * 
     * @return: a kiválasztott játékbeállítások vagy null
     */
    public Settings showDialog() {
        setVisible(true);
        return result;
    }
}
