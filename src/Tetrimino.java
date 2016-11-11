import java.awt.*;

/**
 * Created by loomisdf on 10/28/16.
 */
public class Tetrimino {
    public Block[] blocks;
    public TetriminoType tType;
    private Color color = Color.blue;

    public Tetrimino(TetriminoType tType) {
        this.tType = tType;
        this.blocks = new Block[4];

        switch (tType) {
            case SQUARE:
                blocks[0] = new Block(0, 4);
                blocks[1] = new Block(0, 5);
                blocks[2] = new Block(1, 4);
                blocks[3] = new Block(1, 5);
                break;
            case T:
                blocks[0] = new Block(0, 4);
                blocks[1] = new Block(1, 3);
                blocks[2] = new Block(1, 4);
                blocks[3] = new Block(1, 5);
                break;
            case S_L:
                blocks[0] = new Block(0, 4);
                blocks[1] = new Block(0, 5);
                blocks[2] = new Block(1, 3);
                blocks[3] = new Block(1, 4);
                break;
            case S_R:
                blocks[0] = new Block(0, 3);
                blocks[1] = new Block(0, 4);
                blocks[2] = new Block(1, 4);
                blocks[3] = new Block(1, 5);
                break;
            case L_L:
                blocks[0] = new Block(0, 5);
                blocks[1] = new Block(1, 3);
                blocks[2] = new Block(1, 4);
                blocks[3] = new Block(1, 5);
                break;
            case L_R:
                blocks[0] = new Block(0, 3);
                blocks[1] = new Block(1, 3);
                blocks[2] = new Block(1, 4);
                blocks[3] = new Block(1, 5);
                break;
            case LINE:
                blocks[0] = new Block(0, 3);
                blocks[1] = new Block(0, 4);
                blocks[2] = new Block(0, 5);
                blocks[3] = new Block(0, 6);
                break;
        }
    }

    public void draw(Graphics g, Grid grid) {
        int block_width = grid.getWidth() / 10;
        int block_height = grid.getHeight() / 20;

        g.setColor(color);
        for(Block b : blocks) {
            g.fillRect(grid.getX() + (b.col * block_width), grid.getY() + (b.row * block_height), block_width, block_height);
        }
    }

    public boolean containsBlock(int row, int col) {
        for(Block b : blocks) {
            if(b.row == row && b.col == col) {
                return true;
            }
        }
        return false;
    }

    public void moveRight() {
        // check for room
        for(Block b : blocks) {
            if(b.col == Tetris.COLS - 1 || Tetris.brd[b.row][b.col + 1] == Tetris.BLOCK) {
                return;
            }
        }
        for(Block b : blocks) {
            b.col++;
        }
    }
    public void moveLeft() {
        // check for room
        for(Block b : blocks) {
            if(b.col == 0 || Tetris.brd[b.row][b.col - 1] == Tetris.BLOCK) {
                return;
            }
        }
        for(Block b : blocks) {
            b.col--;
        }
    }
    public boolean moveDown() {
        // check for room
        for(Block b : blocks) {
            if(b.row == Tetris.ROWS - 1 || Tetris.brd[b.row + 1][b.col] == Tetris.BLOCK) {
                return false;
            }
        }
        for(Block b : blocks) {
            b.row++;
        }
        return true;
    }
    public void rotateRight(int[][] brd) {
        if(tType == TetriminoType.SQUARE) return;
    }
    public void rotateLeft(Block[][] brd) {
        if(tType == TetriminoType.SQUARE) return;

    }
}
