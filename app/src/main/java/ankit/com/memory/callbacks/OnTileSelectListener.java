package ankit.com.memory.callbacks;

import ankit.com.memory.models.Tile;

public interface OnTileSelectListener {
    void onTileSelected(int position, Tile tile);
}
