package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.MultiLevelLauncher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.level.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Jave doc.
 */
public class MultipleLevelTest extends GameAbstractTest {
    private MultiLevelSinglePlayerGame multiGame;
    // for observing won/lost
    @Mock
    private Level.LevelObserver observer;


    /**
     * Start the game, and bring it in the PLAYING state.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        MultiLevelLauncher launch = (new MultiLevelLauncher()).withMapFile(getMapFile());
        launch.launch();
        multiGame = launch.getGame();
        setGame(multiGame);
        multiGame.getLevel().addObserver(observer);
        setObserver(observer);
    }

    /**
     * Proceed from one level to the next.
     */
    @Test
    void winOneLevelAndMoveToNext() {
        // save some old state for testing purposes
        Level startLevel = multiGame.getLevel();
        int levelsLeft = multiGame.levelsLeft();
        assertThat(levelsLeft).isPositive();
        // enter the WON-LEVEL state
        win();
        Square winPosition = getPlayer().getSquare();
        // move on to the next level
        multiGame.start();
        assertThat(multiGame.levelsLeft())
            .isEqualTo(levelsLeft - 1);
        assertThat(multiGame.getLevel())
            .isNotEqualTo(startLevel);
        assertThat(getPlayer().getSquare())
            .isNotEqualTo(winPosition)
            .withFailMessage("Player active on new level");
        assertThat(multiGame.isInProgress()).isFalse()
            .withFailMessage("Should be in CREATE state");
    }

    /**
     * Overriding WON sneak paths, since now a ’start’ event on
     * Game should have effect.
     * Sneak paths are tricky, as they define ’negative’ behavior --
     transitions
     * that remain forbidden now and in the future.
     */
    @Override
    @Test
    void invalidWON() {
        win();
        getGame().move(getPlayer(), Direction.NORTH);
        getGame().stop();
        assertThat(multiGame.isInProgress()).isFalse();
        verifyNoMoreInteractions(getObserver());
    }
    /**
     * Finalize the full game successfully.
     */
    @Test
    void winAllLevels() {
        // Win both levels.
        Level startLevel = multiGame.getLevel();
        winOneLevelAndMoveToNext();
        winOneLevelAndMoveToNext();
        win();
        // Now there are no more levels to play.
        assertThat(multiGame.levelsLeft()).isZero();
        assertThat(multiGame.isInProgress()).isFalse();
        // Restart should now not have effect.
        multiGame.start();
        assertThat(multiGame.isInProgress()).isFalse();
        assertThat(multiGame.getLevel())
            .isNotEqualTo(startLevel).withFailMessage("Still pointing to old level");
    }
}
