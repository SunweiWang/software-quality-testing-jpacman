package nl.tudelft.jpacman.level;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import nl.tudelft.jpacman.npc.Ghost;
import org.junit.jupiter.api.Test;
/**
 * Tests that are common to all collision map implementations.
 */
public abstract class CollisionMapAbstractTests {
    private Player player = mock(Player.class);
    private Ghost ghost = mock(Ghost.class);
    private Pellet pellet = mock(Pellet.class);
    private CollisionMap cmap;
    /**
     * R1: A player bumps into a ghost.
     * The player should die.
     */
    @Test
    public void testPlayerGhost() {
        getCmap().collide(player, ghost);
        verify(player).setAlive(false);
        verifyZeroInteractions(ghost);
        verifyZeroInteractions(pellet);
    }
    /**
     * R2: A ghost bumps into a player.
     * The player dies.
     */
    @Test
    public void testGhostPlayer() {
        getCmap().collide(ghost, player);
        verify(player).setAlive(false);
        verifyZeroInteractions(ghost);
        verifyZeroInteractions(pellet);
    }

    /**
     * R3: A player bumps into a pellet.
     * The player should eat it.
     */
    @Test
    public void testPlayerPellet() {
        final int pointsEaten = 42;
        when(pellet.getValue()).thenReturn(pointsEaten);
        getCmap().collide(player, pellet);
        verify(player).addPoints(pointsEaten);
        verify(pellet).leaveSquare();
        verifyZeroInteractions(ghost);
    }
    /**
     * R4: A ghost bumps into a pellet.
     * No measurable effects.
     */
    @Test
    public void testGhostNonPlayer() {
        getCmap().collide(ghost, pellet);
        verifyZeroInteractions(ghost);
        verifyZeroInteractions(player);
        verifyZeroInteractions(pellet);
    }
    /**
     * R5: A ghost bumps into another ghost.
     * No measurable effects.
     */
    @Test
    public void testGhostGhost() {
        Ghost otherGhost = mock(Ghost.class);
        getCmap().collide(ghost, otherGhost);
        verifyZeroInteractions(ghost);
        verifyZeroInteractions(otherGhost);
    }

    /**
     * Two pellets collide. Fairly hypothetical.
     * OK if this is not tested, as it also does not follow from
     * the decision table.
     */
    @Test
    public void movingNonMovers() {
        getCmap().collide(pellet, pellet);
        verifyZeroInteractions(player);
        verifyZeroInteractions(ghost);
        verifyZeroInteractions(pellet);
    }
    /**
     * A pellet bumps into the player. Fairly hypothetical.
     * OK if this is not tested, as it also does not follow from
     * the decision table.
     */
    @Test
    public void nonMovingCollider() {
        final int pointsEaten = 42;
        when(pellet.getValue()).thenReturn(pointsEaten);
        getCmap().collide(pellet, player);
        verify(player).addPoints(pointsEaten);
        verify(pellet).leaveSquare();
        verifyZeroInteractions(ghost);
    }
    /**
     * The collision map under test.
     * @return The collision map under test.
     */
    protected CollisionMap getCmap() {
        return cmap;
    }

    /**
     * The setter.
     * @param cmap The collision map
     */
    protected void setCmap(CollisionMap cmap) {
        this.cmap = cmap;
    }
}

