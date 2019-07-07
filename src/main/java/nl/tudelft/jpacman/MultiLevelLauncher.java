package nl.tudelft.jpacman;

import nl.tudelft.jpacman.game.MultiLevelGame;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.points.PointCalculatorLoader;
import nl.tudelft.jpacman.ui.PacManUI;
import nl.tudelft.jpacman.ui.PacManUiBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The multi level game launcher.
 */
public class MultiLevelLauncher extends Launcher {

    private MultiLevelGame multiGame;

    private PacManUI pacManUI;

    private static final int MAX_GAMES = 4;

    @Override
    public MultiLevelGame getGame() {
        return multiGame;
    }

    @Override
    public MultiLevelGame makeGame() {
        List<Level> list = new ArrayList<>();
        for (int i = 0; i < MAX_GAMES; i++) {
            list.add(makeLevel());
        }
        PlayerFactory pf = getPlayerFactory();
        multiGame = new MultiLevelGame(pf.createPacMan(), list, loadPointCalculator());
        return multiGame;
    }

    /**
     * Creates and starts a JPac-Man game.
     */
    @Override
    public void launch() {
        makeGame();
        PacManUiBuilder builder = new PacManUiBuilder().withDefaultButtons();
        addSinglePlayerKeys(builder);
        pacManUI = builder.build(getGame());
        pacManUI.start();
    }

    private PointCalculator loadPointCalculator() {
        return new PointCalculatorLoader().load();
    }

    /**
     * Main execution method for the Launcher.
     * Executes method launch.
     *
     * @param args The command line arguments - which are ignored.
     * @throws IOException When a resource could not be read.
     */
    public static void main(String[] args) throws IOException {
        new MultiLevelLauncher().launch();
    }
}
