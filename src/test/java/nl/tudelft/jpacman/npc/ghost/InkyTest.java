package nl.tudelft.jpacman.npc.ghost;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * Testing the possible moves of Inky.
 */
class InkyTest {

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
        Ghost inky = ghostFactory.createInky();
        assertThrows(AssertionError.class, inky::nextAiMove);
    }

    /**
     * Testing the method nextAiMove() for when Inky is being stuck in between walls.
     * We expect the next move to be empty because there are no possible paths.
     */
    @Test
    void testNoPossiblePath() {
        ArrayList<String> levelMap = new ArrayList<>();
        levelMap.add("##########");
        levelMap.add("#P  B  #I#");
        levelMap.add("##########");
        Level level = ghostMapParser.parseMap(levelMap);
        Player player = playerFactory.createPacMan();
        level.registerPlayer(player);
        player.setDirection(Direction.EAST);
        assertThat(Objects.requireNonNull(
            Navigation.findUnitInBoard(Inky.class, level.getBoard())).nextAiMove())
            .isEqualTo(Optional.empty());
    }

    /**
     * Testing the behaviour of the method nextAiMove() for when there is no blinky on the board.
     * There should be no next move.
     */
    @Test
    void testNoBlinky() {
        ArrayList<String> levelMap = new ArrayList<>();
        levelMap.add("##########");
        levelMap.add("#P     #I#");
        levelMap.add("##########");
        Level level = ghostMapParser.parseMap(levelMap);
        Player player = playerFactory.createPacMan();
        level.registerPlayer(player);
        player.setDirection(Direction.EAST);
        ghostFactory.createBlinky();
        assertThat(Objects.requireNonNull(
            Navigation.findUnitInBoard(Inky.class, level.getBoard())).nextAiMove())
            .isEqualTo(Optional.empty());
    }

    /**
     * Testing the behaviour of the method nextAiMove() for when there is no player on the board.
     * There should be no next move.
     */
    @Test
    void testNoPlayer() {
        ArrayList<String> levelMap = new ArrayList<>();
        levelMap.add("##########");
        levelMap.add("#B     #I#");
        levelMap.add("##########");
        Level level = ghostMapParser.parseMap(levelMap);
        playerFactory.createPacMan();
        assertThat(Objects.requireNonNull(
            Navigation.findUnitInBoard(Inky.class, level.getBoard())).nextAiMove())
            .isEqualTo(Optional.empty());
    }

    /**
     * Testing one of normal behaviours of the method nextAiMove(). The player is facing EAST
     * thus the shortest path possible from Blinky to two squares in front of the player
     * is in a straight line facing EAST. Inky is expected have the pathing towards the end of
     * the corridor, going through both Blinky and the player because he needs to move double
     * the distance of the shortest path.
     */
    @Test
    void testNormalBehaviour() {
        ArrayList<String> levelMap = new ArrayList<>();
        levelMap.add("###################");
        levelMap.add("#  I   B  P       #");
        levelMap.add("###################");
        Level level = ghostMapParser.parseMap(levelMap);
        Player player = playerFactory.createPacMan();
        level.registerPlayer(player);
        player.setDirection(Direction.EAST);
        assertThat(Objects.requireNonNull(
            Navigation.findUnitInBoard(Inky.class, level.getBoard())).nextAiMove())
            .hasValue(Direction.EAST);
    }

    /**
     * Testing the second normal behaviour of the method nextAiMove(). The player is facing EAST
     * thus the shortest path possible in a single straight line from Blinky to two squares in front
     * of the player will make Inky will run away from the player to the square resulting in double
     * the distance of the shortest path.
     */
    @Test
    void testInkyInFrontBlinkyBehindPacMan() {
        ArrayList<String> levelMap = new ArrayList<>();
        levelMap.add("#################");
        levelMap.add("#BPI            #");
        levelMap.add("#################");
        Level level = ghostMapParser.parseMap(levelMap);
        Player player = playerFactory.createPacMan();
        level.registerPlayer(player);
        player.setDirection(Direction.EAST);
        assertThat(Objects.requireNonNull(
            Navigation.findUnitInBoard(Inky.class, level.getBoard())).nextAiMove())
            .hasValue(Direction.EAST);
    }

}
