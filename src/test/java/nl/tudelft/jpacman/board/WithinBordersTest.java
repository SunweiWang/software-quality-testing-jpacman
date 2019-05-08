package nl.tudelft.jpacman.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Conducts a domain matrix test for the <code>withinBorders</code> method of {@link Board}.
 */
class WithinBordersTest {
    private static final int WIDTH = 6;
    private static final int HEIGHT = 4;

    private Board board;

    /**
     * Setup a board to be used for testing boundary conditions.
     */
    @BeforeEach
    void setup() {
        Square[][] grid = new Square[WIDTH][HEIGHT];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                grid[x][y] = new BasicSquare();
            }
        }
        board = new Board(grid);
    }

    /**
     * Test input data with test cases for on/off and in point for every
     * boundary value.
     *
     * @param x
     *          The x position.
     * @param y
     *          The y position.
     * @param expected
     *          The expected outcome.
     */
    @ParameterizedTest
    @CsvSource({
        "0, 2, true",
        "-1, 3, false",
        "6, 2, false",
        "5, 3, true",
        "1, 0, true",
        "2, -1, false",
        "3, 4, false",
        "4, 3, true"
    })
    void testWithinBorders(int x, int y, boolean expected) {
        assertThat(board.withinBorders(x, y)).isEqualTo(expected);
    }
}
