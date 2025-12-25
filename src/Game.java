import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;
import biuoop.KeyboardSensor;

import java.awt.Color;

/**
 * The main game class: holds sprites and collidables and runs the animation.
 */
public class Game {

    private SpriteCollection sprites;
    private GameEnvironment environment;
    private GUI gui;
    private Counter score;

    // [ASS3 - Part 1] Count remaining blocks in the game (non-border blocks)
    private Counter remainingBlocks;

    // [ASS3 - Part 2] Count remaining balls in the game
    private Counter remainingBalls;

    /**
     * Create a new empty game.
     */
    public Game() {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.remainingBlocks = new Counter();
        this.remainingBalls = new Counter(); // Part 2
        this.score = new Counter();
    }

    /**
     * Add a collidable to the game environment.
     *
     * @param c the collidable to add
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * Add a sprite to the game.
     *
     * @param s the sprite to add
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }

    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

    /**
     * Initialize a new game: create the GUI, blocks, balls and paddle,
     * and add them to the game.
     */
    public void initialize() {
        // Create GUI 800x600
        this.gui = new GUI("Arkanoid Game", 800, 600);
        KeyboardSensor keyboard = this.gui.getKeyboardSensor();

        // [ASS3 - Part 1] Block remover + counter
        BlockRemover blockRemover = new BlockRemover(this, this.remainingBlocks);

        // [ASS3 - Part 2] Ball remover + counter
        BallRemover ballRemover = new BallRemover(this, this.remainingBalls);

        // [ASS3 - Part 3] Score listener
        ScoreTrackingListener scoreListener = new ScoreTrackingListener(this.score);

        // [ASS3] Printing listener (simple test)
        PrintingHitListener printer = new PrintingHitListener();

        // --- Create Borders ---
        int width = 800;
        int height = 600;
        int borderSize = 20;

        // Top border
        Block top = new Block(
                new Rectangle(new Point(0, 0), width, borderSize),
                Color.GRAY);

        // Left border
        Block left = new Block(
                new Rectangle(new Point(0, 0), borderSize, height),
                Color.GRAY);

        // Right border
        Block right = new Block(
                new Rectangle(new Point(width - borderSize, 0), borderSize, height),
                Color.GRAY);

        // Add borders to the game (no listeners)
        addBlock(top);
        addBlock(left);
        addBlock(right);

        // --- Death Region (Part 2) ---
        Block deathRegion = new Block(
                new Rectangle(new Point(0, height), width, borderSize),
                new Color(0, 0, 0, 0)
        );
        deathRegion.addHitListener(ballRemover);
        this.addCollidable(deathRegion);

        // --- Create Game Blocks ---
        Color[] colors = {Color.GRAY, Color.RED, Color.YELLOW, Color.BLUE, Color.PINK, Color.GREEN};
        int blockWidth = 50;
        int blockHeight = 25;

        for (int i = 0; i < 6; i++) {
            Color rowColor = colors[i];

            for (int j = 0; j < 12 - i; j++) {
                double x = (width - borderSize) - (j + 1) * blockWidth;
                double y = 100 + i * blockHeight;

                Rectangle rect = new Rectangle(new Point(x, y), blockWidth, blockHeight);
                Block block = new Block(rect, rowColor);

                // Register listeners
                block.addHitListener(blockRemover);
                block.addHitListener(printer);
                block.addHitListener(scoreListener); // Part 3: +5 points per hit

                this.remainingBlocks.increase(1);
                addBlock(block);
            }
        }

        // --- Create Balls (3 balls) ---
        Ball ball1 = new Ball(new Point(400, 300), 5, Color.WHITE);
        ball1.setVelocity(4, 4);
        ball1.setGameEnvironment(this.environment);
        this.addSprite(ball1);
        this.remainingBalls.increase(1);

        Ball ball2 = new Ball(new Point(300, 300), 5, Color.WHITE);
        ball2.setVelocity(-4, 4);
        ball2.setGameEnvironment(this.environment);
        this.addSprite(ball2);
        this.remainingBalls.increase(1);

        Ball ball3 = new Ball(new Point(500, 300), 5, Color.WHITE);
        ball3.setVelocity(3, -4);
        ball3.setGameEnvironment(this.environment);
        this.addSprite(ball3);
        this.remainingBalls.increase(1);

        // --- Create Paddle ---
        Rectangle paddleRect = new Rectangle(new Point(350, 560), 100, 20);
        Paddle paddle = new Paddle(paddleRect, Color.ORANGE, keyboard, 8);
        paddle.addToGame(this);

        // [ASS3 - Part 3] score display (add LAST so it draws on top)
        ScoreIndicator scoreIndicator = new ScoreIndicator(this.score);
        this.addSprite(scoreIndicator);
    }

    /**
     * Helper: add a block as both sprite and collidable.
     *
     * @param block the block to add
     */
    private void addBlock(Block block) {
        this.addCollidable(block);
        this.addSprite(block);
    }

    /**
     * Run the game -- start the animation loop.
     */
    public void run() {
        Sleeper sleeper = new Sleeper();
        int framesPerSecond = 60;
        int millisecondsPerFrame = 1000 / framesPerSecond;

        while (true) {
            long startTime = System.currentTimeMillis(); // timing

            DrawSurface d = this.gui.getDrawSurface();

            d.setColor(new Color(0, 0, 100));
            d.fillRectangle(0, 0, 800, 600);

            this.sprites.drawAllOn(d);
            this.gui.show(d);
            this.sprites.notifyAllTimePassed();

            // Exit when no more blocks OR no more balls (Part 1 + Part 2)
            if (this.remainingBlocks.getValue() == 0 || this.remainingBalls.getValue() == 0) {

                // Part 3: bonus for clearing all blocks
                if (this.remainingBlocks.getValue() == 0) {
                    this.score.increase(100);
                }

                if (this.gui != null) {
                    this.gui.close();
                }
                return;
            }

            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
            }
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.initialize();
        game.run();

    }
}
