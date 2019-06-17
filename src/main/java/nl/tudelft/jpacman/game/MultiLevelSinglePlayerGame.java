package nl.tudelft.jpacman.game;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import com.google.common.collect.ImmutableList;
import nl.tudelft.jpacman.points.PointCalculator;

/**
 * This is an extension of the game supporting multiple
 * levels, for a single player.
 */
public class MultiLevelSinglePlayerGame extends Game {
    private Player player;
    private LinkedList<Level> levels;
    private int moveCount;

    /**
     * Create a new multi-level game.
     * @param player The player of the game.
     * @param pointCalculator The player of the game.
     */
    public MultiLevelSinglePlayerGame(Player player, PointCalculator pointCalculator) {
        super(pointCalculator);
        this.player = player;
        levels = new LinkedList<Level>();
        moveCount = 0;
    }

    @Override
    public List<Player> getPlayers() {
        return ImmutableList.of(player);
    }
    @Override
    public Level getLevel() {
        assert !levels.isEmpty();
        return levels.getFirst();
    }
    @Override
    public void start() {
        super.start();
        if (readyForNextLevel()) {
            nextLevel();
        }
    }
    /**
     * Let the player proceed to the next level.
     */
    public void nextLevel() {
        assert levels.size() > 1;
        levels.removeFirst();
        moveCount = 0;
        getLevel().registerPlayer(player);
    }
    private boolean readyForNextLevel() {
        return player.isAlive()
            && getLevel().remainingPellets() == 0
            && levels.size() > 1;
    }
    /**
     * @return The number of levels still to be played.
     */
    public int levelsLeft() {
        return levels.size() - 1;
    }
    /**
     * Register another level.
     * @param level The extra level to play.
     */
    public void addLevel(Level level) {
        levels.addLast(level);
    }
    /**
     * @return Number of moves made so far.
     */
    public int getMoveCount() {
        return moveCount;
    }
    /**
    * A somewhat naive way of counting any attempt to move,
    * not just the successful moves.
    *
    */
    @Override
    public void move(Player player, Direction direction) {
        super.move(player, direction);
        if (isInProgress()) {
            moveCount++;
        }
    }
}
