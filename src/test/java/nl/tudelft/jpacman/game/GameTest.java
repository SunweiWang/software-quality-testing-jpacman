package nl.tudelft.jpacman.game;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.MultiLevelLauncher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

/**
 * Test class that conducts integration tests on states of JPacMan.
 */
class GameTest {

    private Level.LevelObserver levelObserver;

    /**
     * Provides a stream of launchers.
     *
     * @return the stream
     */
    static Stream<Arguments> provideImplementors() {
        return Stream.of(
            Arguments.of(new Launcher()),
            Arguments.of(new MultiLevelLauncher())
        );
    }

    /**
     * Sets up.
     */
    @BeforeEach
    void setup() {
        levelObserver = Mockito.mock(Level.LevelObserver.class);
    }

    /**
     * Testing that once the user uses the start button again, the game has no more interactions
     * and does not change its state to neither won nor lost nor paused via .stop() method.
     * We bring the game in the inProgress state. Then we press start and check whether
     * the game is still in the same state and nothing happens.
     *
     * @param launcher the launcher
     */
    @SuppressWarnings("checkstyle:magicnumber"
        // the test contains non generic numbers specific to the amount of times regarding mockito.
    )
    @SuppressFBWarnings(
        value = "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"
    )
    @ParameterizedTest
    @MethodSource("provideImplementors")
    void inProgressStateSneakPathTest(Launcher launcher) {
        Launcher launcherTemp = launcher.withMapFile("/gametest.txt");
        launcherTemp.launch();
        Game game = launcher.getGame();
        game.getLevel().addObserver(levelObserver);

        game.start();

        assertThat(game.isInProgress()).isEqualTo(true);

        game.start();

        assertThat(game.isInProgress()).isEqualTo(true);

        Mockito.verify(levelObserver,   times(0)).levelLost();
        Mockito.verify(levelObserver,   times(0)).levelWon();

        if (launcher.getClass() == Launcher.class) {
            launcher.dispose();
        }
    }

    /**
     * Testing that once the user uses the stop button again, the game has no more interactions
     * and does not change its state to neither won nor lost nor inProgress.
     * We start the game then stop and check whether the game is still in the same state and
     * nothing happens.
     * Afterwards we try to move to player and check if it doesn't move and therefore
     * making sure that the last pellet cannot be eaten and the player cannot collide
     * with the ghost or vice versa.
     * We make the assumption in this scenario that when the player cannot move,
     * the ghost also cannot move.
     * On top of that we check whether the levelWon() or levelLost() from levelObserver()
     * have been called to make sure it cannot go to either of those states.
     *
     * @param launcher the launcher
     */
    @SuppressWarnings({"checkstyle:magicnumber", "methodlength"}
        // the test contains non generic numbers specific to the amount of times regarding mockito.
    )
    @SuppressFBWarnings(
        value = "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"
    )
    @ParameterizedTest
    @MethodSource("provideImplementors")
    void pausedStateSneakPathsTest(Launcher launcher) {
        Launcher launcherTemp = launcher.withMapFile("/gametest.txt");
        launcherTemp.launch();
        Game game = launcher.getGame();
        game.getLevel().addObserver(levelObserver);
        Player player = game.getPlayers().get(0);

        game.start();
        game.stop();

        assertThat(game.isInProgress()).isEqualTo(false);

        game.stop();

        assertThat(game.isInProgress()).isEqualTo(false);

        game.move(player, Direction.EAST);
        game.move(player, Direction.EAST);

        Mockito.verify(levelObserver,   times(0)).levelWon();
        assertThat(game.getLevel().getBoard().squareAt(2, 1)
            .getOccupants().get(0)).isEqualTo(player);

        game.move(player, Direction.WEST);

        Mockito.verify(levelObserver,   times(0)).levelLost();

        if (launcher.getClass() == Launcher.class) {
            launcher.dispose();
        }
    }

    /**
     * In this test we bring the game in the won state.
     * We start the game, then stop and test in between and after whether the state doesn't change
     * and nothing happens.
     * We also try to move the player and make sure that it cannot make any moves.
     * We try to move the player to where the last pellet may be and where the ghost is and then
     * check with levelObserver() whether the levelWon() or levelLost() methods have been called.
     *
     */
    @SuppressWarnings({"checkstyle:magicnumber", "methodlength"}
        // the test contains non generic numbers specific to the amount of times regarding mockito.
    )
    @SuppressFBWarnings(
        value = "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"
    )
    @Test
    void wonStateSneakPathsTest() {
        Launcher launcherTemp = new Launcher().withMapFile("/gametest.txt");
        launcherTemp.launch();
        Game game = launcherTemp.getGame();
        game.getLevel().addObserver(levelObserver);
        Player player = game.getPlayers().get(0);

        game.start();
        game.move(player, Direction.EAST);
        game.move(player, Direction.EAST);

        assertThat(game.isInProgress()).isEqualTo(false);
        Mockito.verify(levelObserver, times(1)).levelWon();

        game.start();

        assertThat(game.isInProgress()).isEqualTo(false);

        game.stop();

        assertThat(game.isInProgress()).isEqualTo(false);

        game.move(player, Direction.EAST);

        Mockito.verify(levelObserver, times(1)).levelWon();

        game.move(player, Direction.WEST);
        game.move(player, Direction.WEST);
        game.move(player, Direction.WEST);

        Mockito.verify(levelObserver,   times(0)).levelLost();
        assertThat(game.getLevel().getBoard().squareAt(4, 1)
            .getOccupants().get(0)).isEqualTo(player);

        if (launcherTemp.getClass() == Launcher.class) {
            launcherTemp.dispose();
        }
    }

