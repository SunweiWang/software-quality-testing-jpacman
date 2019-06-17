package nl.tudelft.jpacman.integration;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.ghost.Navigation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


/**
 * 2.
 */
public class Scenario2Tests {

    private Game game;
    private Launcher launcher;
    private MapParser mapParser;
    private Player player;
    private static final int SCORE = 10;
    private static final int EMPTY_SCORE = 0;
    private static final long SLEEP_TIME = 500L;


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
     * Helper method to instantiate a launcher with
     * a specific board.
     */
    private void setupWithBoard(String boardFile) {
        launcher.dispose();
        launcher = new Launcher();
        launcher = launcher.withMapFile(boardFile);
        launcher.launch();
        game = launcher.getGame();
        game.start();
        player = game.getPlayers().get(0);
    }
    /**
     * Close the user interface.
     */
    @AfterEach
    public void after() {
        launcher.dispose();
    }

    /**
     * Scenario S2.1 - The player consumes.
     */
    @Test
    public void s21MoveOverPelletAndConsume() {
        setupWithBoard("/simplemap.txt");
        game.move(player, Direction.EAST);
        assertEquals(SCORE, player.getScore());
// make sure the square is empty now
        Unit pellet = Navigation.findUnit(Pellet.class, player.getSquare());
        assertEquals(null, pellet);
    }
    /**
     * Scenario S2.2 - The player moves on empty square.
     */
    @Test
    public void s22MoveOverEmptySquare() {
        setupWithBoard("/simplemap.txt");
// initial cell is empty. First leave it.
        game.move(player, Direction.WEST);
        assertEquals(EMPTY_SCORE, player.getScore());
// then return to it. Points should remain unaltered.
        game.move(player, Direction.EAST);
        assertEquals(EMPTY_SCORE, player.getScore());
    }

    /**
     * Scenario S2.3 - The move fails.
     */
    @Test
    public void s23MoveFails() {
        setupWithBoard("/simplemap.txt");
        Square startingPosition = player.getSquare();
        game.move(player, Direction.SOUTH);
        assertEquals(startingPosition, player.getSquare());
    }

    /**
     * Scenario 2.4: Test Pacman dying.
     * To more easily collide Pacman we use this map:
     *
     * @throws InterruptedException exception throw.
     */
    @Test
    public void s24pacmanDies() throws InterruptedException {
        setupWithBoard("/pacmandies.txt");
        assertTrue(game.isInProgress());
        // We’re close to the Ghost
        // wait a little, and we should die
        Thread.sleep(SLEEP_TIME);
        assertFalse(game.getLevel().isAnyPlayerAlive());
    }

    /**
     * Scenario S2.5: Player wins, extends S2.1.
     * To more easily win we use this map:
     *
     * @throws InterruptedException exception throw.
     */
    @Test
    public void s25PlayerWins() {
        // Setup with small board for an easier win
        setupWithBoard("/easywin.txt");
        // We check that the game is in progress
        assertTrue(game.isInProgress());
        // Now we perform the move
        assertEquals(EMPTY_SCORE, player.getScore());
        game.move(player, Direction.WEST);
        assertEquals(SCORE, player.getScore());
        // Now we check that the game has ended
        assertFalse(game.isInProgress());
    }
}
