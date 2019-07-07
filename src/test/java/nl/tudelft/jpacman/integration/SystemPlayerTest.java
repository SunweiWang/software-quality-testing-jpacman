package nl.tudelft.jpacman.integration;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.Ghost;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class that conducts integration tests on pacman moves.
 */
class SystemPlayerTest {

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
     * Scenario 2.1 After the game has started, the player moves EAST from the starting square.
     * The starting player square should be empty after the move and the square EAST to it should
     * be occupied only by the player and not the pellet. Score should be equal to 10.
     */
    @SuppressWarnings("checkstyle:magicnumber"
        // the test contains non generic numbers specific to the default board
    )
    @Test
    void pacmanEatsPellet() {
        launcher.launch();
        Player pacman = getGame().getPlayers().get(0);
        getGame().start();
        getGame().getLevel().move(pacman, Direction.EAST);

        assertThat(getGame().getLevel().getBoard().squareAt(11, 15).getOccupants().isEmpty())
            .isTrue();

        assertThat(pacman.getScore()).isEqualTo(10);

        assertThat(getGame().getLevel().getBoard().squareAt(12, 15).getOccupants().size())
            .isEqualTo(1);

        assertThat(getGame().getLevel().getBoard().squareAt(12, 15).getOccupants().get(0))
            .isEqualTo(pacman);
    }

    /**
     * Scenario 2.2 After the game has started, the player moves EAST from the starting square.
     * The starting square should be empty after the move and the square EAST to it should be
     * occupied only by pacman.
     * Pacman ate only one pellet and then returns to his
     * original position which has no pellet on it.
     */
    @SuppressWarnings("checkstyle:magicnumber"
        // the test contains non generic numbers specific to the default board
    )
    @Test
    void pacmanMovesOnEmptySquare() {
        launcher.launch();
        Player pacman = getGame().getPlayers().get(0);
        getGame().start();
        getGame().getLevel().move(pacman, Direction.EAST);

        assertThat(getGame().getLevel().getBoard().squareAt(11, 15).getOccupants().isEmpty())
            .isTrue();
        //Checking if pacman ate the pellet.
        assertThat(pacman.getScore()).isEqualTo(10);

        assertThat(getGame().getLevel().getBoard().squareAt(12, 15).getOccupants().get(0))
            .isEqualTo(pacman);

        getGame().getLevel().move(pacman, Direction.WEST);

        assertThat(getGame().getLevel().getBoard().squareAt(11, 15).getOccupants().get(0))
            .isEqualTo(pacman);
        //Checking if pacmans' score did not increase after going on an empty square.
        assertThat(pacman.getScore()).isEqualTo(10);
    }

    /**
     * Scenario 2.3 After the game has started, the player moves NORTH from the starting square.
     * The starting square should still be occupied by pacman because NORTH there is a wall.
     * Pacman did not eat any pellets thus his score should be 0.
     */
    @SuppressWarnings("checkstyle:magicnumber"
        // the test contains non generic numbers specific to the default board
    )
    @Test
    void pacmanHitsWall() {
        launcher.launch();
        Player pacman = getGame().getPlayers().get(0);
        getGame().start();
        getGame().getLevel().move(pacman, Direction.NORTH);

        assertThat(getGame().getLevel().getBoard().squareAt(11, 15).getOccupants().get(0))
            .isEqualTo(pacman);

        assertThat(pacman.getScore()).isEqualTo(0);
    }

    /**
     * Scenario 2.4 After the game has started, the player moves WEST from the starting square.
     * Pacman will collide with a ghost thus making him die and lose the game.
     */
    @Test
    void pacmanLoses() {
        launcher.withMapFile("/integrationtestmap.txt").launch();
        Player pacman = getGame().getPlayers().get(0);
        Ghost ghost = (Ghost) getGame().getLevel().getBoard()
            .squareAt(1, 1).getOccupants().get(0);
        getGame().start();
        getGame().getLevel().addObserver(getGame());
        getGame().getLevel().move(pacman, Direction.WEST);

        assertThat(getGame().isInProgress())
            .isFalse();
        assertThat(pacman.getKiller()).isEqualTo(ghost);
    }

    /**
     * Scenario 2.5 After the game has started, the player moves EAST from the starting square.
     * Pacman will eat one pellet as scenario 2.1 and then moves EAST again to eat the last
     * remaining pellet and win the game.
     * The score should be equal to 20.
     */
    @SuppressWarnings("checkstyle:magicnumber"
        // the test contains non generic numbers specific to the amount of points.
    )
    @Test
    void pacmanWins() {
        launcher.withMapFile("/integrationtestmap.txt").launch();
        Player pacman = getGame().getPlayers().get(0);
        getGame().start();
        getGame().getLevel().addObserver(getGame());
        getGame().getLevel().move(pacman, Direction.EAST);

        assertThat(getGame().getLevel().remainingPellets()).isEqualTo(1);

        getGame().getLevel().move(pacman, Direction.EAST);

        assertThat(pacman.getScore()).isEqualTo(20);

        assertThat(getGame().isInProgress())
            .isFalse();

        assertThat(getGame().getLevel().isAnyPlayerAlive()).isTrue();
        assertThat(pacman.getKiller()).isEqualTo(null);
    }

    private Game getGame() {
        return launcher.getGame();
    }

}
