package ankit.com.memory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import ankit.com.memory.callbacks.OnTileSelectListener;
import ankit.com.memory.models.Tile;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TilesRecyclerViewAdapter extends RecyclerView.Adapter<TilesRecyclerViewAdapter.ViewHolder> {

    private WeakReference<Context> context;
    private List<Tile> tiles;
    private LayoutInflater layoutInflater;
    private OnTileSelectListener listener;

    public TilesRecyclerViewAdapter(Context context, OnTileSelectListener listener) {
        this.context = new WeakReference<>(context);
        layoutInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public TilesRecyclerViewAdapter(Context context, List<Tile> tiles, OnTileSelectListener listener) {
        this.context = new WeakReference<>(context);
        this.tiles = tiles;
        layoutInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public void updateTiles(List<Tile> tiles) {
        this.tiles = tiles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_tile, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tile tile = getItemAtPosition(position);
        holder.tileDescriptionTxt.setText(String.valueOf(tile.getValue()));

        if (tile.isSelected()) {
            holder.tileDescriptionTxt.setVisibility(View.VISIBLE);
        } else {
            holder.tileDescriptionTxt.setVisibility(View.GONE);
        }
    }

    public Tile getItemAtPosition(int position) {
        return tiles.get(position);
    }

    @Override
    public int getItemCount() {
        return tiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tile_description)
        TextView tileDescriptionTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onTileSelected(getAdapterPosition(), getItemAtPosition(getAdapterPosition()));
        }
    }
}
