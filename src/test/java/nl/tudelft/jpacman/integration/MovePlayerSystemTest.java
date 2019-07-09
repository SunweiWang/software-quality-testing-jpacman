package nl.tudelft.jpacman.integration;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Move Player test.
 */
public class MovePlayerSystemTest {

    private Launcher launcher;

    /**
     * Create the launcher.
     */
    @BeforeEach
    void setup() {
        this.launcher = new Launcher();
    }

    /**
     * Close the user interface.
     */
    @AfterEach
    void after() {
        launcher.dispose();
    }


    /**
     * Move to pellet text.
     * Check if when the player has gone to a pellet, his points increase and the pellet dissapears
     */
    @Test
    void moveToPellet() {

        launcher.withMapFile("/UserStory2_1.txt").launch();

        getGame().start();

        // Get the pellet that will be eaten
        Pellet pelletInMap = (Pellet) getGame().getLevel()
            .getBoard()
            .squareAt(2, 1)
            .getOccupants().get(0);

        // Assert that the player has 0 points
        assertThat(getPlayer().getScore()).isEqualTo(0);

        // Move the player towards the pellet
        getGame().move(getPlayer(), Direction.EAST);

        // Assert that the player's score has increased by the value of the pellet
        assertThat(getPlayer().getScore()).isEqualTo(pelletInMap.getValue());

        // Assert that the pellet is not in the cell anymore
        assertThat(getGame().getLevel().getBoard().squareAt(2, 1)
            .getOccupants()).doesNotContain(pelletInMap);

        // Assert that the player is now in that cell
        assertThat(getGame().getLevel().getBoard().squareAt(2, 1)
            .getOccupants()).contains(getPlayer());
    }

    /**
     * Move to pellet text.
     * Check if when the player has gone to a blank space,
     * the player moves there and the points remain the same
     */
    @Test
    void moveToEmptyBlock() {

        this.launcher = new Launcher();

        launcher.withMapFile("/UserStory2_2.txt").launch();

        getGame().start();

        // The current position of the player
        int currPlayerX = 1;
        int currPlayerY = 1;

        // Assert that the player starts with 0 points
        assertThat(getPlayer().getScore()).isEqualTo(0);

        // Assert that the player is in the cell with coordinates (1,1)
        assertThat(getGame().getLevel().getBoard().squareAt(currPlayerX, currPlayerY)
            .getOccupants()).contains(getPlayer());

        // Move the player towards an empty cell
        getGame().move(getPlayer(), Direction.EAST);

        currPlayerX += Direction.EAST.getDeltaX();

        // Assert that the player's score is still 0 and that the player is in the cell east of him
        assertThat(getPlayer().getScore()).isEqualTo(0);
        assertThat(getGame().getLevel().getBoard().squareAt(currPlayerX, currPlayerY)
            .getOccupants()).contains(getPlayer());

    }

    /**
     * Move to pellet text.
     * Check if when the player has gone to a wall space, the player doesn't move
     */
    @Test
    void moveToWall() {

        this.launcher = new Launcher();

        launcher.withMapFile("/UserStory2_3.txt").launch();

        getGame().start();

        int currPlayerX = 1;
        int currPlayerY = 1;

        // Assert that the score is 0
        assertThat(getPlayer().getScore()).isEqualTo(0);

        // Assert that the player is in cell with coordiantes (1, 1)
        assertThat(getGame().getLevel().getBoard().squareAt(currPlayerX, currPlayerY)
            .getOccupants()).contains(getPlayer());

        // Move the player towards a wall
        getGame().move(getPlayer(), Direction.EAST);

        // Assert that the player is still in the initial cell and that his score is still 0
        assertThat(getPlayer().getScore()).isEqualTo(0);
        assertThat(getGame().getLevel().getBoard().squareAt(currPlayerX, currPlayerY)
            .getOccupants()).contains(getPlayer());

    }

    /**
     * Move to pellet text.
     * Check if when the player has gone to a ghost, the player should die
     */
    @Test
    void moveToGhostAndDie() {

        this.launcher = new Launcher();

        launcher.withMapFile("/UserStory2_4.txt").launch();

        getGame().start();

        // Move the player towards the ghost in the map
        getGame().move(getPlayer(), Direction.EAST);

        // Assert that the player has died
        assertThat(getPlayer().isAlive()).isFalse();

    }

    /**
     * Move to pellet text.
     * Check if when the player has gone to a ghost, the player should die
     */
    @Test
    void playerWins() {

        this.launcher = new Launcher();

        launcher.withMapFile("/UserStory2_5.txt").launch();

        getGame().start();


        assertThat(getGame().isInProgress()).isTrue();

        // Eat all of the pellets in the map
        getGame().move(getPlayer(), Direction.EAST);
        getGame().move(getPlayer(), Direction.EAST);

        // Assert that the player has won
        assertThat(getGame().isInProgress()).isFalse();

    }

    private Game getGame() {
        return launcher.getGame();
    }

    private Player getPlayer() {

        return launcher.getGame().getPlayers().get(0);
    }
}
