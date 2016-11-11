import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by loomisdf on 10/28/16.
 */
public class Tetris extends JFrame implements WindowListener{
    private static int DEFAULT_FPS = 80;

    private GamePanel gp;
    private Grid grid;


    public static final int EMPTY = 0;
    public static final int BLOCK = 1;
    public static final int ROWS = 20;
    public static final int COLS = 10;
    public static final int QUEUE_SIZE = 10;
    public static int[][] brd;

    public static Queue<Tetrimino> tetriminoQueue = new LinkedList<Tetrimino>();
    public static Tetrimino curr_tetrimino;

    public Tetris(long period) {
        grid = new Grid(15, 15, 150, 300);

        makeGUI(period);
        init();

        addWindowListener(this);
        pack();
        setResizable(false);
        setVisible(true);
    }

    private void makeGUI(long period) {
        Container c = getContentPane();
        gp = new GamePanel(this, period);
        c.add(gp, "Center");

    }

    public static void init() {
        // Fill the brd with empty spaces
        brd = new int[ROWS][COLS];
        for(int[] r : brd) {
            for(int c : r) {
                r[c] = EMPTY;
            }
        }
        // TODO abstract this into its own data structure called TetriminoQueue
        // populate the tetrimino queue
        for(int i = 0; i < QUEUE_SIZE; i++) {
            int type = ThreadLocalRandom.current().nextInt(0, 6 + 1);
            switch(type) {
                case 0:
                    tetriminoQueue.add(new Tetrimino(TetriminoType.SQUARE));
                    break;
                case 1:
                    tetriminoQueue.add(new Tetrimino(TetriminoType.T));
                    break;
                case 2:
                    tetriminoQueue.add(new Tetrimino(TetriminoType.S_L));
                    break;
                case 3:
                    tetriminoQueue.add(new Tetrimino(TetriminoType.S_R));
                    break;
                case 4:
                    tetriminoQueue.add(new Tetrimino(TetriminoType.L_L));
                    break;
                case 5:
                    tetriminoQueue.add(new Tetrimino(TetriminoType.L_R));
                    break;
                case 6:
                    tetriminoQueue.add(new Tetrimino(TetriminoType.LINE));
                    break;
            }
        }
        // take off the next tetrimino
        curr_tetrimino = tetriminoQueue.remove();
    }

    public static void printBoard() {
        for(int r = 0; r < ROWS; r++) {
            System.out.print("|");
            for(int c = 0; c < COLS; c++) {
                if(curr_tetrimino.containsBlock(r, c)) {
                    System.out.print("T");
                }
                else if(brd[r][c] == EMPTY) {
                    System.out.print(".");
                }
                else if(brd[r][c] == BLOCK) {
                    System.out.print("B");
                }
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("____________");
    }

    public static void checkForTetris() {

    }

    public void update() {

    }

    public void draw(Graphics g) {

        g.setColor(Color.black);
        grid.draw(g); // draw the grid
        curr_tetrimino.draw(g, grid); // draw the current tetrimino
    }

    public static void main(String[] args) {
        int fps = DEFAULT_FPS;
        if(args.length != 0) {
            fps = Integer.parseInt(args[0]);
        }

        long period = (long) 1000.0 / fps;
        System.out.println("Fps: " + fps + "; period: " + period + " ms");
        new Tetris(period*1000000L);

        //curr_tetrimino.moveRight(brd);
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        gp.stopGame();
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {
        gp.pauseGame();
    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {
        gp.resumeGame();
    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {
        gp.resumeGame();
    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {
        gp.pauseGame();
    }
}
