package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Abstract game state test.
 */
public abstract class AbstractGameStateTest {


    /**
     * Init Launcher abstract method.
     *
     * @return The launcher that should be used for this set of tests.
     */
    abstract Launcher initLauncher();

    private Launcher launcher;

    /**
     * Returns the current launcher.
     *
     * @return Launcher
     */
    protected Launcher getLauncher() {

        return launcher;
    }

    /**
     * Initialise the launcher and set up the map.
     */
    @BeforeEach
    void setup() {

        this.launcher = initLauncher();

    }

    /**
     * Pause test.
     * Assert that after clicking stop, the game is paused (stopped)
     */
    @Test
    void pauseGame() {

        launcher.withMapFile("/GameTest.txt").launch();

        this.getGame().start();
        this.getGame().stop();

        assertThat(this.getGame().isInProgress()).isFalse();
    }

    /**
     * Continue test.
     * Assert that after clicking stop and then start, the game is back in progress (started)
     */
    @Test
    void continueGameTest() {

        launcher.withMapFile("/GameTest.txt").launch();

        this.getGame().start();
        this.getGame().stop();

        assertThat(this.getGame().isInProgress()).isFalse();

        this.getGame().start();

        assertThat(this.getGame().isInProgress()).isTrue();
    }

    /**
     * Go to empty cell test.
     * Assert that after going to an empty cell, the game is still in progress
     */
    @Test
    void goToEmptyCell() {

        launcher.withMapFile("/GameTest.txt").launch();

        this.getGame().start();
        this.getGame().move(getPlayer(), Direction.EAST);

        assertThat(this.getGame().isInProgress()).isTrue();
    }

    /**
     * Go to pellet cell test.
     * Assert that after going to an pellet cell, without eating all pellets,
     * the game is still in progress.
     */
    @Test
    void goToPelletCell() {

        launcher.withMapFile("/GameTest.txt").launch();

        this.getGame().start();
        this.getGame().move(getPlayer(), Direction.WEST);

        assertThat(this.getGame().isInProgress()).isTrue();
    }

    /**
     * Go to ghost test.
     * Assert that after hitting a ghost, the player is dead
     */
    @Test
    void goToGhost() {

        Level.LevelObserver loseListener = mock(Level.LevelObserver.class);

        launcher.withMapFile("/GameTestWithGhost.txt").launch();

        this.getGame().getLevel().addObserver(loseListener);

        this.getGame().start();

        this.getGame().move(getPlayer(), Direction.EAST);

        verify(loseListener, times(1)).levelLost();
    }

    /**
     * Go to pellet test.
     * Assert that after eating the last pellet,
     * the game is won and the game is not in progress anymore
     */
    @Test
    void goToPelletAndWin() {

        launcher.withMapFile("/GameTest.txt").launch();

        Level.LevelObserver winListener = mock(Level.LevelObserver.class);
        this.getGame().getLevel().addObserver(winListener);

        this.getGame().start();
        this.getGame().move(getPlayer(), Direction.WEST);
        this.getGame().move(getPlayer(), Direction.WEST);

        assertThat(this.getGame().isInProgress()).isFalse();

        verify(winListener, times(1)).levelWon();
    }


    /**
     * Get the current game.
     *
     * @return the current game
     */
    protected Game getGame() {
        return launcher.getGame();
    }

    /**
     * Get player.
     *
     * @return the current player
     */
    protected Player getPlayer() {

        return launcher.getGame().getPlayers().get(0);
    }


}
