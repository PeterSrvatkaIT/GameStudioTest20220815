package sk.tuke.gamestudio.minesweeper.core;

import java.io.Serializable;
import java.util.Random;

/**
 * Field represents playing field and game logic.
 */
public class Field implements Serializable {
    /**
     * Playing field tiles.
     */
    private final Tile[][] tiles;

    /**
     * Field row count. Rows are indexed from 0 to (rowCount - 1).
     */
    private final int rowCount;

    /**
     * Column count. Columns are indexed from 0 to (columnCount - 1).
     */
    private final int columnCount;

    /**
     * Mine count.
     */
    private final int mineCount;

    /**
     * Game state.
     */
    private GameState state = GameState.PLAYING;
    private long startMillis;

    /**
     * Constructor.
     *
     * @param rowCount    row count
     * @param columnCount column count
     * @param mineCount   mine count
     */
    public Field(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        tiles = new Tile[rowCount][columnCount];

        //generate the field content
        generate();

    }

    /**
     * Opens tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void openTile(int row, int column) {
        Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.OPEN);
            if (tile instanceof Mine) {
                state = GameState.FAILED;
                return;
            }
            if (tile instanceof Clue) {
                Clue c = (Clue) tile;
                if (c.getValue() == 0)
                    openAdjacentTiles(row, column);
            }

            if (isSolved()) {
                state = GameState.SOLVED;
            }
        }
    }

    /**
     * Marks tile at specified indeces.
     * <p>
     * //     * @param row    row number
     * //     * @param column column number
     */
    public void markTile(int rowCount, int columnCount) {
        if (state == GameState.PLAYING) {
            var tile = getTile(rowCount, columnCount);
            if (tile.getState() == Tile.State.CLOSED) {
                tile.setState(Tile.State.MARKED);
            } else if (tile.getState() == Tile.State.MARKED) {
                tile.setState(Tile.State.CLOSED);
            }
        }
    }

    /**
     * Generates playing field.
     */
    private void generate() {
        Random random = new Random();
        int nMines = 0;
        while (nMines < mineCount) {
            int x = random.nextInt(rowCount);
            int y = random.nextInt(columnCount);
            if (tiles[x][y] == null) {
                tiles[x][y] = new Mine();
                nMines++;
            }
        }
        for (int j = 0; j < rowCount; j++) {
            for (int k = 0; k < columnCount; k++) {
                if (tiles[j][k] == null) {
                    tiles[j][k] = new Clue(countAdjacentMines(j, k));
                }
            }
        }
        startMillis = System.currentTimeMillis();
    }

    /**
     * Returns true if game is solved, false otherwise.
     *
     * @return true if game is solved, false otherwise
     */
    private boolean isSolved() {
        return (rowCount * columnCount) - mineCount == getNumberOf(Tile.State.OPEN);
    }

    private int getNumberOf(Tile.State state) {
        int counter = 0;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (tiles[i][j].getState() == state) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public int getRemainingMineCount() {
        return (getMineCount()) - getNumberOf(Tile.State.MARKED);
    }

    /**
     * Returns number of adjacent mines for a tile at specified position in the field.
     *
     * @param row    row number.
     * @param column column number.
     * @return number of adjacent mines.
     */
    private int countAdjacentMines(int row, int column) {
        int count = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        if (tiles[actRow][actColumn] instanceof Mine) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public void openAdjacentTiles(int row, int column) {
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        openTile(actRow, actColumn);
                    }
                }
            }
        }
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    public GameState getState() {
        return state;
    }

    public Tile getTile(int row, int column) {
        return tiles[row][column];
    }

    public int getPlayTimeInSeconds() {
        return (int) ((System.currentTimeMillis() - startMillis) / 1000);
    }

    public int getScore() {
        return rowCount * columnCount * 10 - getPlayTimeInSeconds();
    }
}
