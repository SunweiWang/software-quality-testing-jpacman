package nl.tudelft.jpacman.level;


/**
 * Map parser test.
 */
class DefaultPlayerInteractionMapTest extends AbstractCollisionTest<DefaultPlayerInteractionMap> {

    @Override
    DefaultPlayerInteractionMap instantiateObject() {

        return new DefaultPlayerInteractionMap(this.getPointCalculator());
    }
}


