package sk.tuke.gamestudio.minesweeper;

import sk.tuke.gamestudio.minesweeper.consoleui.ConsoleUI;
import sk.tuke.gamestudio.minesweeper.consoleui.UserInterface;

/**
 * Main application class.
 */
public class Minesweeper {
    /**
     * User interface.
     */
    private static long startTime;
    private static Minesweeper instance;

    /**
     * Constructor.
     */
    private Minesweeper() {
        instance = this;
        final UserInterface userInterface = new ConsoleUI();
        userInterface.play();
    }

    public static Minesweeper getInstance() {
        if (instance == null) {
            new Minesweeper();
        }
        return instance;
    }

    /**
     * Main method.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        startTime = System.currentTimeMillis();
        new Minesweeper();
        getPlayingSeconds();
    }

    public static int getPlayingSeconds() {
        return (int) ((System.currentTimeMillis() - startTime) / 1000);
    }

}
