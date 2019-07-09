package nl.tudelft.jpacman.level;


/**
 * Map parser test.
 */
class PlayerCollisionsTest extends AbstractCollisionTest<PlayerCollisions> {

    @Override
    PlayerCollisions instantiateObject() {

        return new PlayerCollisions(this.getPointCalculator());
    }
}


