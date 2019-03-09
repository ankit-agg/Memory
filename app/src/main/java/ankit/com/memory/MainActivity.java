package ankit.com.memory;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ankit.com.memory.callbacks.OnTileSelectListener;
import ankit.com.memory.models.GameLevel;
import ankit.com.memory.models.Tile;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnTileSelectListener {

    private GameViewModel viewModel;
    private TilesRecyclerViewAdapter adapter;
    private Handler handler;

    private Tile lastSelectedTile;
    private List<Tile> tilesList;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.timer)
    TextView timerTxt;

    @BindView(R.id.level)
    TextView levelTxt;

    @BindView(R.id.score)
    TextView scoreTxt;

    private CountDownTimer countDownTimer;
    private long timeLeft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        handler = new Handler();

        viewModel = ViewModelProviders.of(this).get(GameViewModel.class);

        adapter = new TilesRecyclerViewAdapter(this, this);
        recyclerView.setAdapter(adapter);

        viewModel.getGameLevelLiveData().observe(this, gameLevel -> {
            if (gameLevel == null) {
                return;
            }
            onNewLevel(gameLevel);
        });

        viewModel.getLevelScoreLiveData().observe(this, pair -> {
            if (pair == null) {
                return;
            }
            Toast.makeText(this, getString(R.string.level_score, pair.second, pair.first.getDifficultyLevel()), Toast
                    .LENGTH_SHORT).show();
        });

        viewModel.getTotalScoreLiveData().observe(this, score -> {
            scoreTxt.setText(getString(R.string.total_score, score));
        });

        viewModel.startGame();
    }

    private void onNewLevel(GameLevel gameLevel) {
        recyclerView.setLayoutManager(new GridLayoutManager(this, gameLevel.getNoColumns()));
        tilesList = viewModel.getTilesForLevel();
        adapter.updateTiles(tilesList);
        lastSelectedTile = null;

        levelTxt.setText(getString(R.string.level, gameLevel.getDifficultyLevel()));
        countDownTimer = new CountDownTimer(gameLevel.getTime() * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished / 1000;
                timerTxt.setText(getString(R.string.seconds_left, timeLeft));
                if(timeLeft == 1) {
                    onTimeOver();
                }
            }

            public void onFinish() {
            }

        }.start();
    }

    private void onTimeOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.game_over));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                viewModel.startGame();
            }
        });
        builder.create().show();
    }

    private void onSuccessfulSelection() {
        for (Tile tile : tilesList) {
            if (!tile.isSelected()) {
                return;
            }
        }
        if (viewModel.getGameLevelLiveData().getValue() == null) {
            return;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        viewModel.onLevelCompleted(viewModel.getGameLevelLiveData().getValue().getTime() - timeLeft);
    }

    @Override
    public void onTileSelected(int position, Tile tile) {
        tile.setSelected(true);
        adapter.notifyItemChanged(position);

        handler.postDelayed(() -> {
            if (lastSelectedTile == null) {
                lastSelectedTile = tile;
                return;
            }
            if (lastSelectedTile.getValue() != tile.getValue()) {
                lastSelectedTile.setSelected(false);
                tile.setSelected(false);
                adapter.notifyItemChanged(tilesList.indexOf(lastSelectedTile));
                adapter.notifyItemChanged(position);
                lastSelectedTile = null;
                return;
            }
            lastSelectedTile = null;
            onSuccessfulSelection();
        }, 200);
    }

    @Override
    protected void onStop() {
        super.onStop();
        countDownTimer.cancel();
    }
}
