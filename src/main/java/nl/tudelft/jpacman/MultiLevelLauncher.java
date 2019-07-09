package nl.tudelft.jpacman;


import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.game.MultiLevelGame;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.points.PointCalculatorLoader;

import java.io.IOException;
import java.util.ArrayList;

/**
 * MultiLevelLauncher class.
 */
public class MultiLevelLauncher extends Launcher {

    private MultiLevelGame multiGame;

    /**
     * Make a multiLevel Game.
     * @return The created game
     */
    @Override
    public Game makeGame() {

        ArrayList<Level> levels = new ArrayList<>();
        levels.add(makeLevel());
        levels.add(makeLevel());
        levels.add(makeLevel());

        multiGame = getGameFactory()
            .createMultiLevelGame(levels, new PointCalculatorLoader().load());
        return multiGame;
    }

    /**
     * Returns the current game.
     * @return the game
     */
    @Override
    public MultiLevelGame getGame() {
        return multiGame;
    }

    /**
     * New main method for playing with multiple levels.
     * @param args - Arguments
     * @throws IOException - Exception
     */
    public static void main(String[] args) throws IOException {
        new MultiLevelLauncher().launch();
    }
}
