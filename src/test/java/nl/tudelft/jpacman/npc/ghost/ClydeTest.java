package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for the ghost clyde.
 */
public class ClydeTest {


    private GhostMapParser parser;
    private Player p;

    /**
     * Set up before each test.
     *
     * @throws IOException Throws IOException when parser failed.
     */
    @BeforeEach
    void setUp() throws IOException {

        PacManSprites spriteStore = new PacManSprites();

        parser = new GhostMapParser(
            new LevelFactory(spriteStore, new GhostFactory(spriteStore),
                new DefaultPointCalculator()),
            new BoardFactory(spriteStore),
            new GhostFactory(spriteStore)
        );

        p = (new PlayerFactory(spriteStore)).createPacMan();
        p.setDirection(Direction.WEST);


    }

    /**
     * Check if Clayde goes closer if it's at least 8 blocks away from the player.
     *
     * @throws IOException Throws IOException when parser failed.
     */
    @Test
    void checkGoCloser() throws IOException {

        Level l = this.createMap("#...P..........C...#");
        l.registerPlayer(p);

        Clyde c = Navigation.findUnitInBoard(Clyde.class, l.getBoard());

        List<Direction> route = Navigation.shortestPath(p.getSquare(), c.getSquare(), c);
        int oldDistance = route.size();

        l.move(c, c.nextAiMove().get());

        route = Navigation.shortestPath(p.getSquare(), c.getSquare(), c);
        assertThat(oldDistance).isEqualTo(route.size() + 1);
    }

    /**
     * Check if Clayde goes away from the player if it is within 8 blocks of clyde.
     *
     * @throws IOException Throws IOException when parser failed.
     */
    @Test
    void checkGoAway() throws IOException {

        Level l = this.createMap("#...P...C...#");
        l.registerPlayer(p);

        Clyde c = Navigation.findUnitInBoard(Clyde.class, l.getBoard());

        List<Direction> route = Navigation.shortestPath(p.getSquare(), c.getSquare(), c);
        int oldDistance = route.size();

        l.move(c, c.nextAiMove().get());

        route = Navigation.shortestPath(p.getSquare(), c.getSquare(), c);
        assertThat(oldDistance).isEqualTo(route.size() - 1);
    }


    /**
     * Assert that Clyde doesn't move if there is nowhere to move (path is null).
     *
     * @throws IOException Throws IOException when parser failed.
     */
    @Test
    void checkPath() throws IOException {

        Level l = this.createMap("#C#P#");
        l.registerPlayer(p);

        Clyde c = Navigation.findUnitInBoard(Clyde.class, l.getBoard());

        Optional<Direction> direction = c.nextAiMove();
        assertThat(direction.isPresent()).isFalse();
    }

    /**
     * Asserts that Clyde doesn't move when there is no player.
     *
     * @throws IOException Throws IOException when parser failed.
     */
    @Test
    void checkNoPlayer() throws IOException {

        Level l = this.createMap("#...C..#");
        Clyde c = Navigation.findUnitInBoard(Clyde.class, l.getBoard());
        Optional<Direction> direction = c.nextAiMove();
        assertThat(direction.isPresent()).isFalse();

    }

    /**
     * Generates a Level out of the given map.
     *
     * @param mapLayout -> The layout of the map as a string
     * @return the instantiated Level
     */
    Level createMap(String mapLayout) {

        List<String> map = new ArrayList<String>();
        map.add(mapLayout);
        Level l = parser.parseMap(map);
        l.start();

        return l;
    }


}
