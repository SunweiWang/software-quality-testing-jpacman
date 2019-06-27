package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Series of test cases for the map parser.
 */
class MapParserTest {
    /**
     * The object under test.
     */
    private MapParser mapParser;

    /**
     * The dependencies of the object under test.
     * They will be verified to see if the correct factory
     * methods are invoked upon parsing the relevant lines.
     */
    private LevelFactory levelFactory;
    private BoardFactory boardFactory;

    /**
     * Instantiate the parser with its (mock) factories.
     *
     * [Good Weather]
     */
    @BeforeEach
    void setUp() {
        levelFactory = mock(LevelFactory.class);
        boardFactory = mock(BoardFactory.class);

        mapParser = new MapParser(levelFactory, boardFactory);
    }

    /**
     * Test that an empty square is actually created.
     *
     * [Good Weather]
     */
    @Test
    void parseEmptySquare() {
        mapParser.parseMap(singletonList(" "));

        verify(boardFactory).createGround();
        verifyLevelCreated();
    }

    /**
     * Test that a wall is actually created.
     *
     * [Good Weather]
     */
    @Test
    void parseWall() {
        mapParser.parseMap(singletonList("#"));

        verify(boardFactory).createWall();
        verifyLevelCreated();
    }

    /**
     * Test that a pellet is actually created.
     *
     * [Good Weather]
     */
    @Test
    void parsePellet() {
        Square square = mock(Square.class);
        when(boardFactory.createGround()).thenReturn(square);

        Pellet pellet = mock(Pellet.class);
        when(levelFactory.createPellet()).thenReturn(pellet);

        mapParser.parseMap(singletonList("."));

        verify(boardFactory).createGround();
        verify(levelFactory).createPellet();
        verify(pellet).occupy(square);
        verifyLevelCreated();
    }

    /**
     * Test that a player is actually created.
     *
     * [Good Weather]
     */
    @Test
    void parsePlayer() {
        Square playerSquare = mock(Square.class);
        when(boardFactory.createGround()).thenReturn(playerSquare);

        mapParser.parseMap(singletonList("P"));

        verify(boardFactory).createGround();
        verify(levelFactory).createLevel(any(), anyList(), eq(singletonList(playerSquare)));
    }

    /**
     * Test that a ghost is actually created.
     *
     * [Good Weather]
     */
    @Test
    void parseGhost() {
        Square square = mock(Square.class);
        when(boardFactory.createGround()).thenReturn(square);

        Ghost ghost = mock(Ghost.class);
        when(levelFactory.createGhost()).thenReturn(ghost);

        mapParser.parseMap(singletonList("G"));

        verify(boardFactory).createGround();
        verify(levelFactory).createGhost();
        verify(ghost).occupy(square);
        verifyLevelCreated();
    }

    /**
     * Test that a map provided as a char array can be parsed.
     *
     * [Good Weather]
     */
    @Test
    void parseArray() {
        mapParser.parseMap(new char[][] {{' '}, {'#'}});

        verify(boardFactory).createGround();
        verify(boardFactory).createWall();
        verifyLevelCreated();
    }

    /**
     * Test that a map provided as a file can be parsed.
     *
     * [Good Weather]
     *
     * Contents of basicmap.txt:
     * <code>
     *     P
     * </code>
     *
     * @throws IOException in case the map file could not be loaded.
     */
    @Test
    void parseFile() throws IOException {
        mapParser.parseMap("/basicmap.txt");

        verify(boardFactory).createGround();
        verifyLevelCreated();
    }

    /**
     * Test that parsing zero lines fails.
     *
     * [Bad Weather]
     */
    @Test
    void invalidEmptyList() {
        assertThatThrownBy(() -> mapParser.parseMap(emptyList()))
            .isInstanceOf(PacmanConfigurationException.class);
    }

    /**
     * Test that parsing an empty line fails.
     *
     * [Bad Weather]
     */
    @Test
    void invalidEmptyLine() {
        assertThatThrownBy(() -> mapParser.parseMap(singletonList("")))
            .isInstanceOf(PacmanConfigurationException.class);
    }

    /**
     * Test that lines cannot have different lengths.
     *
     * [Bad Weather]
     */
    @Test
    void invalidLineLengths() {
        assertThatThrownBy(() -> mapParser.parseMap(asList("##", "#")))
            .isInstanceOf(PacmanConfigurationException.class);
    }

    /**
     * Test that lines cannot contain invalid characters.
     *
     * [Bad Weather]
     */
    @Test
    void invalidCharacter() {
        assertThatThrownBy(() -> mapParser.parseMap(singletonList("!")))
            .isInstanceOf(PacmanConfigurationException.class);
    }

    /**
     * Test that a non existing file cannot be parsed.
     *
     * [Bad Weather]
     */
    @Test
    void nonExistingFile() {
        assertThatThrownBy(() -> mapParser.parseMap("/does-not-exist.txt"))
            .isInstanceOf(PacmanConfigurationException.class);
    }

    /**
     * Verifies that a level is created.
     */
    void verifyLevelCreated() {
        verify(levelFactory).createLevel(any(), anyList(), anyList());
    }
}
