package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.MultiLevelLauncher;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Multi-Level.
 */
public class MultiLevelGameStateMachineTest extends GameStateMachineTest {

    private String mapFile = "/state-board.txt";
    // for observing won/lost
    @Mock
    private Level.LevelObserver observer;

    private PointCalculator pointCalculator = new DefaultPointCalculator();
    private MultiLevelSinglePlayerGame multiGame;

    /**
     * set up game.
     *
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        MultiLevelLauncher launch = (new MultiLevelLauncher()).withMapFile(mapFile);
        multiGame = launch.makeGame();
        multiGame.getLevel().addObserver(observer);
    }

    /**
     * Override get game.
     * @return
     */
    @Override
    protected MultiLevelSinglePlayerGame getGame() {
        return multiGame;
    }

    /**
     * Proceed from one level to the next.
     */
    @Test
    void winOneLevelAndMoveToNext() {
        // save some old state for testing purposes
        Level startLevel = getGame().getLevel();
        int levelsLeft = getGame().levelsLeft();
        assertThat(levelsLeft).isPositive();
        // enter the WON-LEVEL state
        win();
        Square winPosition = getPlayer().getSquare();
        // move on to the next level
        getGame().start();
        assertThat(getGame().levelsLeft())
            .isEqualTo(levelsLeft - 1);
        assertThat(getGame().getLevel())
            .isNotEqualTo(startLevel);
        assertThat(getPlayer().getSquare())
            .isNotEqualTo(winPosition)
            .withFailMessage("Player active on new level");
        assertThat(getGame().isInProgress()).isFalse()
            .withFailMessage("Should be in CREATE state");
    }
}
