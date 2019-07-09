package nl.tudelft.jpacman.integration;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
class SuspensionSystemTest {

    private Launcher launcher;

    /**
     * Create the launcher.
     */
    @BeforeEach
    void before() {
        launcher = new Launcher();
    }

    /**
     * Close the user interface.
     */
    @AfterEach
    void after() {
        launcher.dispose();
    }

    /**
     * Pause Test.
     * Check if after the game is stopped, the player and the ghost don't move
     */
    @Test
    void pauseTest() {


        launcher.withMapFile("/UserStory4.txt").launch();

        getGame().start();

        assertThat(getGame().isInProgress()).isTrue();
        getPlayer().setDirection(Direction.WEST);

        getGame().stop();

        assertThat(getGame().isInProgress()).isFalse();

        getGame().move(getPlayer(), Direction.EAST);

        // Assert that the player didn't move (still on WEST)
        assertThat(getPlayer().getDirection()).isEqualTo(Direction.WEST);

    }

    /**
     * Resume game test case.
     * Tests for when the player clicks start after stop has been clicked
     */
    @Test
    void resumeGame() {

        launcher.withMapFile("/UserStory4.txt").launch();

        getGame().start();

        assertThat(getGame().isInProgress()).isTrue();
        getPlayer().setDirection(Direction.WEST);

        getGame().stop();
        getGame().start();

        assertThat(getGame().isInProgress()).isTrue();

        getGame().move(getPlayer(), Direction.EAST);

        // Assert that the player moved (from WEST to EAST)
        assertThat(getPlayer().getDirection()).isEqualTo(Direction.EAST);
    }

    private Game getGame() {
        return launcher.getGame();
    }

    private Player getPlayer() {

        return launcher.getGame().getPlayers().get(0);
    }
}
