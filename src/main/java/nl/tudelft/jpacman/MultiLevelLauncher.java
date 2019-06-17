package nl.tudelft.jpacman;
import nl.tudelft.jpacman.game.MultiLevelSinglePlayerGame;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.ui.PacManUiBuilder;
import nl.tudelft.jpacman.ui.ScorePanel;
import nl.tudelft.jpacman.ui.ScorePanel.ScoreFormatter;
/**
 * A launcher that creates a game with a single
 * player and 3 levels.
 *
 */
public class MultiLevelLauncher extends Launcher {
    private MultiLevelSinglePlayerGame multiGame;

    @Override
    public MultiLevelSinglePlayerGame getGame() {
        return multiGame;
    }

    @Override
    public MultiLevelSinglePlayerGame makeGame() {
        Player player = getPlayerFactory().createPacMan();
        multiGame = new MultiLevelSinglePlayerGame(player, new DefaultPointCalculator());
        addLevels();
        multiGame.getLevel().registerPlayer(player);
        return multiGame;
    }

    /**
     * Set the name of the file containing this level's map.
     *
     * @param fileName
     *            Map to be used.
     * @return Level corresponding to the given map.
     */
    @Override
    public MultiLevelLauncher withMapFile(String fileName) {
        setLevelMap(fileName);
        return this;
    }


    /**
     * Add a series of levels to this multi-level game.
     */
    public void addLevels() {
// Just add the same level three times.
        addLevel(makeLevel());
        addLevel(makeLevel());
        addLevel(makeLevel());
    }
    /**
     * Add one particular level to this game.
     * @param l The level to bye added.
     */
    public void addLevel(Level l) {
        multiGame.addLevel(l);
    }
    /**
     * Creates and starts a JPac-Man game.
     */
    @Override
    public void launch() {
        multiGame = makeGame();
// This could be a lambda, but those cause cobertura to crash ...
        ScoreFormatter sf = new ScoreFormatter() {
            public String format(Player p) {
                String score = ScorePanel.DEFAULT_SCORE_FORMATTER.format(p);
                score += String.format("; Steps: %3d", multiGame.getMoveCount());
                return score;
            }
        };
        PacManUiBuilder builder = new PacManUiBuilder()
            .withDefaultButtons()
            .withScoreFormatter(sf);
//        addSinglePlayerKeys(builder, multiGame);
        addSinglePlayerKeys(builder);
        builder.build(multiGame).start();
    }

    /**
     * Create a new multi-level game and launch it.
     * @param arg All arguments are ignored.
     */
    public static void main(String[] arg) {
        MultiLevelLauncher l = new MultiLevelLauncher();
        l.launch();
    }
}
