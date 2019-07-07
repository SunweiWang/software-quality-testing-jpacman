package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.AnimatedSprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

/**
 * Test suite for class PlayerCollisions and DefaultPlayerInteractionMap.
 */
class PlayerCollisionsTest {

    private static PointCalculator pointCalculator = new DefaultPointCalculator();

    private Player player;

    private Ghost ghost;

    private Pellet pellet;

    private static final int NUMBER = 10;


    /**
     * Sets up all the objects and mocks necessary for the tests.
     */
    @BeforeEach
    void setUp() {
        AnimatedSprite animatedSprite = Mockito.mock(AnimatedSprite.class);
        player = new Player(null, animatedSprite);
        ghost = Mockito.mock(Ghost.class);
        pellet = Mockito.mock(Pellet.class);
        Mockito.when(pellet.getValue()).thenReturn(NUMBER);
        Mockito.doNothing().when(animatedSprite).restart();
    }

    /**
     * Provides a stream of two classes, both having the same method that we test.
     *
     * @return the implementation of CollisionMap
     */
    static Stream<Arguments> provideImplementors() {
        return Stream.of(
            Arguments.of(new DefaultPlayerInteractionMap(pointCalculator)),
            Arguments.of(new PlayerCollisions(pointCalculator))
        );
    }


    /**
     * Test for player colliding with pellet
     * We check if the player has gained points and the method leaveSquare
     * has been invoked to make sure everything worked correctly.
     *
     * @param map CollisionMap that can be either DefaultPlayerInteractionMap or PlayerCollisions
     */
    @ParameterizedTest
    @MethodSource("provideImplementors")
    void playerCollidePelletTest(CollisionMap map) {
        map.collide(player, pellet);
        assertThat(player.getScore()).isEqualTo(NUMBER);
        Mockito.verify(pellet, times(1)).leaveSquare();
    }

    /**
     * Test for player colliding with ghost
     * We check if the player is not alive anymore and its killer is the ghost
     * it collided with.
     *
     * @param map CollisionMap that can be either DefaultPlayerInteractionMap or PlayerCollisions
     */
    @ParameterizedTest
    @MethodSource("provideImplementors")
    void playerCollideGhostTest(CollisionMap map) {
        map.collide(player, ghost);
        assertThat(player.isAlive()).isFalse();
        assertThat(player.getKiller()).isEqualTo(ghost);
    }

    /**
     * Test for ghost colliding with pellet
     * In this case nothing should happen so we want to make sure that the if statement
     * on line 56 in PlayerCollisions turns out false and the statement player.setAlive(false);
     * on line 78 never is reached. Therefor we check if the player is still alive.
     *
     * @param map CollisionMap that can be either DefaultPlayerInteractionMap or PlayerCollisions
     */
    @ParameterizedTest
    @MethodSource("provideImplementors")
    void ghostCollidePelletTest(CollisionMap map) {
        map.collide(ghost, pellet);
        assertThat(player.isAlive()).isTrue();
    }

    /**
     * Test for ghost colliding with player
     * We check if the player is not alive anymore and its killer is the ghost
     * it collided with.
     *
     * @param map CollisionMap that can be either DefaultPlayerInteractionMap or PlayerCollisions
     */
    @ParameterizedTest
    @MethodSource("provideImplementors")
    void ghostCollidePlayerTest(CollisionMap map) {
        map.collide(ghost, player);
        assertThat(player.isAlive()).isFalse();
        assertThat(player.getKiller()).isEqualTo(ghost);
    }

    /**
     * Test for player colliding with pellet
     * We check if the player has gained points and the method leaveSquare
     * has been invoked to make sure everything worked correctly.
     *
     * @param map CollisionMap that can be either DefaultPlayerInteractionMap or PlayerCollisions
     */
    @ParameterizedTest
    @MethodSource("provideImplementors")
    void pelletCollidePlayerTest(CollisionMap map) {
        map.collide(pellet, player);
        assertThat(player.getScore()).isEqualTo(NUMBER);
        Mockito.verify(pellet, times(1)).leaveSquare();
    }

    /**
     * Test for pellet colliding with ghost
     * In this case nothing should happen so we want to make sure that the if statement
     * on line 62 in PlayerCollisions turns out false and the statement
     * pointCalculator.consumedAPellet(player, pellet) on line 78 never is reached.
     * Therefor we check if the player didn't get any points.
     *
     * @param map CollisionMap that can be either DefaultPlayerInteractionMap or PlayerCollisions
     */
    @ParameterizedTest
    @MethodSource("provideImplementors")
    void pelletCollideGhostTest(CollisionMap map) {
        map.collide(pellet, ghost);
        assertThat(player.getScore()).isEqualTo(0);
    }

}
