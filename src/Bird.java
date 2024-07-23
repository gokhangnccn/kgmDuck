import javax.swing.*;
import java.awt.*;

public class Bird {
    private int x, y;
    private int speedX, speedY;
    private final int width = 50;
    private final int height = 50;
    private final Image birdImage;
    private boolean isMovingX, isMovingY;

    public Bird() {
        birdImage = new ImageIcon("bird.png").getImage();
        respawn();
    }

    public void update() {
        if (isMovingX) {
            x += speedX;
            if (x > 850) {
                x = 850;
                speedX = -speedX;
            } else if (x < 150) {
                x = 150;
                speedX = -speedX;
            }
        }
        if (isMovingY) {
            y += speedY;
            if (y > 450) {
                y = 450;
                speedY = -speedY;
            } else if (y < 100) {
                y = 100;
                speedY = -speedY;
            }
        }
    }

    public void draw(Graphics g) {
        g.drawImage(birdImage, x, y, width, height, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void respawn() {
        x = 150 + (int) (Math.random() * 700);
        y = 100 + (int) (Math.random() * 400);
        speedX = 2 + (int) (Math.random() * 3);
        speedY = 2 + (int) (Math.random() * 3);
        isMovingX = false;
        isMovingY = false;
    }


    public void increaseSpeed(int level) {
        if (level > 1) {
            isMovingX = true;
            speedX += level;
            if (level > 2) {
                isMovingY = true;
                speedY += level;
            }
        }
    }
}