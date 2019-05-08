package nl.tudelft.jpacman.npc.ghost;

import com.google.common.collect.Lists;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test suite for the Inky ghost.
 */
class InkyTests {
    private static final PacManSprites PACMAN_SPRITES = new PacManSprites();
    private static GhostMapParser ghostMapParser;

    /**
     * Here, we setup our <i>GhostMapParser</i>, using the various required factories.
     * We abstracted away the method that creates an actual map/Level: <i>createMap</i>.
     */
    @BeforeAll
    static void setUp() {
        GhostFactory ghostFactory = new GhostFactory(PACMAN_SPRITES);
        PointCalculator pointCalculator = new DefaultPointCalculator();
        BoardFactory boardFactory = new BoardFactory(PACMAN_SPRITES);
        LevelFactory levelFactory = new LevelFactory(PACMAN_SPRITES, ghostFactory, pointCalculator);
        ghostMapParser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);
    }

    /**
     * This method allows the test suite to create specific maps for each test-case.
     * Since some test cases require us to create a <i>Level</i> without a Player,
     * we added the createPlayer parameter.
     *
     * @param map
     *          The map that represents the Board to create
     * @param createPlayer
     *          Flag indicating whether to register a Player or not in the Level.
     * @return The created level.
     */
    private Level createMap(List<String> map, boolean createPlayer) {
        Level level = ghostMapParser.parseMap(map);
        PlayerFactory playerFactory = new PlayerFactory(PACMAN_SPRITES);
        if (createPlayer) {
            level.registerPlayer(playerFactory.createPacMan());
        }
        return level;
    }

    /**
     * Good weather test 1: Since Inky and Blinky are alongside each other,
     * they will both want to move in Pacman's direction.
     */
    @Test
    void inkyChasesPacman() {
        List<String> map = Lists.newArrayList(
            "##############",
            "#      P   IB#",
            "#          # #",
            "##############"
        );

        Level level = createMap(map, true);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Optional<Direction> aiMove = inky.nextAiMove();

        assertThat(aiMove).contains(Direction.WEST);
    }

    /**
     * Good weather test 2: Inky will stay ahead of Pacman. Pacman is facing east,
     * so twice the distance from Blinky will be a point far in the east.
     */
    @Test
    void inkyGetsAhead() {
        List<String> map = Lists.newArrayList(
            "############################",
            "#B     P   I               #",
            "#                          #",
            "############################"
        );

        Level level = createMap(map, true);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Optional<Direction> aiMove = inky.nextAiMove();

        assertThat(aiMove).contains(Direction.EAST);
    }

    /**
     * Bad weather test 3: No Pacman on the board.
     */
    @Test
    void noPlayer() {
        List<String> map = Lists.newArrayList(
            "##############",
            "#    #      B#",
            "#   I#       #",
            "##############"
        );

        Level level = createMap(map, false);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Optional<Direction> aiMove = inky.nextAiMove();

        assertThat(aiMove).isEmpty();
    }

    /**
     * Bad weather test 4: No Blinky on the board.
     */
    @Test
    void noBlinky() {
        List<String> map = Lists.newArrayList(
            "##############",
            "#    #    P  #",
            "#   I#       #",
            "##############"
        );

        Level level = createMap(map, true);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Optional<Direction> aiMove = inky.nextAiMove();

        assertThat(aiMove).isEmpty();
    }

    /**
     * Bad weather 5.A: No path from Inky to the <i>destination</i>.
     * Recall that the pathfinding between Inky and the target
     * should use Inky as traveller. Here we have blocked the target
     * cell with a wall, so we expect Inky to do an empty move.
     */
    @Test
    void squareAHeadOfPlayerIsAWall() {
        List<String> map = Lists.newArrayList(
            "##############",
            "#######P    B#",
            "#   I        #",
            "##############"
        );

        Level level = createMap(map, true);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Optional<Direction> aiMove = inky.nextAiMove();
        assertThat(aiMove).isEmpty();
    }

    /**
     * Bad weather 5.B: No path from Inky to Pacman.
     * Recall that the pathfinding between blinky and the player
     * should ignore walls. Hence, we expect Inky
     * to behave properly in this special case.
     */
    @Test
    void noPathFromBlinkyToPlayer() {
        List<String> map = Lists.newArrayList(
            "##############",
            "#      P   #B#",
            "#   I      # #",
            "##############"
        );

        Level level = createMap(map, true);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Optional<Direction> aiMove = inky.nextAiMove();

        assertThat(aiMove).contains(Direction.NORTH);
    }

    /**
     * Bad weather 6: Inky is already at the destination.
     */
    @Test
    void inkyAlreadyAtTargetLocation() {
        List<String> map = Lists.newArrayList(
            "##############",
            "#....I.....PB#",
            "#            #",
            "##############"
        );

        Level level =  createMap(map, true);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Optional<Direction> aiMove = inky.nextAiMove();

        assertThat(aiMove).isEmpty();
    }
}
