/**
 * Objects that want to be notified of hit events should implement this interface.
 */
public interface HitListener {

    /**
     * This method is called whenever the beingHit object is hit.
     * The hitter parameter is the Ball that's doing the hitting.
     *
     * @param beingHit the block that was hit
     * @param hitter the ball that hit the block
     */
    void hitEvent(Block beingHit, Ball hitter);
}
