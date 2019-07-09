package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @param <T>
 */
public abstract class AbstractCollisionTest<T extends CollisionMap> {


    private T playerCollisions;
    private PointCalculator pointCalculator = mock(PointCalculator.class);

    /**
     * A getter to have package access to point calculator.
     *
     * @return Returns a point calculator.
     */
    PointCalculator getPointCalculator() {
        return pointCalculator;
    }

    /**
     * Abstract method used by the child class to generate the CollisionMap instance.
     *
     * @return the instantiated object.
     */
    abstract T instantiateObject();

    /**
     * Setup before all tests.
     */
    @BeforeEach
    void setup() {

        this.playerCollisions = instantiateObject();
    }

    /**
     * Test for a collision between the player and a ghost.
     * Checks if the player is dead after the collision, and that is has been killed by that ghost.
     */
    @Test
    void collidePlayerWithGhost() {

        Player player = mock(Player.class);
        player.setAlive(true);

        Ghost ghost = mock(Ghost.class);


        this.playerCollisions.collide(player, ghost);

        verify(pointCalculator, times(1)).collidedWithAGhost(player, ghost);

        assertThat(player.isAlive()).isFalse();
        verify(player, times(1)).setKiller(ghost);

    }

    /**
     * Test for a collision between the player and a pellet .
     */
    @Test
    void collidePlayerWithPellet() {

        Player player = mock(Player.class);
        Pellet pellet = mock(Pellet.class);

        this.playerCollisions.collide(player, pellet);

        verify(pointCalculator, times(1)).consumedAPellet(player, pellet);
        verify(pellet, times(1)).leaveSquare();


    }


    /**
     * Test for a collision between the player and a ghost.
     * Checks if the player is dead after the collision, and that is has been killed by that ghost.
     */
    @Test
    void collideGhostWithPlayer() {

        Player player = mock(Player.class);
        player.setAlive(true);

        Ghost ghost = mock(Ghost.class);

        this.playerCollisions.collide(ghost, player);

        verify(pointCalculator, times(1)).collidedWithAGhost(player, ghost);

        assertThat(player.isAlive()).isFalse();
        verify(player, times(1)).setKiller(ghost);


    }

    /**
     * Test for a collision between a pellet and the player.
     */
    @Test
    void collidePelletWithPlayer() {

        Player player = mock(Player.class);
        Pellet pellet = mock(Pellet.class);

        this.playerCollisions.collide(pellet, player);

        verify(pointCalculator, times(1)).consumedAPellet(player, pellet);
        verify(pellet, times(1)).leaveSquare();


    }

    /**
     * Test for when nothing collides with nothing,
     * a pellet collides with nothing or a ghost collides with nothing.
     * Nothing should happen in this scenarios
     */
    @Test
    void nothingHappensTest() {

        Pellet pellet = mock(Pellet.class);
        Ghost ghost = mock(Ghost.class);

        Unit unitOne = mock(Unit.class);
        Unit unitTwo = mock(Unit.class);

        this.playerCollisions.collide(unitOne, unitTwo);
        this.playerCollisions.collide(pellet, unitOne);
        this.playerCollisions.collide(ghost, unitTwo);

        verify(pointCalculator, never()).consumedAPellet(any(), any());
        verify(pellet, never()).leaveSquare();

    }


}
