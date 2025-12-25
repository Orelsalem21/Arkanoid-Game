import biuoop.DrawSurface;
import biuoop.KeyboardSensor;

import java.awt.Color;

/**
 * The paddle is the player-controlled block.
 */
public class Paddle implements Sprite, Collidable {

    private static final int LEFT_BORDER = 20;
    private static final int RIGHT_BORDER = 780; // 800 - 20

    private Rectangle rectangle;
    private Color color;
    private KeyboardSensor keyboard;
    private int speed;

    /**
     * Create a new paddle.
     *
     * @param rectangle the paddle shape
     * @param color     the paddle color
     * @param keyboard  the keyboard sensor
     * @param speed     the paddle speed in pixels per frame
     */
    public Paddle(Rectangle rectangle, Color color,
                  KeyboardSensor keyboard, int speed) {
        this.rectangle = rectangle;
        this.color = color;
        this.keyboard = keyboard;
        this.speed = speed;
    }

    /**
     * Move the paddle left.
     */
    public void moveLeft() {
        double newX = this.rectangle.getUpperLeft().getX() - this.speed;
        if (newX < LEFT_BORDER) {
            newX = LEFT_BORDER;
        }
        this.rectangle = new Rectangle(
                new Point(newX, this.rectangle.getUpperLeft().getY()),
                this.rectangle.getWidth(),
                this.rectangle.getHeight());
    }

    /**
     * Move the paddle right.
     */
    public void moveRight() {
        double newX = this.rectangle.getUpperLeft().getX() + this.speed;
        double maxX = RIGHT_BORDER - this.rectangle.getWidth();
        if (newX > maxX) {
            newX = maxX;
        }
        this.rectangle = new Rectangle(
                new Point(newX, this.rectangle.getUpperLeft().getY()),
                this.rectangle.getWidth(),
                this.rectangle.getHeight());
    }

    // Sprite

    @Override
    public void timePassed() {
        if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            moveLeft();
        }
        if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            moveRight();
        }
    }

    @Override
    public void drawOn(DrawSurface d) {
        int x = (int) this.rectangle.getUpperLeft().getX();
        int y = (int) this.rectangle.getUpperLeft().getY();
        int w = (int) this.rectangle.getWidth();
        int h = (int) this.rectangle.getHeight();

        d.setColor(this.color);
        d.fillRectangle(x, y, w, h);
    }

    // Collidable

    @Override
    public Rectangle getCollisionRectangle() {
        return this.rectangle;
    }

    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        double xLeft = rectangle.getUpperLeft().getX();
        double yTop = rectangle.getUpperLeft().getY();
        double xRight = xLeft + rectangle.getWidth();
        double yBottom = yTop + rectangle.getHeight();

        double px = collisionPoint.getX();
        double py = collisionPoint.getY();

        double dx = currentVelocity.getDx();
        double dy = currentVelocity.getDy();

        double epsilon = 0.0001;

        // --- TOP HIT (including corners) ---
        if (py <= yTop + epsilon && px >= xLeft - epsilon && px <= xRight + epsilon) {
            double speed = Math.sqrt(dx * dx + dy * dy);
            double regionWidth = rectangle.getWidth() / 5.0;
            double hitPosition = px - xLeft;
            int region = (int) (hitPosition / regionWidth) + 1;

            if (region < 1) {
                region = 1;
            }
            if (region > 5) {
                region = 5;
            }

            double angle;
            switch (region) {
                case 1:
                    angle = 300; // -60
                    break;
                case 2:
                    angle = 330; // -30
                    break;
                case 3:
                    angle = 0;
                    break;
                case 4:
                    angle = 30;
                    break;
                case 5:
                default:
                    angle = 60;
                    break;
            }
            return Velocity.fromAngleAndSpeed(angle, speed);
        }

        // --- SIDE HIT ---
        if (px <= xLeft + epsilon || px >= xRight - epsilon) {
            dx = -dx;
        }

        // --- BOTTOM HIT (rare, but safe) ---
        if (py >= yBottom - epsilon) {
            dy = -dy;
        }

        return new Velocity(dx, dy);
    }

    /**
     * Add this paddle to the game.
     *
     * @param g the game
     */
    public void addToGame(Game g) {
        g.addSprite(this);
        g.addCollidable(this);
    }
}
