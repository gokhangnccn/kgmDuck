import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener {
    private final Timer timer;
    private final ArrayList<CannonBall> cannonBalls;
    private Bird[] birds;
    private final Controls controls;
    private final int cannonX = 50;
    private final int cannonY = 500;
    private int score;
    private int level;
    private final int[] nextLevel;
    private final long startTime;
    private boolean isBigCannonBall;
    private String message;

    public GamePanel(Controls controls) {
        this.controls = controls;
        setPreferredSize(new Dimension(1200, 600));
        setBackground(Color.WHITE);
        cannonBalls = new ArrayList<>();
        birds = new Bird[1];
        birds[0] = new Bird();

        timer = new Timer(20, this);
        timer.start();

        startTime = System.currentTimeMillis();
        score = 0;

        level = 1;
        nextLevel = new int[]{100, 200, 300, 400, 500};
        isBigCannonBall = false;
        message = "";

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                shootCannonBall();
            }
        });

        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE) {
                    buyBigCannonBall();
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }

    private void shootCannonBall() {
        if (cannonBalls.size() < 3) {
            int angle = controls.getAngle();
            double speed = controls.getSpeed() / 5.5;
            double angleRad = Math.toRadians(angle);
            int cannonLength = 60;

            int cannonBallX = cannonX + (int) (cannonLength * Math.cos(angleRad));
            int cannonBallY = cannonY - (int) (cannonLength * Math.sin(angleRad));

            CannonBall cannonBall = new CannonBall(cannonBallX, cannonBallY, angle, speed, isBigCannonBall);
            cannonBalls.add(cannonBall);
        }
    }

    private void buyBigCannonBall() {
        if (score >= 100) {
            score -= 100;
            isBigCannonBall = true;
            message = "Topunuz Büyütüldü!";
        } else {
            message = "Yeterli puanınız yok!";
        }

        Timer messageTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message = "";
                ((Timer) e.getSource()).stop();
            }
        });
        messageTimer.setRepeats(false);
        messageTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCannon(g);
        for (CannonBall cannonBall : cannonBalls) {
            cannonBall.draw(g);
        }
        for (Bird bird : birds) {
            bird.draw(g);
        }
        drawScoreAndTime(g);
        drawMessage(g);
    }

    private void drawCannon(Graphics g) {
        int angle = controls.getAngle();
        Graphics2D g2d = (Graphics2D) g.create();

        // Çizim merkezini tankın konumuna göre ayarla
        g2d.translate(cannonX, cannonY);

        // Tankın namlusunu döndür
        g2d.rotate(Math.toRadians(-angle+90)); // Burada açı negatif değil, doğrudan kullanabilirsin

        // Namlunun çizimi
        int cannonWidth = 10;
        int cannonLength = 60;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(-cannonWidth / 2, -cannonLength, cannonWidth, cannonLength);

        // Namlunun ucuna bir çizgi ekle
        g2d.setColor(Color.RED);
        g2d.drawLine(0, -cannonLength, 0, -cannonLength - 10);

        // Çizim merkezini geri al
        g2d.rotate(Math.toRadians(angle-90));
        g2d.translate(-cannonX, -cannonY);

        // Tankın gövdesini çiz
        g2d.setColor(Color.GRAY);
        g2d.fillRect(cannonX - 20, cannonY - 20, 40, 40); // Tankın gövdesi için bir dikdörtgen

        g2d.dispose();
    }

    private void drawScoreAndTime(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 100, 50);
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        g.drawString("Time: " + elapsedTime + " s", 100, 70);
        g.drawString("Level: " + level, 100, 90);
    }

    private void drawMessage(Graphics g) {
        if (!message.isEmpty()) {
            g.setColor(Color.RED);
            g.drawString(message, 400, 50);
        }
    }

    private void levelUp() {
        if (level < 5 && score >= nextLevel[level - 1]) {
            level++;
            birds = new Bird[1];
            for (int i = 0; i < birds.length; i++) {
                birds[i] = new Bird();
                birds[i].increaseSpeed(level);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (CannonBall cannonBall : cannonBalls) {
            cannonBall.update();
        }
        cannonBalls.removeIf(CannonBall::isOutOfScreen);
        for (Bird bird : birds) {
            bird.update();
        }
        checkCollisions();
        levelUp();
        repaint();
    }

    private void checkCollisions() {
        for (CannonBall cannonBall : cannonBalls) {
            Rectangle cannonBallBounds = cannonBall.getBounds();
            for (Bird bird : birds) {
                Rectangle birdBounds = bird.getBounds();
                if (cannonBallBounds.intersects(birdBounds)) {
                    bird.respawn();
                    score += 50;
                    isBigCannonBall = false;
                    break;
                }
            }
        }
    }
}