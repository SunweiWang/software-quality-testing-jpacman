package nl.tudelft.jpacman.board;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for the checkInBoundsMethod from class board.
 */
class BoundaryTest {
    private BasicSquare square = new BasicSquare();
    private final Square[][] grid = {
        {square, square, square, square, square},
        {square, square, square, square, square},
        {square, square, square, square, square},
        {square, square, square, square, square},
        {square, square, square, square, square}
        };

    /**
     * Checks for X AND Y to be in bounds.
     * @param arg1 X value.
     * @param arg2 Y value.
     * @param arg3 boolean value.
     */
    @ParameterizedTest
    @CsvSource({
        "0, 1, true",
        "-1, 1, false",
        "5, 1, false",
        "4, 1, true",
        "1, 0, true",
        "1, -1, false",
        "1, 5, false",
        "1, 4, true",

    })
    void checkInBoundsBoth(final int arg1, final int arg2, final boolean arg3) {
        Board board = new Board(grid);
        assertThat(board.withinBorders(arg1, arg2)).isEqualTo(arg3);

    }

}

