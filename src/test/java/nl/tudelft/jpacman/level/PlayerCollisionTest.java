package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.points.PointCalculator;

import static org.mockito.Mockito.mock;


/**
 * javdoc.
 */
public class PlayerCollisionTest extends CollisionMapAbstractTests {
    /**
     * javaodc.
     */
    public PlayerCollisionTest() {
        setCmap(new PlayerCollisions(mock(PointCalculator.class)));
    }
}
