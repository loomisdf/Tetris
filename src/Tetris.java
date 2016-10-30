import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by loomisdf on 10/28/16.
 */
public class Tetris {
    public static final int EMPTY = 0;
    public static final int BLOCK = 1;
    public static final int ROWS = 20;
    public static final int COLS = 10;
    public static final int QUEUE_SIZE = 10;
    public static int[][] brd;

    public static Queue<Tetrimino> tetriminoQueue = new LinkedList<Tetrimino>();
    public static Tetrimino curr_tetrimino;

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

    public static void main(String[] args) {
        init();
        printBoard();
        curr_tetrimino.moveRight(brd);
        printBoard();
    }
}
