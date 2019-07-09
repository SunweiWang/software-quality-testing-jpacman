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
 * Test for the Inky ghost ai moves.
 */
class InkyTest {


    private GhostMapParser parser;
    private Player p;

    /**
     * Set up before each test.
     * Generates the GhostMapParser and initialises the player
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
     * Check if Inky goes towards the player if Blinky is in front of Inky.
     *
     * @throws IOException Throws IOException when parser failed.
     */
    @Test
    void checkGoCloser() throws IOException {

        Level l = this.createMap("#......P.B.I#");
        l.registerPlayer(p);

        Inky c = Navigation.findUnitInBoard(Inky.class, l.getBoard());

        List<Direction> route = Navigation.shortestPath(p.getSquare(), c.getSquare(), c);
        int oldDistance = route.size();

        l.move(c, c.nextAiMove().get());
        route = Navigation.shortestPath(p.getSquare(), c.getSquare(), c);

        assertThat(oldDistance).isEqualTo(route.size() + 1);
    }

    /**
     * Check if Inky goes away from the player if Blinky is too far behind.
     *
     * @throws IOException Throws IOException when parser failed.
     */
    @Test
    void checkGoAway() throws IOException {

        Level l = this.createMap("#......I.P...B#");
        l.registerPlayer(p);

        Inky i = Navigation.findUnitInBoard(Inky.class, l.getBoard());

        List<Direction> route = Navigation.shortestPath(p.getSquare(), i.getSquare(), i);
        int oldDistance = route.size();
        l.move(i, i.nextAiMove().get());
        route = Navigation.shortestPath(p.getSquare(), i.getSquare(), i);

        assertThat(oldDistance).isEqualTo(route.size() - 1);
    }


    /**
     * Assert that Inky doesn't move if there is nowhere to move (path is null).
     *
     * @throws IOException Throws IOException when parser failed.
     */
    @Test
    void checkPath() throws IOException {


        Level l = this.createMap("####I#B#P####");
        l.registerPlayer(p);

        Inky i = Navigation.findUnitInBoard(Inky.class, l.getBoard());

        Optional<Direction> direction = i.nextAiMove();

        assertThat(direction.isPresent()).isFalse();
    }

    /**
     * Asserts that Inky doesn't move when there is no player.
     *
     * @throws IOException Throws IOException when parser failed.
     */
    @Test
    void checkNoPlayer() throws IOException {

        Level l = this.createMap("#..B.I..#");
        Inky i = Navigation.findUnitInBoard(Inky.class, l.getBoard());
        Optional<Direction> direction = i.nextAiMove();

        assertThat(direction.isPresent()).isFalse();
    }

    /**
     * Asserts that Inky doesn't move when there is no Blinky.
     *
     * @throws IOException Throws IOException when parser failed.
     */
    @Test
    void checkNoBlinky() throws IOException {

        Level l = this.createMap("#...P...I...#");
        Inky i = Navigation.findUnitInBoard(Inky.class, l.getBoard());
        Optional<Direction> direction = i.nextAiMove();

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