    /**
     * In this test case, we bring the game in the lost state.
     * We try to start and stop the game and check if the state doesn't change and nothing happens.
     * We try to move the player and and then check with levelObserver()
     * whether the levelWon() or levelLost() methods have been called.
     *
     * @param launcher the launcher
     */
    @SuppressWarnings({"checkstyle:magicnumber", "methodlength"}
        // the test contains non generic numbers specific to the amount of times regarding mockito.
    )
    @SuppressFBWarnings(
        value = "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"
    )
    @ParameterizedTest
    @MethodSource("provideImplementors")
    void lostStateSneakPathsTest(Launcher launcher) {
        Launcher launcherTemp = launcher.withMapFile("/gametest.txt");
        launcherTemp.launch();
        Game game = launcher.getGame();
        game.getLevel().addObserver(levelObserver);
        Player player = game.getPlayers().get(0);

        game.start();
        game.move(player, Direction.WEST);

        assertThat(game.isInProgress()).isEqualTo(false);
        Mockito.verify(levelObserver, times(1)).levelLost();

        game.start();

        assertThat(game.isInProgress()).isEqualTo(false);

        game.stop();

        assertThat(game.isInProgress()).isEqualTo(false);

        game.move(player, Direction.EAST);
        game.move(player, Direction.EAST);
        game.move(player, Direction.EAST);

        Mockito.verify(levelObserver, times(0)).levelWon();

        game.move(player, Direction.WEST);

        Mockito.verify(levelObserver, times(1)).levelLost();

        if (launcher.getClass() == Launcher.class) {
            launcher.dispose();
        }
    }

    /**
     * Test case for winning state.
     * We are verifying that we cannot transition to any other state after fully winning the game.
     */
    @SuppressWarnings({"checkstyle:magicnumber", "methodlength"}
        // the test contains non generic numbers specific to the amount of times regarding mockito.
    )
    @Test
    void wonLevelStateTest() {
        Launcher launcherTemp = new MultiLevelLauncher().withMapFile("/gametest.txt");
        launcherTemp.launch();
        Game game = launcherTemp.getGame();
        Player player = game.getPlayers().get(0);

        for (int i = 0; i < 4; i++) {
            game.start();
            game.getLevel().addObserver(levelObserver);
            assertThat(game.isInProgress()).isEqualTo(true);
            game.move(player, Direction.EAST);
            game.move(player, Direction.EAST);
            assertThat(game.isInProgress()).isEqualTo(false);
        }
        Mockito.verify(levelObserver, times(4)).levelWon();
        game.start();

        assertThat(game.isInProgress()).isEqualTo(false);

        game.stop();

        assertThat(game.isInProgress()).isEqualTo(false);

        game.move(player, Direction.EAST);

        Mockito.verify(levelObserver, times(4)).levelWon();

        game.move(player, Direction.WEST);
        game.move(player, Direction.WEST);
        game.move(player, Direction.WEST);

        Mockito.verify(levelObserver,   times(0)).levelLost();
        assertThat(game.getLevel().getBoard().squareAt(4, 1)
            .getOccupants().get(0)).isEqualTo(player);

    }

    /**
     * Test case for lost level state
     * We are verifying that we are able to lose the game after a couple of wins and that
     * we cannot transition to any other state afterwards.
     */
    @SuppressWarnings({"checkstyle:magicnumber", "methodlength"}
        // the test contains non generic numbers specific to the amount of times regarding mockito.
    )
    @Test
    void losingAfterWinningStateTest() {
        Launcher launcherTemp = new MultiLevelLauncher().withMapFile("/gametest.txt");
        launcherTemp.launch();
        Game game = launcherTemp.getGame();
        Player player = game.getPlayers().get(0);

        for (int i = 0; i < 3; i++) {
            game.start();
            game.getLevel().addObserver(levelObserver);
            assertThat(game.isInProgress()).isEqualTo(true);
            game.move(player, Direction.EAST);
            game.move(player, Direction.EAST);
            assertThat(game.isInProgress()).isEqualTo(false);
        }
        Mockito.verify(levelObserver, times(3)).levelWon();
        game.start();
        game.getLevel().addObserver(levelObserver);
        assertThat(game.isInProgress()).isEqualTo(true);
        game.move(player, Direction.WEST);

        Mockito.verify(levelObserver,   times(1)).levelLost();

        game.start();

        assertThat(game.isInProgress()).isEqualTo(false);

        game.stop();

        assertThat(game.isInProgress()).isEqualTo(false);

    }

    /**
     * Test case for inter level state.
     * We are verifying that we cannot transition to any other state after winning one level and
     * that there are no possibilities of changing state besides game.start() which will start
     * the next level.
     */
    @SuppressWarnings({"checkstyle:magicnumber", "methodlength"}
        // the test contains non generic numbers specific to the amount of times regarding mockito.
    )
    @Test
    void interLevelStateTest() {
        Launcher launcherTemp = new MultiLevelLauncher().withMapFile("/gametest.txt");
        launcherTemp.launch();
        Game game = launcherTemp.getGame();
        Player player = game.getPlayers().get(0);
        game.start();
        game.getLevel().addObserver(levelObserver);
        assertThat(game.isInProgress()).isEqualTo(true);
        game.move(player, Direction.EAST);
        game.move(player, Direction.EAST);
        assertThat(game.isInProgress()).isEqualTo(false);

        Mockito.verify(levelObserver, times(1)).levelWon();

        game.stop();

        assertThat(game.isInProgress()).isEqualTo(false);

        game.move(player, Direction.EAST);
        game.move(player, Direction.EAST);

        Mockito.verify(levelObserver, times(1)).levelWon();

        game.move(player, Direction.WEST);

        Mockito.verify(levelObserver,   times(0)).levelLost();

    }
}
