package nl.tudelft.jpacman.board;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The type Board test.
 */
class BoardTest {

    private Square square;

    private Board board;

    private static final int SIZE = 10;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        square = new BasicSquare();
        Square[][] grid = new Square[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = square;
            }
        }
        board = new Board(grid);
    }

    /**
     * Basic grid test.
     */
    @Test
    void basicGridTest() {
        Square[][] grid = {{square}};
        Board board = new Board(grid);
        assertEquals(board.squareAt(0, 0), square);
    }

    /**
     * Testing if the point P(x,y) is within the area the board or is not,
     * depending on the validity which is the expected result.
     *
     * @param x the x
     * @param y the y
     * @param validity the expected result
     */
    @ParameterizedTest
    @CsvSource({
        "0 , 3, true",
        "-1 , 4, false",
        "9 , 5, true",
        "10 , 6, false",
        "4 , 0, true",
        "5 , -1, false",
        "6 , 9, true",
        "7 , 10, false"
    })
    void withinBordersTrue(int x, int y, boolean validity) {
        assertThat(board.withinBorders(x, y)).isEqualTo(validity);
    }

}
