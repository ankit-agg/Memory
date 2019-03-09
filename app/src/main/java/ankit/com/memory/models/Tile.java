package ankit.com.memory.models;

public class Tile {

    private int value;

    private boolean selected;

    public Tile(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
