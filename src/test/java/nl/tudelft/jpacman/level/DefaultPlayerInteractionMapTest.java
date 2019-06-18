package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;


/**
 * java doc.
 */
public class DefaultPlayerInteractionMapTest extends CollisionMapAbstractTests {
    private PointCalculator pointCalculator = new DefaultPointCalculator();

    /**
     * java doc.
     */
    public DefaultPlayerInteractionMapTest() {
        setCmap(new DefaultPlayerInteractionMap(pointCalculator));
    }
}
