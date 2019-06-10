package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;


/**
 * java doc.
 */
public class PlayerCollisionTest extends CollisionMapAbstractTests {
    private PointCalculator pointCalculator = new DefaultPointCalculator();

    /**
     * java doc.
     */
    public PlayerCollisionTest() {
        setCmap(new PlayerCollisions(pointCalculator));
    }
}
