public class BallRemover implements HitListener {
    private Game game;
    private Counter remainingBalls;

    public BallRemover(Game game, Counter remainingBalls) {
        this.game = game;
        this.remainingBalls = remainingBalls;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        // remove the ball from the game
        hitter.removeFromGame(this.game);

        // update counter
        this.remainingBalls.decrease(1);
    }
}
