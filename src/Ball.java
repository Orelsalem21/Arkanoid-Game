import biuoop.DrawSurface;
import java.awt.Color;

public class Ball implements Sprite {
    // data members
    private Point center;
    private int r;
    private Color color;
    private Velocity velocity;
    private GameEnvironment gameEnvironment;

    // constructor
    public Ball(Point center, int r, Color color) {
        this.center = center;
        this.r = r;
        this.color = color;
        this.velocity = new Velocity(0, 0);
    }

    public Ball(int x, int y, int r, Color color) {
        this.center = new Point(x, y);
        this.r = r;
        this.color = color;
        this.velocity = new Velocity(0, 0);
    }

    public void setGameEnvironment(GameEnvironment gameEnvironment) {
        this.gameEnvironment = gameEnvironment;
    }

    // existing int getters (keep as-is)
    public int getX() {
        return (int) this.center.getX();
    }

    public int getY() {
        return (int) this.center.getY();
    }

    // NEW: precise getters for debugging / accurate trajectory building outside Ball
    public double getXDouble() {
        return this.center.getX();
    }

    public double getYDouble() {
        return this.center.getY();
    }

    public int getSize() {
        return this.r;
    }

    public Color getColor() {
        return this.color;
    }

    public void setVelocity(Velocity v) {
        this.velocity = v;
    }

    public void setVelocity(double dx, double dy) {
        this.velocity = new Velocity(dx, dy);
    }

    public Velocity getVelocity() {
        return this.velocity;
    }

    @Override
    public void timePassed() {
        this.moveOneStep();
    }

    /**
     * Move the ball one step according to the game environment.
     */
    public void moveOneStep() {
        if (this.velocity == null) {
            return;
        }

        double dx = this.velocity.getDx();
        double dy = this.velocity.getDy();
        double speed = Math.sqrt(dx * dx + dy * dy);
        if (speed == 0) {
            return;
        }

        Point currentCenter = this.center;
        Point nextCenter = this.velocity.applyToPoint(currentCenter);

        // unit direction
        double ux = dx / speed;
        double uy = dy / speed;

        // lead point on circle edge
        Point edgeStart = new Point(
                currentCenter.getX() + ux * this.r,
                currentCenter.getY() + uy * this.r
        );

        // extend enough to avoid corner misses (move length + extra margin)
        double extend = speed + this.r + 0.5;
        Point edgeEnd = new Point(
                edgeStart.getX() + ux * extend,
                edgeStart.getY() + uy * extend
        );

        Line trajectory = new Line(edgeStart, edgeEnd);

        if (this.gameEnvironment == null) {
            this.center = nextCenter;
            return;
        }

        CollisionInfo info = this.gameEnvironment.getClosestCollision(trajectory);
        if (info == null) {
            this.center = nextCenter;
            return;
        }

        Point cp = info.collisionPoint();
        Rectangle rect = info.collisionObject().getCollisionRectangle();

        double epsilon = 0.01;

        double rectLeft = rect.getUpperLeft().getX();
        double rectRight = rectLeft + rect.getWidth();
        double rectTop = rect.getUpperLeft().getY();
        double rectBottom = rectTop + rect.getHeight();

        // choose closest side (robust for corners)
        double distLeft = Math.abs(cp.getX() - rectLeft);
        double distRight = Math.abs(cp.getX() - rectRight);
        double distTop = Math.abs(cp.getY() - rectTop);
        double distBottom = Math.abs(cp.getY() - rectBottom);

        double min = distLeft;
        String side = "LEFT";
        if (distRight < min) { min = distRight; side = "RIGHT"; }
        if (distTop < min) { min = distTop; side = "TOP"; }
        if (distBottom < min) { min = distBottom; side = "BOTTOM"; }

        double newX = this.center.getX();
        double newY = this.center.getY();

        if (side.equals("TOP")) {
            newY = rectTop - this.r - epsilon;
        } else if (side.equals("BOTTOM")) {
            newY = rectBottom + this.r + epsilon;
        } else if (side.equals("LEFT")) {
            newX = rectLeft - this.r - epsilon;
        } else { // RIGHT
            newX = rectRight + this.r + epsilon;
        }

        this.center = new Point(newX, newY);
        this.velocity = info.collisionObject().hit(this, cp, this.velocity);
    }
    public void removeFromGame(Game game) {
        game.removeSprite(this);
    }

    @Override
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillCircle(this.getX(), this.getY(), this.r);
    }
}
