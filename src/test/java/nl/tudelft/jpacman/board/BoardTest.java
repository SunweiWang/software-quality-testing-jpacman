package nl.tudelft.jpacman.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test of Board.
 */
public class BoardTest {

    private BasicSquare square = new BasicSquare();

    private final Square[][] grid = {
        {square}
    };

    private final Square[][] nullGrid = {
        {null}
    };

    /**
     * Test of creating board test.
     */
    @Test
    public void createBoardTest() {

        Board board = new Board(grid);
        assertEquals(1, board.getHeight());
        assertEquals(1, board.getHeight());

        assertEquals(square, board.squareAt(0, 0));
    }

    /**
     * Test of creating board test with null.
     */
    @Test
    public void createBoardTestWithNull() {

        try {

            new Board(nullGrid);
            fail();
        } catch (AssertionError e) {

            assertEquals("Initial grid cannot contain null squares", e.getMessage());
        }


    }
}
