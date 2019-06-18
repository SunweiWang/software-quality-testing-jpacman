package nl.tudelft.jpacman.integration.suspension;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.game.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Consider the scenario: Given that the game has started, the ghosts and player should be
 * suspended when the player has clicked the stop button. A test suite should setup according to
 * these conditions and check each step with an assert or mock’s verify.
 */
public class SuspensionTests {
    private Game game;
    private Launcher launcher;

    /**
     * Start a launcher, which can display the user interface.
     */
    @BeforeEach
    public void before() {
        launcher = new Launcher();
        launcher.launch();
        game = launcher.getGame();
        game.start();
    }

    /**
     * Close the user interface.
     */
    @AfterEach
    public void after() {
        launcher.dispose();
    }

    /**
     * Scenario 4.1. Suspend the game.
     */
    @Test
    public void scenario41Suspend() {
        assertTrue(game.isInProgress());
        game.stop(); // Test S4.1
        assertFalse(game.isInProgress());
    }

    /**
     * Scenario 4.2. Restart the game.
     */
    @Test
    public void scenario42Suspend() {
        scenario41Suspend();
        game.start(); // And now S4.2
        assertTrue(game.isInProgress());
    }
}
