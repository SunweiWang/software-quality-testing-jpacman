package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level.LevelObserver;
import nl.tudelft.jpacman.level.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Game state machine.
 * For Parallel class hierarchy, make a subclass of the current class.
 */
public class GameStateMachineTest {

    private Game game;
    private String mapFile = "/state-board.txt";
    // for observing won/lost
    @Mock
    private LevelObserver observer;
    /**
     * Start the game, and bring it in the PLAYING state.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        Launcher launch = (new Launcher()).withMapFile(mapFile);
        game = launch.makeGame();
        game.getLevel().addObserver(observer);
    }
    /**
     * Expose the map so that the extensions can use it too.
     *
     * @return The map for one level.
     */
    protected String getMapFile() {
        return mapFile;
    }
    /**
    * Expose the game, so that extensions can use it too.
    *
    * @return Game created.
    */
    protected Game getGame() {
        return game;
    }

    /**
     * set the game to this test.
     *
     * @param game the game
     */
    protected void setGame(Game game) {
        this.game = game;
    }
    /**
     * Expose the level osberver, so that extensions can use it too.
     *
     * @return (mocked) observer.
     */
    protected LevelObserver getObserver() {
        return observer;
    }
    /**
     * Expose the player, so that extensions of the state machine
     * can use it to.
     *
     * @return Player created.
     */
    protected Player getPlayer() {
        return getGame().getPlayers().get(0);
    }
    /**y
     * Test that no transitions are possible from the
     * initial (created) state.
     */
    @Test
    void invalidCREATED() {
        // when not started, nothing can be done.
        getGame().move(getPlayer(), Direction.EAST);
        assertThat(getGame().isInProgress()).isFalse();
        verifyNoMoreInteractions(observer);
    }
    /**
     * Path from PLAYING to WON state.
     */
    @Test
    void win() {
        getGame().start();
        assertThat(getGame().isInProgress()).isTrue();
        getGame().move(getPlayer(), Direction.EAST);
        getGame().move(getPlayer(), Direction.EAST);
        verify(observer).levelWon();
        assertThat(getGame().isInProgress()).isFalse();
    }

    /**
     * Move, while staying in the PLAYING state.
     */
    @Test
    void moveWithinPLAYING() {
        getGame().start();
        assertThat(getGame().isInProgress()).isTrue();
        getGame().move(getPlayer(), Direction.EAST);
        assertThat(getPlayer().isAlive()).isTrue();
        verifyZeroInteractions(observer);
        assertThat(getGame().isInProgress()).isTrue();
    }
    /**
     * Path from PLAYING to LOST state.
     */
    @Test
    void die() {
        getGame().start();
        getGame().move(getPlayer(), Direction.WEST);
        getGame().move(getPlayer(), Direction.WEST);
        assertThat(getPlayer().isAlive()).isFalse();
        verify(observer).levelLost();
        assertThat(getGame().isInProgress()).isFalse();
    }
    /**
     * Events that should have no effect in PLAYING state.
     */
    @Test
    void invalidPLAYING() {
        getGame().start();
        getGame().start();
        verifyZeroInteractions(observer);
        assertThat(getGame().isInProgress()).isTrue();
    }
    /**
     * Events that should have no effect in LOST state.
     */
    @Test
    void invalidLOST() {
        die();
        getGame().move(getPlayer(), Direction.EAST);
        getGame().stop();
        getGame().start();
        assertThat(getGame().isInProgress()).isFalse();
        verifyNoMoreInteractions(observer);
    }
    /**
     * Events that should have no effect in WON state.
     */
    @Test
    void invalidWON() {
        win();
        getGame().move(getPlayer(), Direction.NORTH);
        getGame().stop();
        getGame().start();
        assertThat(getGame().isInProgress()).isFalse();
        verifyNoMoreInteractions(observer);
    }
    /**
     * Path from PLAYING to HALTED state.
     */
    @Test
    void pauseAndRestart() {
        getGame().start();
        assertThat(getGame().isInProgress()).isTrue();
        getGame().stop();
        assertThat(getGame().isInProgress()).isFalse();
        // we resume
        getGame().start();
        assertThat(getGame().isInProgress()).isTrue();
        verifyZeroInteractions(observer);
    }
    /**
     * Events that should have no effect in PAUSED state.
     */
    @Test
    void invalidPAUSED() {
        getGame().start();
        getGame().stop();
        // this has no effect (sneak paths)
        getGame().move(getPlayer(), Direction.EAST);
        getGame().move(getPlayer(), Direction.EAST);
        getGame().stop();
    }

}
