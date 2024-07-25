import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener {
    private final Timer timer;
    private final ArrayList<CannonBall> cannonBalls;
    private Bird[] birds;
    private final Controls controls; // Controls referansı
    private final int cannonX = 50;
    private final int cannonY = 500;
    private int score;
    private final int finishScore;
    private int firstScore;
    private int hitScore;
    private int missScore;
    private int level;
    private final int[] nextLevel;
    private long startTime;
    private final Image backgroundImage;
    private final Image cannon;
    private final Image cannonWheel;

    private boolean completed;

    public GamePanel(Controls controls) {
        this.controls = controls; // Controls referansı
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
        finishScore = 1000;
        firstScore = 50;
        hitScore = 50;
        missScore = 10;

        level = 1;
        nextLevel = new int[]{200, 400, 600, 800, 1000};

        completed = false;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                shootCannonBall();
            }
        });



        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setFocusable(true);
                requestFocusInWindow();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    shootCannonBall();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    controls.incrementAngle();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    controls.decrementAngle();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    controls.incrementSpeed();
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    controls.decrementSpeed();
                }
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

            @Override
            public void focusLost(FocusEvent e) {
                setFocusable(true);
                requestFocusInWindow();
            }
        });
    }

    private void shootCannonBall() {
        if (cannonBalls.size() < 1) {
            int angle = controls.getAngle();
            double speed = controls.getSpeed() / 5.5;
            double angleRad = Math.toRadians(angle);
            int cannonLength = 120;

            int cannonBallX = cannonX + (int) (cannonLength * Math.cos(angleRad));
            int cannonBallY = cannonY - (int) (cannonLength * Math.sin(angleRad));

            CannonBall cannonBall = new CannonBall(cannonBallX, cannonBallY, angle, speed);
            cannonBalls.add(cannonBall);

            SoundUtils.playSound("shot-fire.wav");
        }
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
    }

    private void drawCannon(Graphics g) {
        int angle = controls.getAngle();
        Graphics2D g2d = (Graphics2D) g.create();

        int cannonHeight = cannon.getHeight(this);
        int cannonWheelWidth = cannonWheel.getWidth(this);
        int cannonWheelHeight = cannonWheel.getHeight(this);

        int cannonBaseX = cannonX - cannonWheelWidth / 2;
        int cannonBaseY = cannonY - cannonWheelHeight / 2;

        g2d.translate(cannonBaseX + 30, cannonBaseY + cannonWheelHeight - 30);
        g2d.rotate(Math.toRadians(-angle + 30));

        g2d.drawImage(cannon, -30, -cannonHeight + 30, this);

        g2d.rotate(Math.toRadians(angle));
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
        String nextText = "Sonraki Level: " + nextLevel[level - 1];

        int x = 10;
        int y = 30;
        g2d.drawString(scoreText, x, y);
        g2d.drawString(timeText, x, y + 20);
        g2d.drawString(levelText, x, y + 40);
        g2d.drawString(nextText, x, y + 60);

        g2d.drawString("Press the MOUSE1 button or the SPACE bar to shoot!", getWidth() / 2 - getWidth() / 4, getHeight() - 20);
    }

    private void levelUp() {
        if (level < nextLevel.length && score >= nextLevel[level - 1]) {
            level++;
            birds = new Bird[1];
            for (int i = 0; i < birds.length; i++) {
                birds[i] = new Bird();
                birds[i].increaseSpeed(level);
            }
        }
    }

    private void showCompletionDialog() {
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        String message = "Tebrikler bitirdiniz!\nSüreniz: " + elapsedTime + " saniye";

        int option = JOptionPane.showOptionDialog(
                this,
                message,
                "Oyun Bitti",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Yeniden Oyna", "Kapat"},
                "Yeniden Oyna"
        );

        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    private void showFailureDialog() {
        String message = "Maalesef 0 puana ulaştığınız için başarısız oldunuz..";

        int option = JOptionPane.showOptionDialog(
                this,
                message,
                "Başarısız",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Yeniden Oyna", "Kapat"},
                "Yeniden Oyna"
        );

        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    private void restartGame() {
        score = firstScore;
        level = 1;
        birds = new Bird[1];
        birds[0] = new Bird();
        cannonBalls.clear();
        startTime = System.currentTimeMillis();
        completed = false;
        timer.start();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<CannonBall> outOfBoundsBalls = new ArrayList<>();
        List<CannonBall> hitBalls = new ArrayList<>();

        for (CannonBall cannonBall : cannonBalls) {
            cannonBall.update();
            if (cannonBall.isOutOfScreen()) {
                outOfBoundsBalls.add(cannonBall);
                score -= missScore;
            }
        }

        cannonBalls.removeAll(outOfBoundsBalls);

        for (Bird bird : birds) {
            bird.update();
        }
        if (score >= finishScore && !completed) {
            completed = true;
            timer.stop();
            showCompletionDialog();
        }
        if (score <= 0) {
            showFailureDialog();
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
                    score += hitScore;
                    hitBalls.add(cannonBall);
                    break;
                } else if (cannonBallBounds.intersects(wallBounds)) {
                    cannonBall.bounceOffWall();
                }
            }
        }
    }
}