package nl.tudelft.jpacman.integration.suspension;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.game.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * suspension.
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
     * 4.1.
     */
    @Test
    public void scenario41Suspend() {
        assertTrue(game.isInProgress());
        game.stop(); // Test S4.1
        assertFalse(game.isInProgress());
    }

    /**
     * 4.2.
     */
    @Test
    public void scenario42Suspend() {
        scenario41Suspend();
        game.start(); // And now S4.2
        assertTrue(game.isInProgress());
    }
}
