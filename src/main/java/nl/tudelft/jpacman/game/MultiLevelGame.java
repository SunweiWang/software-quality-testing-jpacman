package nl.tudelft.jpacman.game;

import com.google.common.collect.ImmutableList;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.points.PointCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Game With multiple levels.
 */
public class MultiLevelGame extends Game {


    private List<Level> levels;

    private int currentLevel = 0;

    private final Player player;


    /**
     * Creates a new game.
     *
     * @param player          - The player.
     * @param levels          -> The list of levels
     * @param pointCalculator The way to calculate points upon collisions.
     */
    public MultiLevelGame(Player player, List<Level> levels, PointCalculator pointCalculator) {


        super(pointCalculator);
        this.player = player;

        this.levels = new ArrayList<>(levels);

        this.levels.get(0).registerPlayer(player);

    }


    @Override
    public List<Player> getPlayers() {

        return ImmutableList.of(player);
    }

    @Override
    public Level getLevel() {

        return this.levels.get(currentLevel);
    }

    @Override
    public void levelWon() {

        stop();

        if (this.currentLevel < this.levels.size() - 1) {

            this.currentLevel += 1;

            this.levels.get(this.currentLevel).registerPlayer(player);
            start();

        }

    }

    /**
     * Shows the current played level.
     * @return int
     */
    public int getPlayerLevel() {

        return this.currentLevel + 1;
    }
}
