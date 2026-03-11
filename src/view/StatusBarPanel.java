package view;

import model.GameStatus;

import javax.swing.*;
import java.awt.*;

/**
 * A StatusBarPanel a játék alsó státuszsávát jeleníti meg.
 */
public class StatusBarPanel extends JPanel {
    private final JLabel livesLabel;
    private final JLabel timeLabel;
    private final JLabel statusLabel;

    /**
     * Konstruktor, amely létrehoz egy új státuszsáv panelt.
     */
    public StatusBarPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        livesLabel = new JLabel("Életek: 0/0");
        timeLabel = new JLabel("Idő: 0 s");
        statusLabel = new JLabel("Állapot: -");

        add(livesLabel);
        add(Box.createHorizontalStrut(20));
        add(timeLabel);
        add(Box.createHorizontalStrut(20));
        add(statusLabel);
    }

    /**
     * Frissíti a státuszsáv megjelenített értékeit:
     * 
     * @param lives:      a játékos aktuális életei
     * @param maxLives:   a játékos maximális életei
     * @param seconds:    az eltelt idő
     * @param gameStatus: a játék aktuális állapota
     */
    public void update(int lives, int maxLives, int seconds, GameStatus gameStatus) {
        livesLabel.setText("Életek: " + lives + "/" + maxLives);
        timeLabel.setText("Idő: " + seconds + " s");

        String statusText;
        switch (gameStatus) {
            case RUNNING:
                statusText = "Fut";
                break;
            case WON:
                statusText = "Nyertél!";
                break;
            case LOST:
                statusText = "Vesztettél!";
                break;
            default:
                statusText = "-";
                break;
        }

        statusLabel.setText("Állapot: " + statusText);
    }
}
