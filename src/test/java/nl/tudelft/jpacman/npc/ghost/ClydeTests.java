package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the ai of the Clyde ghost.
 */
class ClydeTests {
    private static final PacManSprites PACMAN_SPRITES = new PacManSprites();
    private static GhostMapParser ghostMapParser;

    private static List<String> noPathMap = Lists.newArrayList(
        "###########",
        "#P#C      #",
        "###########"
    );

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
     * @param map
     *          The map that represents the Board to create
     * @param createPlayer
     *          Flag indicating whether to register a Player or not in the Level.
     * @return
     */
    private Level createLevel(List<String> map, boolean createPlayer) {
        Level level = ghostMapParser.parseMap(map);
        PlayerFactory playerFactory = new PlayerFactory(PACMAN_SPRITES);
        if (createPlayer) {
            level.registerPlayer(playerFactory.createPacMan());
        }
        return level;
    }

    /**
     * If clyde is far away (> 8 tiles, see <i>Clyde</i>) form pacman he should chase.
     */
    @Test
    void clyde_not_shy() {
        List<String> map = Lists.newArrayList(
            "############",
            "#P        C#",
            "############"
        );

        Level level = createLevel(map, true);
        Ghost clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(clyde.nextAiMove()).contains(Direction.WEST);
    }

    /**
     * If clyde is close to pacman he should run.
     */
    @Test
    void clyde_shy() {
        List<String> map = Lists.newArrayList(
            "###########",
            "#P C      #",
            "###########"
        );

        Level level = createLevel(map, true);
        Ghost clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(clyde.nextAiMove()).contains(Direction.EAST);
    }

    /**
     * If clyde has no path to the player he should make a random move.
     */
    @Test
    void no_path_to_player() {
        Level level = createLevel(noPathMap, true);
        Ghost clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(clyde.nextAiMove()).isEmpty();
    }

    /**
     * If there is no player clyde should never find a path to him.
     */
    @Test
    void no_player_on_the_map() {
        Level level = createLevel(noPathMap, false);
        Ghost clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(clyde.nextAiMove()).isEmpty();
    }

    /**
     * Negative case for the squaresAheadOf method, should return the Square of the Unit itself.
     */
    @Test
    void squares_ahead_negative() {
        Level level = createLevel(noPathMap, true);
        Ghost clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        Square target = clyde.getSquare();
        assertThat(clyde.squaresAheadOf(-1)).isEqualTo(target);
    }

    /**
     * Case that exceeds bounds for the squaresAheadOf method. Since we have wrap around,
     * it should re-enter on the left side of the screen, so in this case (just over
     * the bounds) it finds the left most square.
     */
    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void squares_ahead_out_of_bounds() {
        Level level = createLevel(noPathMap, false);
        Ghost clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        Square target = level.getBoard().squareAt(0, 1);
        assertThat(clyde.squaresAheadOf(8)).isEqualTo(target);
    }

    /**
     * Case that has regular behaviour for the squaresAheadOf method. Should return
     * the square right next to the Unit.
     */
    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void squares_ahead_of_within_bounds() {
        Level level = createLevel(noPathMap, false);
        Ghost clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        Square target = level.getBoard().squareAt(4, 1);
        assertThat(clyde.squaresAheadOf(1)).isEqualTo(target);
    }
}
