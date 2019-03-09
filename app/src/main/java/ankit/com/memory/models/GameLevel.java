package ankit.com.memory.models;

public class GameLevel {

    private static final int STARTING_LEVEL_DURATION = 60;
    private static final double TIME_MULTIPLIER = 0.96;
    private static final int STARTING_DIFFICULTY_LEVEL = 1;
    private static final int INITIAL_NO_OF_ROWS = 3;
    private static final int INITIAL_NO_COLUMNS = 2;
    private static final int DEFAULT_ROWS_ADDITION = 1;
    private static final int DEFAULT_COLUMN_ADDITION = 1;
    private static final int SCORE_MULTIPLIER = 100;

    private int difficultyLevel;
    private int time;
    private int noRows;
    private int noColumns;

    public GameLevel() {
        difficultyLevel = STARTING_DIFFICULTY_LEVEL;
        time = STARTING_LEVEL_DURATION;
        noRows = INITIAL_NO_OF_ROWS;
        noColumns = INITIAL_NO_COLUMNS;
    }

    public GameLevel getNextLevel() {
        GameLevel level = new GameLevel();
        level.difficultyLevel = difficultyLevel + 1;
        level.time = (int) (time * TIME_MULTIPLIER);
        level.noRows = noRows + DEFAULT_ROWS_ADDITION;
        level.noColumns = noColumns + DEFAULT_COLUMN_ADDITION;
        return level;
    }

    public int getScoreForLevel(long timeToFinish) {
        return (int) ((time - timeToFinish) * difficultyLevel * SCORE_MULTIPLIER);
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public int getTime() {
        return time;
    }

    public int getNoRows() {
        return noRows;
    }

    public int getNoColumns() {
        return noColumns;
    }
}
