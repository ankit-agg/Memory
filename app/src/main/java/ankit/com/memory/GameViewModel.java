package ankit.com.memory;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ankit.com.memory.models.GameLevel;
import ankit.com.memory.models.Tile;

public class GameViewModel extends ViewModel {

    private MutableLiveData<GameLevel> gameLevelLiveData;
    private MutableLiveData<Pair<GameLevel, Integer>> levelScoreLiveData;
    private MutableLiveData<Integer> totalScoreLiveData;

    public GameViewModel() {
        gameLevelLiveData = new MutableLiveData<>();
        totalScoreLiveData = new MutableLiveData<>();
        levelScoreLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Pair<GameLevel, Integer>> getLevelScoreLiveData() {
        return levelScoreLiveData;
    }

    public MutableLiveData<Integer> getTotalScoreLiveData() {
        return totalScoreLiveData;
    }

    public MutableLiveData<GameLevel> getGameLevelLiveData() {
        return gameLevelLiveData;
    }

    public void onLevelCompleted(long secondsToComplete) {
        GameLevel gameLevel = gameLevelLiveData.getValue();
        if (gameLevel == null) {
            return;
        }
        int score = gameLevel.getScoreForLevel(secondsToComplete);
        gameLevelLiveData.postValue(gameLevel.getNextLevel());
        levelScoreLiveData.postValue(new Pair<>(gameLevel, score));
        int totalScore = 0;
        if (getTotalScoreLiveData().getValue() != null) {
            totalScore += score;
        } else {
            totalScore = score;
        }
        totalScoreLiveData.postValue(totalScore);
    }

    public void onTimerCompleted() {
        GameLevel gameLevel = gameLevelLiveData.getValue();
        if (gameLevel == null) {
            gameLevelLiveData.postValue(new GameLevel());
        } else {
            gameLevelLiveData.postValue(gameLevel);
        }
    }

    public void startGame() {
        gameLevelLiveData.postValue(new GameLevel());
        totalScoreLiveData.postValue(0);
    }

    public List<Tile> getTilesForLevel() {
        GameLevel level = gameLevelLiveData.getValue();
        if (level == null) {
            return new ArrayList<>();
        }
        int totalValues = (level.getNoRows() * level.getNoColumns()) / 2;
        List<Tile> tiles = new ArrayList<>();
        for (int i = 1; i <= totalValues; i++) {
            tiles.add(new Tile(i));
        }
        for (int i = 1; i <= totalValues; i++) {
            tiles.add(new Tile(i));
        }
        Collections.shuffle(tiles);
        return tiles;
    }

}
