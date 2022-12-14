package sk.tuke.gamestudio.minesweeper.consoleui;

import java.io.*;

public class Settings implements Serializable {
    private static final String SETTING_FILE = System.getProperty("user.home") + System.getProperty("file.separator") + "minesweeper.settings";
    public static Settings BEGINNER = new Settings(3, 3, 1); // temporary changed from 9-9-10
    public static Settings INTERMEDIATE = new Settings(16, 16, 40);
    public static Settings EXPERT = new Settings(16, 30, 99);
    private int rowCount;
    private int columnCount;
    private int mineCount;

    public Settings(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
    }

    public static Settings load() {
        //try with resources - netreba robit close()
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(SETTING_FILE))) {
            return (Settings) ois.readObject();
        } catch (IOException e) {
            System.out.println("unable to open,  default BEGINNER");
        } catch (ClassNotFoundException e) {
            System.out.println("unable to read settings, default BEGINNER");
        }
        return BEGINNER;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    public void setMineCount(int mineCount) {
        this.mineCount = mineCount;
    }

    @Override
    public boolean equals(Object obj) {
        //inicializacia s priamo v podmienke
        //pattern variables
        if (!(obj instanceof Settings s)) {
            return false;
        }
        return s.rowCount == rowCount
                && s.columnCount == columnCount
                && s.mineCount == mineCount;
    }

    @Override
    public int hashCode() {
        return rowCount * columnCount * mineCount;
    }

    public void save() {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(
                    new FileOutputStream(SETTING_FILE));
            oos.writeObject(this);
        } catch (IOException e) {
            System.out.println("unable to update settings");
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    //empty naschval
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Settings [" + rowCount + "," + columnCount + "," + mineCount + "]";
    }
}
