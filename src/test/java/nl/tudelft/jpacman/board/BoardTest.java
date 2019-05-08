package nl.tudelft.jpacman.board;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests the class invariant of {@link Board}.
 */
class BoardTest {
    /**
     * Tests the construction of a regular small board.
     */
    @Test
    void testBoardConstruction() {
        Square square = new BasicSquare();

        Board board = new Board(new Square[][] {{square}});

        assertThat(board.squareAt(0, 0)).isEqualTo(square);
    }

    /**
     * Tests the construction of a board with a grid containing <code>null</code>
     * instead of a Square.
     *
     * If assertions are enabled, this should result in an assertion error.
     */
    @Test
    void testBoardConstructionWithNullSquareShouldThrowAssertionError() {
        assumeTrue(assertionsEnabled());

        assertThatThrownBy(() -> new Board(new Square[][] {{null}}))
            .isInstanceOf(AssertionError.class);
    }

    /**
     * Checks if assertions are enabled.
     *
     * @return true iff assertions are enabled
     */
    static boolean assertionsEnabled() {
        return BoardTest.class.desiredAssertionStatus();
    }
}
