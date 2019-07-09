package nl.tudelft.jpacman.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite to confirm that {@link Unit}s correctly (de)occupy squares.
 *
 * @author Jeroen Roosen
 */
class OccupantTest {

    /**
     * The unit under test.
     */
    private Unit unit;

    /**
     * Resets the unit under test.
     */
    @BeforeEach
    void setUp() {
        unit = new BasicUnit();
    }

    /**
     * Asserts that a unit has no square to start with.
     */
    @Test
    void noStartSquare() {
        // Remove the following placeholder:
        assertThat(unit.hasSquare()).isFalse();
    }

    /**
     * Tests that the unit indeed has the target square as its base after
     * occupation.
     */
    @Test
    void testOccupy() {
        Square sq = new BasicSquare();
        unit.occupy(sq);
        // Remove the following placeholder:
        assertEquals(sq, unit.getSquare());
        assertEquals(1, sq.getOccupants().size());
        assertEquals(unit, sq.getOccupants().get(0));

    }

    /**
     * Test that the unit indeed has the target square as its base after
     * double occupation.
     */
    @Test
    void testReoccupy() {
        Square sq = new BasicSquare();
        Square sq2 = new BasicSquare();
        unit.occupy(sq);
        unit.occupy(sq2);
        // Remove the following placeholder:
        assertEquals(sq2, unit.getSquare());
        assertEquals(0, sq.getOccupants().size());
        assertEquals(1, sq2.getOccupants().size());
        assertEquals(unit, sq2.getOccupants().get(0));
    }
}
