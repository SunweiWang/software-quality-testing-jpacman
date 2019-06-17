package nl.tudelft.jpacman.integration;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Level.LevelObserver;
import nl.tudelft.jpacman.level.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Game state machine.
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

}
