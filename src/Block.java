import biuoop.DrawSurface;
import java.awt.Color;

/**
 * A block is a collidable and a sprite. It can notify listeners when it is hit.
 */
public class Block implements Collidable, Sprite, HitNotifier {

    private java.util.List<HitListener> hitListeners;

    private Rectangle rectangle;
    private Color color;

    /**
     * Create a new block using the given rectangle.
     * The block will use a default color.
     *
     * @param rectangle the rectangle that defines the block shape
     */
    public Block(Rectangle rectangle) {
        this(rectangle, Color.GRAY);
    }

    /**
     * Create a new block using the given rectangle and color.
     *
     * @param rectangle the rectangle that defines the block shape
     * @param color     the color of the block
     */
    public Block(Rectangle rectangle, Color color) {
        this.rectangle = rectangle;
        this.color = color;
        this.hitListeners = new java.util.ArrayList<HitListener>();
    }

    /**
     * Return the collision rectangle of the block.
     *
     * @return the rectangle of this block
     */
    @Override
    public Rectangle getCollisionRectangle() {
        return this.rectangle;
    }

    /**
     * Calculate the new velocity after hitting this block.
     *
     * @param hitter          the ball that hit this block
     * @param collisionPoint  the point where the collision occurs
     * @param currentVelocity the velocity before the hit
     * @return a new velocity after the collision
     */
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {

        double newDx = currentVelocity.getDx();
        double newDy = currentVelocity.getDy();

        double xLeft = rectangle.getUpperLeft().getX();
        double yTop = rectangle.getUpperLeft().getY();
        double xRight = xLeft + rectangle.getWidth();
        double yBottom = yTop + rectangle.getHeight();

        double px = collisionPoint.getX();
        double py = collisionPoint.getY();

        // IMPORTANT: collisionPoint is double, so use epsilon comparisons (not ==)
        double eps = 1e-6;

        boolean hitLeft = Math.abs(px - xLeft) <= eps;
        boolean hitRight = Math.abs(px - xRight) <= eps;
        boolean hitTop = Math.abs(py - yTop) <= eps;
        boolean hitBottom = Math.abs(py - yBottom) <= eps;

        if (hitLeft || hitRight) {
            newDx = -newDx;
        }
        if (hitTop || hitBottom) {
            newDy = -newDy;
        }

        // Notify listeners about the hit
        this.notifyHit(hitter);

        return new Velocity(newDx, newDy);
    }

    /**
     * Draw the block on the given DrawSurface.
     *
     * @param d the surface to draw on
     */
    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(this.color);
        Point upperLeft = this.rectangle.getUpperLeft();
        int x = (int) upperLeft.getX();
        int y = (int) upperLeft.getY();
        int width = (int) this.rectangle.getWidth();
        int height = (int) this.rectangle.getHeight();
        d.fillRectangle(x, y, width, height);

        d.setColor(Color.BLACK);
        d.drawRectangle(x, y, width, height);
    }

    /**
     * Notify the block that time has passed.
     * Currently, blocks do not change over time.
     */
    @Override
    public void timePassed() {
        // no behavior for now
    }

    @Override
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    @Override
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    /**
     * Notify all listeners about a hit event.
     *
     * @param hitter the ball that hit this block
     */
    private void notifyHit(Ball hitter) {
        java.util.List<HitListener> listeners =
                new java.util.ArrayList<HitListener>(this.hitListeners);
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }

    /**
     * Add this block to the given game.
     *
     * @param game the game to add this block to
     */
    public void addToGame(Game game) {
        game.addSprite(this);
        game.addCollidable(this);
    }

    /**
     * Remove this block from the given game.
     *
     * @param game the game to remove this block from
     */
    public void removeFromGame(Game game) {
        game.removeSprite(this);
        game.removeCollidable(this);
    }
}
