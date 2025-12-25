public class CollisionInfo {

    private Point collisionPoint;
    private Collidable collisionObject;

    /**
     * Create a new CollisionInfo object.
     *
     * @param collisionPoint  the point at which the collision occurs
     * @param collisionObject the collidable object involved in the collision
     */
    public CollisionInfo(Point collisionPoint, Collidable collisionObject) {
        this.collisionPoint = collisionPoint;
        this.collisionObject = collisionObject;
    }

    /**
     * Return the point at which the collision occurs.
     *
     * @return the collision point
     */
    public Point collisionPoint() {
        return this.collisionPoint;
    }

    /**
     * Return the collidable object involved in the collision.
     *
     * @return the collidable object
     */
    public Collidable collisionObject() {
        return this.collisionObject;
    }
}
