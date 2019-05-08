package nl.tudelft.jpacman.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;




public class BoardTest {

    private Square s1;
    private Board b1;

    /**
     * Setup of the boardtests.
     */
    @BeforeEach
    public void setUp() {
        s1 = new BasicSquare();
    }

    /**
     * Check whether the constructor properly creates a board when supplied with proper arguments.
     */
    @Test
    public void nonNullBoardTest() {
        b1 = new Board(new Square[][]{{s1}});
        assertThat(b1.squareAt(0, 0)).isEqualTo(s1);
    }


}
