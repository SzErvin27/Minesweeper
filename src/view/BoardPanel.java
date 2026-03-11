package view;

import model.Board;
import model.Cell;
import model.FlagState;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.function.BiConsumer;

/**
 * A BoardPanel a játék tábla grafikus megjelenítéséért felelős.
 */
public class BoardPanel extends JPanel {
    private JButton[][] buttons; //a cellák
    private int width;
    private int height;

    private Icon bombIcon;
    private Icon flagIcon1;
    private Icon flagIcon2;

    /**
     * Létrehoz egy új táblát.
     */
    public BoardPanel() {
        setLayout(new BorderLayout());
        loadIcons();
    }

    /**
     * Betölti az ikonokat.
     */
    private void loadIcons() {
        bombIcon = loadIcon("/img/bomb.png");
        flagIcon1 = loadIcon("/img/flag.png");
        flagIcon2 = loadIcon("/img/flag_alt.png");
    }

    /**
     * Betölti és 24×24 pixelre méretezi az ikont.
     *
     * @param path: az ikon elérési útja
     * @return: méretezett ikon vagy null, ha a fájl nem található
     */
    private Icon loadIcon(String path) {
        URL url = getClass().getResource(path);
        if (url == null) {
            System.out.println("Nem található ikon: " + path);
            return null;
        }
        ImageIcon original = new ImageIcon(url);
        Image scaled = original.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /**
     * Inicializálja a tábla gombjait és létrehoz egy rács elrendezést.
     *
     * @param board: a játék táblája
     * @param onLeftClick: a bal kattintás eseménykezelője
     * @param onRightClick: a jobb kattintás eseménykezelője
     */
    public void initBoard(Board board,
                          BiConsumer<Integer, Integer> onLeftClick,
                          BiConsumer<Integer, Integer> onRightClick) {

        this.width = board.getWidth();
        this.height = board.getHeight();
        this.buttons = new JButton[height][width];

        JPanel grid = new JPanel(new GridLayout(height, width));

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                JButton btn = new JButton();
                btn.setMargin(new Insets(0, 0, 0, 0));
                btn.setFont(new Font("Arial", Font.BOLD, 16));
                btn.setPreferredSize(new Dimension(40, 40));

                final int r = row;
                final int c = col;

                //bal kattintás
                btn.addActionListener(e -> {
                    if (onLeftClick != null) {
                        onLeftClick.accept(r, c);
                    }
                });

                //jobb kattintás
                btn.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent e) {
                        if (e.isPopupTrigger() && onRightClick != null) {
                            onRightClick.accept(r, c);
                        }
                    }

                    @Override
                    public void mouseReleased(java.awt.event.MouseEvent e) {
                        if (e.isPopupTrigger() && onRightClick != null) {
                            onRightClick.accept(r, c);
                        }
                    }
                });

                buttons[row][col] = btn;
                grid.add(btn);
            }
        }

        removeAll();
        add(grid, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /**
     * Frissíti a megadott cellát.
     * 
     * @param row: a sorindex
     * @param col: az oszlopindex
     * @param cell: a cella
     */
    public void updateCell(int row, int col, Cell cell) {
        JButton btn = buttons[row][col];

        btn.setIcon(null);
        btn.setText("");

        if (!cell.isRevealed()) {
            FlagState fs = cell.getFlagState();
            if (fs == FlagState.FLAG1) {
                if (flagIcon1 != null) btn.setIcon(flagIcon1);
                else btn.setText("F");
            } else if (fs == FlagState.FLAG2) {
                if (flagIcon2 != null) btn.setIcon(flagIcon2);
                else btn.setText("?");
            } else {
                btn.setIcon(null);
                btn.setText("");
            }
            btn.setBackground(null);
            return;
        }

        if (cell.hasAnyBomb()) {
            if (bombIcon != null) {
                btn.setIcon(bombIcon);
            } else {
                btn.setText("B");
            }
            btn.setBackground(Color.RED);
            return;
        }

        int count = cell.getAdjacentBombCount();
        if (count > 0) {
            btn.setText(String.valueOf(count));
        } else {
            btn.setText("");
        }
        btn.setBackground(Color.LIGHT_GRAY);
    }

    /**
     * A teljes tábla frissítése.
     * Végigmegy minden cellán és újrarajzolja azt.
     *
     * @param board: a tábla aktuális állapota
     */
    public void refresh(Board board) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                updateCell(row, col, board.getCell(row, col));
            }
        }
    }
}
