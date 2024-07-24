import javax.swing.*;
import java.awt.*;

public class Bird {
    private int x, y;
    private int speedX, speedY;
    private final int width = 50;
    private final int height = 50;
    private final Image birdImage;
    private boolean isMovingX, isMovingY;
    private boolean drawWall;
    private Rectangle wall;


    public Bird() {
        birdImage = new ImageIcon("bird.png").getImage();
        respawn();
        wall = new Rectangle(0, 0, 0, 0);
    }

    public void update() {
        if (isMovingX) {
            x += speedX;
            if (x > 950) {
                x = 950;
                speedX = -speedX;
            } else if (x < 250) {
                x = 250;
                speedX = -speedX;
            }
        }
        if (isMovingY) {
            y += speedY;
            if (y > 500) {
                y = 500;
                if (y > 400) {
                    y = 400;
                    speedY = -speedY;
                } else if (y < 100) {
                    y = 100;
                    speedY = -speedY;
                }
            }
        }
    }

    public void draw(Graphics g) {
        g.drawImage(birdImage, x, y, width, height, null);
        if (drawWall) {
            g.setColor(Color.RED);
            g.fillRect(wall.x, wall.y, wall.width, wall.height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Rectangle getWallBounds() {
        return wall;
    }

    public void respawn() {
        SoundUtils.playSound("hitting-bird.wav");
        x = 250 + (int) (Math.random() * 700);
        y = 100 + (int) (Math.random() * 400);
        speedX = 2 + (int) (Math.random() * 3);
        speedY = 2 + (int) (Math.random() * 3);

        if (drawWall) {
            wall.setBounds(x - 20 - ((int)(Math.random()*40)), y - ((int)(Math.random()*40)), 8, 80);
        }
    }

    public void increaseSpeed(int level) {
        drawWall = false;
        switch (level) {
            case 5:
                isMovingX = false;
                isMovingY = false;
                drawWall = true;
                wall.setBounds(x - 20 - ((int)(Math.random()*40)), y - ((int)(Math.random()*40)), 8, 80);
                break;
            case 4:
                isMovingX = true;
                isMovingY = true;
                speedX += level;
                speedY += level;
                break;
            case 3:
                isMovingX = true;
                isMovingY = true;
                speedX += level;
                break;
            case 2:
                isMovingX = true;
                break;
            default:
                break;
        }
    }
}
