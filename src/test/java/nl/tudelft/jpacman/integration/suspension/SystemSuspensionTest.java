package nl.tudelft.jpacman.integration.suspension;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class that conducts integration tests on game suspension.
 */
class SystemSuspensionTest {

    private Launcher launcher;

    /**
     * Start a launcher, which can display the user interface.
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
     * Scenario 4.1 After the game has started, the player clicks on the "Stop" button.
     * The game should pause.
     * The test just starts the game and then pauses it.
     * It checks if the game is indeed not in progress.
     */
    @SuppressWarnings("checkstyle:magicnumber"
        // the test contains non generic numbers specific to the default board
    )
    @Test
    void gameIsSuspended() {
        launcher.launch();
        getGame().start();
        getGame().stop();
        assertThat(getGame().isInProgress()).isFalse();
        Player pacman = getGame().getPlayers().get(0);
        getGame().getLevel().move(pacman, Direction.EAST);
        assertThat(getGame().getLevel().getBoard().squareAt(11, 15).getOccupants().isEmpty())
            .isFalse();
    }

    /**
     * Scenario 4.2 After the game has been suspended, the player clicks on the "Start" button.
     * The game should resume.
     * The test just stops the game and then resumes it.
     * It checks if the game is indeed in progress.
     */
    @SuppressWarnings("checkstyle:magicnumber"
        // the test contains non generic numbers specific to the default board
    )
    @Test
    void gameIsResumed() {
        launcher.launch();
        getGame().start();
        getGame().stop();
        assertThat(getGame().isInProgress()).isFalse();
        getGame().start();
        assertThat(getGame().isInProgress()).isTrue();
        Player pacman = getGame().getPlayers().get(0);
        getGame().getLevel().move(pacman, Direction.EAST);
        assertThat(getGame().getLevel().getBoard().squareAt(11, 15).getOccupants().isEmpty())
            .isTrue();
    }

    private Game getGame() {
        return launcher.getGame();
    }

}
