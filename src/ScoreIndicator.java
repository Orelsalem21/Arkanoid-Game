import biuoop.DrawSurface;
import java.awt.Color;

public class ScoreIndicator implements Sprite {
    private Counter score;

    public ScoreIndicator(Counter score) {
        this.score = score;
    }

    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(Color.WHITE);
        d.drawText(350, 15, "Score: " + this.score.getValue(), 16);
    }

    @Override
    public void timePassed() {
        // nothing to do
    }
}
