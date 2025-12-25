/**
 * A BlockRemover is in charge of removing blocks from the game,
 * as well as keeping count of the number of blocks that remain.
 */
public class BlockRemover implements HitListener {

    private Game game;
    private Counter remainingBlocks;

    /**
     * Create a new BlockRemover.
     *
     * @param game the game instance to remove blocks from
     * @param remainingBlocks counter for remaining blocks
     */
    public BlockRemover(Game game, Counter remainingBlocks) {
        this.game = game;
        this.remainingBlocks = remainingBlocks;
    }

    /**
     * Blocks that are hit should be removed from the game.
     * Remember to remove this listener from the block that is being removed.
     *
     * @param beingHit the block that was hit
     * @param hitter the ball that hit the block
     */
    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        beingHit.removeHitListener(this);
        beingHit.removeFromGame(this.game);
        this.remainingBlocks.decrease(1);
    }
}
