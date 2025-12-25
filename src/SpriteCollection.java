import biuoop.DrawSurface;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of sprites that can be drawn and updated.
 */
public class SpriteCollection {

    private List<Sprite> sprites;

    /**
     * Create an empty sprite collection.
     */
    public SpriteCollection() {
        this.sprites = new ArrayList<Sprite>();
    }

    /**
     * Add a sprite to the collection.
     *
     * @param s the sprite to add
     */
    public void addSprite(Sprite s) {
        this.sprites.add(s);
    }

    /**
     * Remove a sprite from the collection.
     *
     * @param s the sprite to remove
     */
    public void removeSprite(Sprite s) {
        this.sprites.remove(s);
    }

    /**
     * Call timePassed() on all sprites.
     */
    public void notifyAllTimePassed() {
        List<Sprite> spritesCopy = new ArrayList<Sprite>(this.sprites);
        for (Sprite s : spritesCopy) {
            s.timePassed();
        }
    }

    /**
     * Call drawOn(d) on all sprites.
     *
     * @param d the DrawSurface to draw on
     */
    public void drawAllOn(DrawSurface d) {
        List<Sprite> spritesCopy = new ArrayList<Sprite>(this.sprites);
        for (Sprite s : spritesCopy) {
            s.drawOn(d);
        }
    }
}
