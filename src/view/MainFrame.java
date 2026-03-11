package view;

import model.Board;
import model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;

/**
 * A MainFrame a játék főablaka.
 */
public class MainFrame extends JFrame {
    private final BoardPanel boardPanel;
    private final StatusBarPanel statusBarPanel;
    private final JMenuItem newGameItem;
    private final JMenuItem highscoresItem;
    private final JMenuItem exitItem;
    
    /**
     * Konstruktor, ami létrehoz egy új főablakot.
     * Inicializálja a menüsort, a játéktáblát és az állapotsort.
     */
    public MainFrame() {
        super("Minesweeper");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Játék");

        newGameItem = new JMenuItem("Új játék");
        highscoresItem = new JMenuItem("Ranglista");
        exitItem = new JMenuItem("Kilépés");

        gameMenu.add(newGameItem);
        gameMenu.add(highscoresItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        menuBar.add(gameMenu);
        setJMenuBar(menuBar);

        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        statusBarPanel = new StatusBarPanel();
        add(statusBarPanel, BorderLayout.SOUTH);

        setSize(900, 700);
        setLocationRelativeTo(null);
    }

    /**
     * Inicializálja a játékmezőt.
     *
     * @param board: a játék táblája
     * @param onLeftClick: bal kattintás eseménykezelője
     * @param onRightClick: jobb kattintás eseménykezelője
     */
    public void initBoard(Board board,
                          BiConsumer<Integer, Integer> onLeftClick,
                          BiConsumer<Integer, Integer> onRightClick) {

        boardPanel.initBoard(board, onLeftClick, onRightClick);
        setLocationRelativeTo(null);
    }

    /**
     * Frissíti a táblát a megadott tábla alapján.
     *
     * @param board: a friss állapotot tartalmazó tábla
     */
    public void refreshBoard(Board board) {
        boardPanel.refresh(board);
    }

    /**
     * Frissíti a státuszsávot a játék aktuális adatai alapján.
     *
     * @param model: a játékmodell, amely tartalmazza az életeket, időt, státuszt
     */
    public void updateStatus(GameModel model) {
        statusBarPanel.update(
                model.getLives(),
                model.getMaxLives(),
                model.getElapsedSeconds(),
                model.getStatus()
        );
    }

    /**
     * Eseménykezelőt köt az "Új játék" gombra.
     *
     * @param listener: az új játék indításakor meghívandó listener
     */
    public void setNewGameAction(ActionListener listener) {
        newGameItem.addActionListener(listener);
    }

    /**
     * Eseménykezelőt köt a "Ranglista" gombra.
     *
     * @param listener: a ranglista megnyitásakor meghívandó listener
     */
    public void setHighscoresAction(ActionListener listener) {
        highscoresItem.addActionListener(listener);
    }

    /**
     * Eseménykezelőt köt a "Kilépés" gombra.
     *
     * @param listener: a kilépésnél meghívandó listener
     */
    public void setExitAction(ActionListener listener) {
        exitItem.addActionListener(listener);
    }
}
