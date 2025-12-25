import biuoop.DrawSurface;

public interface Sprite {

    /**
     * Draw the sprite to the given DrawSurface.
     *
     * @param d the surface to draw on
     */
    void drawOn(DrawSurface d);

    /**
     * Notify the sprite that time has passed.
     */
    void timePassed();
}
