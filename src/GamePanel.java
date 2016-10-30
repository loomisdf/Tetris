import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by loomisdf on 10/28/16.
 */
public class GamePanel extends JPanel implements Runnable {
    private static final int PWIDTH = 500;
    private static final int PHEIGHT = 400;

    private Thread animator;
    private boolean running = false;
    private int period = 10;

    private boolean gameOver = false;

    private Graphics dbg;
    private Image dbImage = null;


    public GamePanel() {
        setBackground(Color.white);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        setFocusable(true);
        requestFocus();
        readyForTermination();

        // create game components

        // listen for mouse presses
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                testPress(mouseEvent.getX(), mouseEvent.getY());
            }
        });
    }

    private void testPress(int x, int y) {
        if(!gameOver) {
            // TODO do something
            System.out.println("x = [" + x + "], y = [" + y + "]");
        }
    }

    private void readyForTermination() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
               int keyCode = e.getKeyCode();
                if((keyCode == KeyEvent.VK_ESCAPE) ||
                        (keyCode == KeyEvent.VK_Q) ||
                        (keyCode == KeyEvent.VK_END) ||
                        (keyCode == KeyEvent.VK_C) && e.isControlDown()) {
                    running = false;
                }
            }
        });
    }

    public void addNotify() {
        super.addNotify();
        startGame();
    }

    private void startGame() {
        if(animator == null || !running) {
            animator = new Thread(this);
            animator.start();
        }
    }

    public void stopGame() {
        running = false;
    }

    public void gameUpdate() {
        if(!gameOver) {
            // update game state
        }
    }

    public void gameRender() {
        if(dbImage == null) {
            dbImage = createImage(PWIDTH, PHEIGHT);
            if(dbImage == null) {
                System.out.println("dbImage is null");
                return;
            }
            else {
                dbg = dbImage.getGraphics();
            }
        }
        // clear the background
        dbg.setColor(Color.white);
        dbg.fillRect(0, 0, PWIDTH, PHEIGHT);

        // TODO draw game elements

        if(gameOver) {
            gameOverMessage(dbg);
        }
    }

    private void gameOverMessage(Graphics dbg) {
        dbg.drawString("game over", 0, 0);
    }

    @Override
    public void run() {
        long beforeTime, timeDiff, sleepTime;

        beforeTime = System.currentTimeMillis();

        running = true;
        while(running) {
            gameUpdate();
            gameRender();
            paintScreen();
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleepTime = period - timeDiff;

            if(sleepTime <= 0)
                sleepTime = 5;
            try {
                Thread.sleep(sleepTime);
            }
            catch(InterruptedException e) {

            }
            System.exit(0);
        }
    }

    private void paintScreen() {
        Graphics g;
        try {
            g = this.getGraphics();
            if((g != null) && dbImage != null) {
                g.drawImage(dbImage, 0, 0, null);
            }
            g.dispose();
        }
        catch(Exception e) {
            System.out.println("Graphics context error: " + e);
        }
    }
}
