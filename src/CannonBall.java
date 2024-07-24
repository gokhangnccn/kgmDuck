import java.awt.*;

public class CannonBall {
    private double x, y;
    private double vx, vy;
    private final double gravity = 9.8;
    private final double friction = 0.995;
    private boolean isBig;

    public CannonBall(double startX, double startY, double angle, double speed, boolean isBig) {
        this.x = startX;
        this.y = startY;
        this.vx = speed * Math.cos(Math.toRadians(angle));
        this.vy = speed * Math.sin(Math.toRadians(angle));
        this.isBig = isBig;
    }

    public void update() {
        x += vx;
        y -= vy;
        vy -= gravity * 0.02;
        vx *= friction;
        vy *= friction;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        int size = isBig ? 15 : 10;
        g.fillOval((int) x, (int) y, size, size);
    }

    public Rectangle getBounds() {
        int size = isBig ? 15 : 10;
        return new Rectangle((int) x, (int) y, size, size);
    }

    public boolean isOutOfScreen() {
        return x < 0 || x > 1200 || y > 600;
    }

    public void bounceOffWall() {
        vx = -vx;
    }

}

































