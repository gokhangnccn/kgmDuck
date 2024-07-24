import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
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

    private final Image backgroundImage;
    private final Image cannon;
    private final Image cannonWheel;

    public GamePanel(Controls controls) {
        this.controls = controls;
        setPreferredSize(new Dimension(1200, 600));
        setBackground(Color.WHITE);
        cannonBalls = new ArrayList<>();
        birds = new Bird[1];
        birds[0] = new Bird();

        backgroundImage = new ImageIcon("background.png").getImage();
        cannon = new ImageIcon("cannon.png").getImage();
        cannonWheel = new ImageIcon("cannon-wheel.png").getImage();

        timer = new Timer(20, this);
        timer.start();

        startTime = System.currentTimeMillis();
        score = 50;

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

        setFocusable(true);
        requestFocusInWindow();

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setFocusable(true);
                requestFocusInWindow();
            }
        });

        addKeyListeners();
    }

    private void addKeyListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    buyBigCannonBall();
                }
            }
        });
    }

    private void shootCannonBall() {
        if (cannonBalls.size() < 20) {
            int angle = controls.getAngle();
            double speed = controls.getSpeed() / 5.5;
            double angleRad = Math.toRadians(angle);
            int cannonLength = 60;

            int cannonBallX = cannonX + (int) (cannonLength * Math.cos(angleRad));
            int cannonBallY = cannonY - (int) (cannonLength * Math.sin(angleRad));

            CannonBall cannonBall = new CannonBall(cannonBallX, cannonBallY, angle, speed, isBigCannonBall);
            cannonBalls.add(cannonBall);

            SoundUtils.playSound("shot-fire.wav");
            isBigCannonBall = false; // Reset big cannonball flag after shooting
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

        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

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

        // Topun ve tekerleğin boyutlarını al
        int cannonWidth = cannon.getWidth(this);
        int cannonHeight = cannon.getHeight(this);
        int cannonWheelWidth = cannonWheel.getWidth(this);
        int cannonWheelHeight = cannonWheel.getHeight(this);

        // Çizim merkezini topun sol alt köşesi olarak belirle
        int cannonBaseX = cannonX - cannonWheelWidth / 2;
        int cannonBaseY = cannonY - cannonWheelHeight / 2;

        // Tekerlek ve topun döndürme merkezini belirle
        g2d.translate(cannonBaseX + 30, cannonBaseY + cannonWheelHeight - 30);
        g2d.rotate(Math.toRadians(-angle + 30)); // Topun döndürme açısını belirle

        // Topun kendisini çiz
        g2d.drawImage(cannon, -30, -cannonHeight + 30, this);

        // Çizim merkezini geri al ve tekerleği çiz
        g2d.rotate(Math.toRadians(angle)); // Döndürmeyi geri al
        g2d.drawImage(cannonWheel, 0, -10, this);

        g2d.dispose();
    }

    private void drawScoreAndTime(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Font font = new Font("Monospaced", Font.BOLD, 18);
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);

        String scoreText = "Score: " + score;
        String timeText = "Time: " + (System.currentTimeMillis() - startTime) / 1000 + "s";
        String levelText = "Level: " + level;

        int x = 10;
        int y = 30;
        g2d.drawString(scoreText, x, y);
        g2d.drawString(timeText, x, y + 20);
        g2d.drawString(levelText, x, y + 40);
    }


    private void drawMessage(Graphics g) {
        if (!message.isEmpty()) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.setColor(Color.RED);
            g2d.drawString(message, 400, 50);
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

            isBigCannonBall = false;
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();
        addKeyListeners();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<CannonBall> outOfBoundsBalls = new ArrayList<>();
        List<CannonBall> hitBalls = new ArrayList<>(); //

        for (CannonBall cannonBall : cannonBalls) {
            cannonBall.update();
            if (cannonBall.isOutOfScreen()) {
                outOfBoundsBalls.add(cannonBall);
                score -= 5; // Top ekran dışına çıktığında 5 puan eksilir
            }
        }

        cannonBalls.removeAll(outOfBoundsBalls);

        for (Bird bird : birds) {
            bird.update();
        }

        updateUI();
        checkCollisions(hitBalls);
        cannonBalls.removeAll(hitBalls);
        levelUp();
        repaint();
    }

    private void checkCollisions(List<CannonBall> hitBalls) {
        for (CannonBall cannonBall : cannonBalls) {
            Rectangle cannonBallBounds = cannonBall.getBounds();
            for (Bird bird : birds) {
                Rectangle birdBounds = bird.getBounds();
                Rectangle wallBounds = bird.getWallBounds();
                if (cannonBallBounds.intersects(birdBounds)) {
                    bird.respawn();
                    score += 15;
                    hitBalls.add(cannonBall);
                    isBigCannonBall = false;
                    break;
                } else if (cannonBallBounds.intersects(wallBounds)) {
                    cannonBall.bounceOffWall();
                }
            }
        }
    }

}