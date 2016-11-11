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
    private static final int NO_DELAYS_PER_YIELD = 16;
    private static int MAX_FRAME_SKIPS = 5;

    private Thread animator;
    private boolean running = false;
    private boolean isPaused = false;
    private long period;

    private boolean gameOver = false;

    private Graphics dbg;
    private Image dbImage = null;

    private Tetris tTop;

    public GamePanel(Tetris tetris, long period) {
        tTop = tetris;
        this.period = period;

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
        if(!isPaused && !gameOver) {
            // TODO do something
            System.out.println("Mouse pressed: x = [" + x + "], y = [" + y + "]");
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

    public void pauseGame() {
        isPaused = true;
    }

    public void resumeGame() {
        isPaused = false;
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
        if(!isPaused && !gameOver) {
            tTop.update();
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
        tTop.draw(dbg);

        if(gameOver) {
            gameOverMessage(dbg);
        }
    }

    private void gameOverMessage(Graphics dbg) {
        dbg.drawString("game over", 0, 0);
    }

    @Override
    public void run() {
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        long excess = 0L;

        beforeTime = System.nanoTime();

        running = true;
        while(running) {
            gameUpdate();
            gameRender();
            paintScreen();

            afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            sleepTime = (period - timeDiff) - overSleepTime;

            if(sleepTime > 0) { // some time left in this cycle
                try {
                    Thread.sleep(sleepTime / 1000000L); // nano -> ms
                } catch (InterruptedException e) {
                    overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
                }
            }
            else { // sleepTime <= 0; frame took longer than the period
                excess -= sleepTime; // store excess time value
                overSleepTime = 0L;

                if(++noDelays >= NO_DELAYS_PER_YIELD) {
                    Thread.yield();
                    noDelays = 0;
                }
            }
            beforeTime = System.nanoTime();

            /* If frame animation is taking too long, update the game state
               without rendering it, to get the updates/sec nearer to
               the required FPS. */
            int skips = 0;
            while((excess > period) && (skips < MAX_FRAME_SKIPS)) {
                excess -= period;
                gameUpdate();
                skips++;
            }
        }
        System.exit(0);
    } // end of run()

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
