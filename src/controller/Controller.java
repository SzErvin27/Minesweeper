package controller;

import model.Board;
import model.Difficulty;
import model.GameModel;
import model.GameStatus;
import model.Highscore;
import model.ScoreEntry;
import view.MainFrame;
import view.HighscoreDialog;
import view.MainMenuDialog;

import javax.swing.*;
import java.io.File;
import java.util.Random;

/**
 * A Controller osztály felelős az alkalmazás fő vezérléséért.
 */
public class Controller {
    private int boardWidth = 16;
    private int boardHeight = 16;
    private int bombCount = 40;
    private int lives = 3;

    private MainFrame mainFrame;
    private Highscore highscore;
    private HighscoreDialog highscoreDialog;
    private GameModel gameModel;
    private Board board;
    private Timer timer;

    private final File highscoreFile = new File("data/highscores.dat");
    
    /**
     * A Controller konstruktora
     */
    public Controller() {
        highscore = new Highscore();
        highscore.loadFromFile(highscoreFile);

        mainFrame = new MainFrame();
        highscoreDialog = new HighscoreDialog(mainFrame, highscore);

        if (!showMainMenuAndApplySettings()) {
            System.exit(0);
        }
        createNewGame();

        initMenuActions();
        initTimer();

        mainFrame.setVisible(true);
    }
    
    /**
     * Megjeleníti a főmenüt, lekéri a felhasználó által választott beállításokat, majd alkalmazza azokat
     *
     * @return: true ha a beállításokat sikerült érvényesíteni. false ha nem
     */
    private boolean showMainMenuAndApplySettings() {
        MainMenuDialog menu = new MainMenuDialog(
                mainFrame,
                boardWidth,
                boardHeight,
                bombCount,
                () -> highscoreDialog.showDialog()
        );

        MainMenuDialog.Settings settings = menu.showDialog();

        if (settings == null) {
            return false;
        }

        boardWidth = settings.width;
        boardHeight = settings.height;
        bombCount = settings.bombs;

        return true;
    }
    
    /**
     * Új játékot hoz létre az aktuális beállítások alapján
     */
    private void createNewGame() {
        board = new Board(boardWidth, boardHeight, bombCount, new Random());
        gameModel = new GameModel(board, lives);

        mainFrame.initBoard(board, this::onLeftClick, this::onRightClick);
        mainFrame.refreshBoard(board);
        mainFrame.updateStatus(gameModel);
    }
    
    /**
     * Beállítja a menüben található funkciókhoz tartozó eseménykezelőket
     */
    private void initMenuActions() {
        mainFrame.setNewGameAction(e -> onNewGame());
        mainFrame.setHighscoresAction(e -> onShowHighscores());
        mainFrame.setExitAction(e -> onExit());
    }

    /**
     * Elindítja az időzítőt
     */
    private void initTimer() {
        timer = new Timer(1000, e -> {
            if (!gameModel.isGameOver()) {
                gameModel.tickOneSecond();
                mainFrame.updateStatus(gameModel);
            }
        });
        timer.start();
    }
    
    /**
     * Kezeli az „Új játék” menüpontra kattintást
     */
    private void onNewGame() {
        if (timer != null) {
            timer.stop();
        }

        showMainMenuAndApplySettings();
        createNewGame();
        initTimer();
    }

    /**
     * Megjeleníti a highscore ablakot
     */
    private void onShowHighscores() {
        highscoreDialog.showDialog();
    }
    
    /**
     * Mentés és kilépés
     */
    private void onExit() {
        highscore.saveToFile(highscoreFile);
        System.exit(0);
    }

    /**
     * Bal egérgomb kattintás kezelése
     * 
     * @param row: a sor indexe
     * @param col: az oszlop indexe
     */
    private void onLeftClick(int row, int col) {
        if (gameModel.isGameOver()) {
            return;
        }

        gameModel.revealCell(row, col);
        mainFrame.refreshBoard(board);
        mainFrame.updateStatus(gameModel);

        if (gameModel.isGameOver()) {
            timer.stop();
            if (gameModel.getStatus() == GameStatus.WON) {
                handleWin();
            } else {
                handleLoss();
            }
        }
    }

    /**
     * Jobb egérgomb kattintás kezelése
     *
     * @param row: a sor indexe
     * @param col: az oszlop indexe
     */
    private void onRightClick(int row, int col) {
        if (gameModel.isGameOver()) {
            return;
        }

        gameModel.toggleFlag(row, col);   
        mainFrame.refreshBoard(board);
        mainFrame.updateStatus(gameModel);
    }

    /**
     * Győzelem kezelése
     */
    private void handleWin() {
        int time = gameModel.getElapsedSeconds();
        String name = JOptionPane.showInputDialog(
                mainFrame,
                "Nyertél! Időd: " + time + " s\nAdd meg a neved:",
                "Győzelem",
                JOptionPane.PLAIN_MESSAGE
        );

        if (name != null && !name.isBlank()) {
        	Difficulty diff;

        	if (boardWidth == 24 && boardHeight == 24 && bombCount == 99)
        	    diff = Difficulty.HARD;
        	else if (boardWidth == 16 && boardHeight == 16 && bombCount == 40)
        	    diff = Difficulty.MEDIUM;
        	else if (boardWidth == 8 && boardHeight == 8 && bombCount == 10)
        	    diff = Difficulty.EASY;
        	else
        	    diff = Difficulty.CUSTOM;

        	ScoreEntry entry = new ScoreEntry(name.trim(), time, diff);

            highscore.addScore(entry);
            highscore.saveToFile(highscoreFile);
        }

        highscoreDialog.refresh();
    }

    /**
     * Vereség kezelése
     */
    private void handleLoss() {
        JOptionPane.showMessageDialog(
                mainFrame,
                "Elfogytak az életeid. Vesztettél!",
                "Vesztettél",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
