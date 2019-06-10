package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *  JavaDoc here.
 */
class GameUnitTest {
    /**
     * The object under test.
     */
    private Game game;

    /**
     * The dependencies of the object under test.
     * They will be verified to see if the correct factory
     * methods are invoked upon parsing the relevant lines.
     */
    private Player player;
    private Level level;
    private PointCalculator pointCalculator;

    @BeforeEach
    void setUp() {
        player = mock(Player.class);
        level = mock(Level.class);
        pointCalculator = mock(PointCalculator.class);
        game = new SinglePlayerGame(player, level, pointCalculator);
    }

    /**
     * Case 1: Starting a brand new game.
     */
    @Test
    void startGame() {
        final int randomPoints = 42;
        when(level.isAnyPlayerAlive()).thenReturn(true);
        when(level.remainingPellets()).thenReturn(randomPoints);

        game.start();

        verify(level).start();
        verify(level).addObserver(game);
        assertThat(game.isInProgress()).isTrue();
    }

    /**
     * Case 2: Restarting an already running game.
     */
    @Test
    void startGameWhileAlreadyInProgress() {
        final int randomPoints = 42;
        when(level.isAnyPlayerAlive()).thenReturn(true);
        when(level.remainingPellets()).thenReturn(randomPoints);

        game.start();
        verify(level).start();
        verify(level).addObserver(game);

        reset(level); // resets history of interactions

        game.start();
        verify(level, never()).start();
        verify(level, never()).addObserver(game);
    }

    /**
     * Case 3: Attempting to restart a game in which the player had died already.
     */
    @Test
    void startGameWhileNoPlayerAliveAndNoRemainingPellets() {
        when(level.isAnyPlayerAlive()).thenReturn(false);
        when(level.remainingPellets()).thenReturn(0);

        game.start();
        verify(level, never()).start();
        verify(level, never()).addObserver(game);
    }

    /**
     * Case 4: Attempting to restart a game in
     * which the player had already won the level.
     */
    @Test
    void allEatenStart() {
        when(level.isAnyPlayerAlive()).thenReturn(true);
        when(level.remainingPellets()).thenReturn(0);
        game.start();
        verify(level, never()).start();
        assertThat(game.isInProgress()).isFalse();
    }
}
