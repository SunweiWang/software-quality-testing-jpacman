package nl.tudelft.jpacman.game;

import com.google.common.collect.ImmutableList;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.points.PointCalculator;

import java.util.List;

/**
 * The type Multi level game.
 */
public class MultiLevelGame extends Game {

    private Player player;

    private Level currentLevel;

    private List<Level> levelList;

    private int counter = 0;


    /**
     * Creating a new single player game like implementation.
     *
     * @param player          the player
     * @param levels          the level
     * @param pointCalculator The way to calculate points upon collisions.
     */
    public MultiLevelGame(Player player, List<Level> levels, PointCalculator pointCalculator) {
        super(pointCalculator);

        assert player != null;
        assert levels != null;

        this.player = player;
        this.levelList = levels;
        this.currentLevel = levelList.get(0);
        this.currentLevel.registerPlayer(player);
    }

    /**
     * Method which sets the next level and registers the player for the next level.
     */
    public void nextLevel() {
        this.currentLevel = levelList.get(counter);
        currentLevel.registerPlayer(player);
    }

    @Override
    public List<Player> getPlayers() {
        return ImmutableList.of(player);
    }

    @Override
    public Level getLevel() {
        return currentLevel;
    }

    @Override
    public void levelWon() {
        stop();
        if (counter < levelList.size() - 1) {
            counter++;
            nextLevel();
        }
    }
}
