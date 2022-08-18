package sk.tuke.gamestudio.minesweeper.core;

import java.io.Serializable;

/**
 * Game state.
 */
public enum GameState implements Serializable {
    /**
     * Playing game.
     */
    PLAYING,

    /**
     * Game failed.
     */
    FAILED,

    /**
     * Game solved.
     */
    SOLVED
}
