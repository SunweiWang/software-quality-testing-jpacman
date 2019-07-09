package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Map parser test.
 */
class MapParserTest {

    private MapParser mapParser;
    private LevelFactory levelFactory = mock(LevelFactory.class);
    private BoardFactory boardFactory = mock(BoardFactory.class);

    /**
     * Setup before all tests.
     */
    @BeforeEach
    void setup() {

        this.mapParser = new MapParser(levelFactory, boardFactory);

    }


    /**
     * Test the correct read of a sample map.
     * Good weather test.
     */
    @Test
    void parseMapStreamTest() {
        String map = "/simplemap.txt";
        Pellet pellet = mock(Pellet.class);
        Square squareMock = mock(Square.class);
        Level levelMock = mock(Level.class);
        Ghost ghostMock = mock(Ghost.class);

        when(levelFactory.createPellet()).thenReturn(pellet);
        when(boardFactory.createGround()).thenReturn(squareMock);
        when(levelFactory.createGhost()).thenReturn(ghostMock);

        when(levelFactory.createLevel(any(), any(), any())).thenReturn(levelMock);


        Level level = null;
        try {
            level = this.mapParser.parseMap(map);
        } catch (IOException e) {
            fail();
        }

        assertEquals(level, levelMock);

        verify(boardFactory, times(1)).createBoard(any());
        verify(levelFactory, times(1)).createLevel(any(), any(), any());
    }

    /**
     * Tests situation when the map contains an unknown character.
     * Bad weather test
     */
    @Test
    void parseMapInvalidCharacterTest() {


        Pellet pellet = mock(Pellet.class);
        Square square = mock(Square.class);
        Level levelMock = mock(Level.class);

        when(levelFactory.createPellet()).thenReturn(pellet);
        when(boardFactory.createGround()).thenReturn(square);
        when(levelFactory.createLevel(any(), any(), any())).thenReturn(levelMock);

        char[][] map = {
            {'X'}
        };

        assertThrows(PacmanConfigurationException.class, () -> {
            this.mapParser.parseMap(map);
        });

        verify(boardFactory, times(0)).createBoard(any());
        verify(levelFactory, times(0)).createLevel(any(), any(), any());
    }

    /**
     * Test for when the input is null
     * Bad weather test.
     */
    @Test
    void invalidMapTest() {

        assertThrows(PacmanConfigurationException.class, () -> {
            this.mapParser.parseMap((List<String>) null);
        }, "Input text cannot be null.");

    }

    /**
     * Test for when the map arraylist doesn't contain any elements.
     * Bad weather test
     */
    @Test
    void testWithEmptyMap() {

        List<String> map = new ArrayList();

        assertThrows(PacmanConfigurationException.class, () -> {
            this.mapParser.parseMap(map);
        }, "Input text must consist of at least 1 row.");

    }

    /**
     * Test for an arraylist with an empty string.
     * Bad weather test
     */
    @Test
    void testEmptyString() {

        List<String> map = new ArrayList();
        map.add("");

        assertThrows(PacmanConfigurationException.class, () -> {
            this.mapParser.parseMap(map);
        }, "Input text lines cannot be empty.");

    }

    /**
     * Test for an arraylist with two lines with different amount of characters
     * Bad weather test.
     */
    @Test
    void testDifferentLength() {

        List<String> map = new ArrayList();
        map.add("#");
        map.add("##");

        assertThrows(PacmanConfigurationException.class, () -> {
            this.mapParser.parseMap(map);
        }, "Input text lines are not of equal width.");

    }

    /**
     * Test for a file which doesn't exist.
     * Bad weather test
     */
    @Test
    void testNonExistingFile() {

        String mapName = "nonexistingfile";

        assertThrows(PacmanConfigurationException.class, () -> {
            this.mapParser.parseMap(mapName);
        }, "Could not get resource for: " + mapName);

    }

}


