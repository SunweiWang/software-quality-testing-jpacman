package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * GameUnit test.
 */
class GameUnitTest {

    /**
     * Test for a standard situation.
     * The player is alive and there are remaining pellets
     */
    @Test
    void standardSituationTest() {
        Player playerMock = mock(Player.class);
        Level levelMock = mock(Level.class);
        PointCalculator pointCalculatorMock = mock(PointCalculator.class);

        when(levelMock.isAnyPlayerAlive()).thenReturn(true);
        when(levelMock.remainingPellets()).thenReturn(1);

        SinglePlayerGame game = new SinglePlayerGame(playerMock, levelMock, pointCalculatorMock);
        game.start();


        verify(levelMock, times(1)).start();
        verify(levelMock, times(1)).addObserver(any());
    }

    /**
     * Tests for false scenario.
     * The player is dead and there are no remaining pellets
     */
    @Test
    void falseScenario() {
        Player playerMock = mock(Player.class);
        Level levelMock = mock(Level.class);
        PointCalculator pointCalculatorMock = mock(PointCalculator.class);

        when(levelMock.isAnyPlayerAlive()).thenReturn(false);
        when(levelMock.remainingPellets()).thenReturn(0);

        SinglePlayerGame game = new SinglePlayerGame(playerMock, levelMock, pointCalculatorMock);
        game.start();

        verify(levelMock, times(0)).start();
        verify(levelMock, times(0)).addObserver(any());
    }

    /**
     * Test for a true/false scenario.
     * The player is alive, but there are no pellets
     */
    @Test
    void noPelletsRemaning() {
        Player playerMock = mock(Player.class);
        Level levelMock = mock(Level.class);
        PointCalculator pointCalculatorMock = mock(PointCalculator.class);

        when(levelMock.isAnyPlayerAlive()).thenReturn(true);
        when(levelMock.remainingPellets()).thenReturn(0);

        SinglePlayerGame game = new SinglePlayerGame(playerMock, levelMock, pointCalculatorMock);
        game.start();

        verify(levelMock, times(0)).start();
        verify(levelMock, times(0)).addObserver(any());
    }

    /**
     * Test for a situation when start has been already executed.
     * Therefore, the game is in progress.
     */
    @Test
    void gameInProgressTests() {
        Player playerMock = mock(Player.class);
        Level levelMock = mock(Level.class);
        PointCalculator pointCalculatorMock = mock(PointCalculator.class);

        when(levelMock.isAnyPlayerAlive()).thenReturn(true);
        when(levelMock.remainingPellets()).thenReturn(1);

        SinglePlayerGame game = new SinglePlayerGame(playerMock, levelMock, pointCalculatorMock);
        game.start();
        game.start();


        verify(levelMock, times(1)).start();
        verify(levelMock, times(1)).addObserver(any());
    }
}
