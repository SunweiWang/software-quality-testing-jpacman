package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.level.Level;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Jave doc.
 */
public class SingleLevelTest extends GameAbstractTest {
    private Game game;
    // for observing won/lost
    @Mock
    private Level.LevelObserver observer;


    /**
     * Start the game, and bring it in the PLAYING state.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        Launcher launch = (new Launcher()).withMapFile(getMapFile());
        game = launch.makeGame();
        setGame(game);
        game.getLevel().addObserver(observer);
        setObserver(observer);
    }
}
