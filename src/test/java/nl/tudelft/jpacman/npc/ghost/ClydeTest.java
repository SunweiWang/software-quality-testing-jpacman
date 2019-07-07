package nl.tudelft.jpacman.npc.ghost;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;


/**
 * The test case for testing the behaviour of Clyde using the nextAiMove()
 * in the different possible cases.
 */
public class ClydeTest {

    private GhostMapParser ghostMapParser;

    private PlayerFactory playerFactory;

    private PacManSprites spriteStore;

    private GhostFactory ghostFactory;

    private BoardFactory boardFactory;

    private LevelFactory levelFactory;

    /**
     * Setting up the GhostMapParser.
     */
    @BeforeEach
    void setUp() {
        spriteStore = new PacManSprites();
        ghostFactory = new GhostFactory(spriteStore);
        boardFactory = new BoardFactory(spriteStore);
        levelFactory = new LevelFactory(spriteStore,
            ghostFactory,
            mock(PointCalculator.class));
        playerFactory = new PlayerFactory(spriteStore);
        ghostMapParser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);
    }

    /**
     * Testing the method nextAiMove() for when Inky does not occupy a square on the board.
     */
    @Test
    void testNoSquareAssertionError() {
        Ghost clyde = ghostFactory.createClyde();
        assertThrows(AssertionError.class, clyde::nextAiMove);
    }

    /**
     * Testing the direction of the next move of Clyde when he is within eight blocks of the player.
     * The direction we expect from the method nextAiMove() is EAST, away from the player.
     */
    @Test
    void testWithinEightBlocks() {
        ArrayList<String> levelMap = new ArrayList<>();
        levelMap.add("#######");
        levelMap.add("#P   C#");
        levelMap.add("#######");
        Level level = ghostMapParser.parseMap(levelMap);
        Player player = playerFactory.createPacMan();
        level.registerPlayer(player);
        player.setDirection(Direction.EAST);
        assertThat(Objects.requireNonNull(Navigation.findUnitInBoard(Clyde.class, level.getBoard()))
            .nextAiMove())
            .hasValue(Direction.EAST);
    }

    /**
     * Testing the direction of the next move of Clyde when he is further
     * than eight blocks of the player.
     * The direction we expect from the method nextAiMove() is WEST, towards the player.
     */
    @Test
    void testFurtherThanEightBlocks() {
        ArrayList<String> levelMap = new ArrayList<>();
        levelMap.add("#############");
        levelMap.add("#P         C#");
        levelMap.add("#############");
        Level level = ghostMapParser.parseMap(levelMap);
        Player player = playerFactory.createPacMan();
        level.registerPlayer(player);
        player.setDirection(Direction.EAST);
        assertThat(Objects.requireNonNull(Navigation.findUnitInBoard(Clyde.class, level.getBoard()))
            .nextAiMove())
            .hasValue(Direction.WEST);
    }

    /**
     * Testing the method nextAiMove() for when Clyde does not have a player to move to/from.
     */
    @Test
    void testNoPlayer() {
        ArrayList<String> levelMap = new ArrayList<>();
        levelMap.add("####");
        levelMap.add("# C#");
        levelMap.add("####");
        Level level = ghostMapParser.parseMap(levelMap);
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.EAST);
        assertThat(Objects.requireNonNull(Navigation.findUnitInBoard(Clyde.class, level.getBoard()))
            .nextAiMove())
            .isEmpty();
    }

    /**
     * Testing the method nextAiMove() for when Clyde is being stuck in between walls.
     * We expect the next move to be empty because there are no possible paths.
     */
    @Test
    void testNoPossiblePath() {
        ArrayList<String> levelMap = new ArrayList<>();
        levelMap.add("#######");
        levelMap.add("#P  #C#");
        levelMap.add("#######");
        Level level = ghostMapParser.parseMap(levelMap);
        Player player = playerFactory.createPacMan();
        level.registerPlayer(player);
        player.setDirection(Direction.EAST);
        assertThat(Objects.requireNonNull(
            Navigation.findUnitInBoard(Clyde.class, level.getBoard())).nextAiMove())
            .isEqualTo(Optional.empty());
    }

}
