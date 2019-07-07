package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;


/**
 * Test suite for the level.MapParser class.
 * We test the different behaviours related to calling different factories.
 */
class MapParserTest {

    private BoardFactory boardFactory;

    private LevelFactory levelFactory;

    private MapParser mapParser;

    private Square square;

    private Pellet pellet;

    private Ghost ghost;

    private List<String> levelMap;

    private static final int SIZE = 9;

    /**
     * Sets up the mocked classes and the methods that need to be verified if called.
     */
    @BeforeEach
    void setUp() {
        levelFactory = Mockito.mock(LevelFactory.class);
        boardFactory = Mockito.mock(BoardFactory.class);
        pellet = Mockito.mock(Pellet.class);
        square = Mockito.mock(Square.class);
        ghost = Mockito.mock(Ghost.class);
        Mockito.when(boardFactory.createGround()).thenReturn(square);
        Mockito.doNothing().when(pellet).occupy(square);
        Mockito.when(levelFactory.createPellet()).thenReturn(pellet);
        Mockito.doNothing().when(ghost).occupy(square);
        Mockito.when(levelFactory.createGhost()).thenReturn(ghost);
        mapParser = new MapParser(levelFactory, boardFactory);
        levelMap = new ArrayList<>();
    }

    /**
     * Tests a level map with only empty spaces.
     */
    @Test
    void emptySpacesTest() {
        levelMap.add("   ");
        levelMap.add("   ");
        levelMap.add("   ");
        mapParser.parseMap(levelMap);
        Mockito.verify(boardFactory, times(SIZE)).createGround();
    }

    /**
     * Tests a level map only with walls.
     */
    @Test
    void wallsOnlyTest() {
        levelMap.add("###");
        levelMap.add("###");
        levelMap.add("###");
        mapParser.parseMap(levelMap);
        Mockito.verify(boardFactory, times(SIZE)).createWall();
    }

    /**
     * Tests a level map only with pellets.
     */
    @Test
    void pelletsOnlyTest() {
        levelMap.add("...");
        levelMap.add("...");
        levelMap.add("...");
        mapParser.parseMap(levelMap);
        Mockito.verify(boardFactory, times(SIZE)).createGround();
        Mockito.verify(levelFactory, times(SIZE)).createPellet();
    }

    /**
     * Tests a level map with only a ghost.
     */
    @Test
    void ghostsOnlyTest() {
        levelMap.add("G");
        mapParser.parseMap(levelMap);
        Mockito.verify(boardFactory, times(1)).createGround();
    }

    /**
     * Tests a level map with only a player.
     */
    @Test
    void playerOnlyTest() {
        levelMap.add("P");
        Mockito.when(boardFactory.createGround()).thenReturn(square);
        mapParser.parseMap(levelMap);
        Mockito.verify(boardFactory, times(1)).createGround();
    }

    /**
     * Combined normal behaviour test of all of the factories needed to be mocked.
     * Contains all possible good weather cases.
     */
    @Test
    @SuppressWarnings("checkstyle:magicnumber"
        // the test contains non generic numbers specific to this test
    )
    void combinedNormalBehaviourTest() {
        levelMap.add("G..");
        levelMap.add(" P ");
        levelMap.add("###");
        mapParser.parseMap(levelMap);
        Mockito.verify(boardFactory, times(6)).createGround();
        Mockito.verify(levelFactory, times(2)).createPellet();
        Mockito.verify(boardFactory, times(3)).createWall();
        Mockito.verify(levelFactory, times(1)).createGhost();
    }

    /**
     * Test for parsing a text file with normal behaviour.
     *
     * @throws IOException the io exception
     */
    @Test
    @SuppressWarnings("checkstyle:magicnumber"
        // the test contains non generic numbers specific to this test
    )
    void textFileParseMapTest() throws IOException {
        mapParser.parseMap("/testmap.txt");
        Mockito.verify(boardFactory, times(4)).createGround();
        Mockito.verify(levelFactory, times(1)).createPellet();
        Mockito.verify(boardFactory, times(12)).createWall();
        Mockito.verify(levelFactory, times(1)).createGhost();
    }

    /**
     * Test for parsing an inputstream with a map with normal behaviour.
     *
     * @throws IOException the io exception
     */
    @Test
    @SuppressWarnings("checkstyle:magicnumber"
        // the test contains non generic numbers specific to this test
    )
    void inputStreamParseMapTest() throws IOException {
        try (InputStream source = new FileInputStream("./src/test/resources/testmap.txt")) {
            mapParser.parseMap(source);
            Mockito.verify(boardFactory, times(4)).createGround();
            Mockito.verify(levelFactory, times(1)).createPellet();
            Mockito.verify(boardFactory, times(12)).createWall();
            Mockito.verify(levelFactory, times(1)).createGhost();
        }
    }

    /**
     * Test for PacmanConfigurationException when an invalid character
     * is given.
     */
    @Test
    void invalidCharacterTest() {
        levelMap.add("C");
        assertThrows(PacmanConfigurationException.class, () -> mapParser.parseMap(levelMap));
    }

    /**
     * Test for PacmanConfigurationException when trying to parse non-existent file.
     */
    @Test
    void textFileExceptionTest() {
        String map = "No-file.txt";
        PacmanConfigurationException thrown =  assertThrows(PacmanConfigurationException.class,
            () -> mapParser.parseMap(map));
        assertThat(thrown.getMessage()).contains("Could not get resource for: " + map);
    }

    /**
     * Test for PacmanConfigurationException when map is null.
     */
    @Test
    void mapIsNullTest() {
        levelMap = null;
        PacmanConfigurationException thrown = assertThrows(PacmanConfigurationException.class,
            () -> mapParser.parseMap(levelMap));
        assertThat(thrown.getMessage()).contains("Input text cannot be null.");
    }

    /**
     * Test for PacmanConfigurationException when map is empty.
     */
    @Test
    void mapIsEmptyTest() {
        PacmanConfigurationException thrown = assertThrows(PacmanConfigurationException.class,
            () -> mapParser.parseMap(levelMap));
        assertThat(thrown.getMessage()).contains("Input text must consist of at least 1 row.");
    }

    /**
     * Test for PacmanConfigurationException when the line is empty.
     */
    @Test
    void lineIsEmptyTest() {
        levelMap.add("");
        PacmanConfigurationException thrown = assertThrows(PacmanConfigurationException.class,
            () -> mapParser.parseMap(levelMap));
        assertThat(thrown.getMessage()).contains("Input text lines cannot be empty.");
    }

    /**
     * Test for PacmanConfigurationException when the widths of two lines are not equal.
     */
    @Test
    void widthNotEqualTest() {
        levelMap.add("##");
        levelMap.add("#");
        PacmanConfigurationException thrown = assertThrows(PacmanConfigurationException.class,
            () -> mapParser.parseMap(levelMap));
        assertThat(thrown.getMessage()).contains("Input text lines are not of equal width.");
    }

}
