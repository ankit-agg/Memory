package ankit.com.memory.models;

import java.util.List;

public class Board {
    private List<Tile> tileList;
    private GameLevel gameLevel;

    public Board(List<Tile> tileList) {
        this.tileList = tileList;
    }

    public List<Tile> getTileList() {
        return tileList;
    }

    public void setTileList(List<Tile> tileList) {
        this.tileList = tileList;
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    public void setGameLevel(GameLevel gameLevel) {
        this.gameLevel = gameLevel;
    }

}
