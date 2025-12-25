import java.util.ArrayList;
import java.util.List;

public class GameEnvironment {

    private List<Collidable> collidables;

    /**
     * Create a new empty game environment.
     */
    public GameEnvironment() {
        this.collidables = new ArrayList<Collidable>();
    }

    /**
     * Add the given collidable to the environment.
     *
     * @param c the collidable object to add
     */
    public void addCollidable(Collidable c) {
        this.collidables.add(c);
    }

    /**
     * Remove a collidable from the environment.
     *
     * @param c the collidable to remove
     */
    public void removeCollidable(Collidable c) {
        this.collidables.remove(c);
    }

    /**
     * Assume an object moving from line.start() to line.end().
     * If this object will not collide with any of the collidables
     * in this collection, return null.
     * Else, return the information about the closest collision
     * that is going to occur.
     *
     * @param trajectory the line representing the object's path
     * @return CollisionInfo of the closest collision, or null if none
     */
    public CollisionInfo getClosestCollision(Line trajectory) {

        Point closestPoint = null;
        Collidable closestObject = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Collidable c : this.collidables) {
            Rectangle rect = c.getCollisionRectangle();
            Point intersection = trajectory.closestIntersectionToStartOfLine(rect);

            // If there is no intersection but we START exactly on the rectangle border,
            // treat it as a collision (fixes "escape" through borders).
            if (intersection == null && isPointOnRectangleBorder(trajectory.start(), rect)) {
                intersection = trajectory.start();
            }

            if (intersection != null) {
                double distance = trajectory.start().distance(intersection);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPoint = intersection;
                    closestObject = c;
                }
            }
        }

        if (closestPoint == null) {
            return null;
        }

        return new CollisionInfo(closestPoint, closestObject);
    }

    // ---- helpers ----

    private boolean isPointOnRectangleBorder(Point p, Rectangle rect) {
        final double eps = 1e-6;

        double xLeft = rect.getUpperLeft().getX();
        double yTop = rect.getUpperLeft().getY();
        double xRight = xLeft + rect.getWidth();
        double yBottom = yTop + rect.getHeight();

        double x = p.getX();
        double y = p.getY();

        boolean withinX = (x >= xLeft - eps) && (x <= xRight + eps);
        boolean withinY = (y >= yTop - eps) && (y <= yBottom + eps);

        boolean onLeft = withinY && almostEqual(x, xLeft, eps);
        boolean onRight = withinY && almostEqual(x, xRight, eps);
        boolean onTop = withinX && almostEqual(y, yTop, eps);
        boolean onBottom = withinX && almostEqual(y, yBottom, eps);

        return onLeft || onRight || onTop || onBottom;
    }

    private boolean almostEqual(double a, double b, double eps) {
        return Math.abs(a - b) <= eps;
    }
}
