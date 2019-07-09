package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.MultiLevelLauncher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * Test suit for the MultiLevelGameTest.
 */
public class MultiLevelGameTest extends AbstractGameStateTest {

    /**
     * Returns the number of available levels.
     *
     * @return int
     */
    @SuppressWarnings("checkstyle:magicnumber")
    protected int getNumberOfLevels() {

        return 3;
    }

    /**
     * Set the MultiLevelLauncher to be the used level.
     *
     * @return MultiLevelLauncher
     */
    @Override
    public Launcher initLauncher() {

        return new MultiLevelLauncher();
    }


    /**
     * Go to pellet and win the level test.
     * This test should be overridden, since with the MultiLevelGame, the game
     * doesn't end anymore when the level is won
     */
    @Test
    @Override
    void goToPelletAndWin() {

        getLauncher().withMapFile("/GameTest.txt").launch();

        Level.LevelObserver winListener = mock(Level.LevelObserver.class);
        this.getGame().getLevel().addObserver(winListener);

        this.getGame().start();
        this.getGame().move(getPlayer(), Direction.WEST);
        this.getGame().move(getPlayer(), Direction.WEST);

        // Assert that the level was won, and that we have moved to the next level (level 2)
        verify(winListener, times(1)).levelWon();
        assertThat(getGame().getPlayerLevel()).isEqualTo(2);
        assertThat(getGame().isInProgress()).isTrue();
        assertThat(getGame().getLevel().remainingPellets() > 0).isTrue();
    }

    /**
     * Game Won test.
     * Assert that after passing all of the 3 levels, the game is completed.
     */
    @Test
    void gameWon() {

        getLauncher().withMapFile("/UserStory2_5.txt").launch();
        getGame().start();

        assertThat(getGame().isInProgress()).isTrue();

        // Win the level 3 times
        for (int i = 0; i < getNumberOfLevels(); i++) {
            getGame().move(getPlayer(), Direction.EAST);
            getGame().move(getPlayer(), Direction.EAST);
        }

        // Assert that the game is not in progress after beating all levels
        assertThat(getGame().isInProgress()).isFalse();
        assertThat(getGame().getLevel().remainingPellets()).isEqualTo(0);
    }

    /**
     * Get the current game.
     *
     * @return the current game
     */
    @Override
    protected MultiLevelGame getGame() {
        return (MultiLevelGame) getLauncher().getGame();
    }

}
