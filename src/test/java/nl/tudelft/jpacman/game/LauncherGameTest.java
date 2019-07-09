package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.Launcher;

/**
 * Test suit for the normal Launcher.
 */
public class LauncherGameTest extends AbstractGameStateTest {

    /**
     * Init the normal Launcher.
     *
     * @return Launcher
     */
    @Override
    public Launcher initLauncher() {

        return new Launcher();
    }
}
