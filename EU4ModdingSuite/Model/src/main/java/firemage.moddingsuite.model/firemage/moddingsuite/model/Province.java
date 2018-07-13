package firemage.moddingsuite.model;

import javafx.scene.paint.Color;

public class Province {
    private Color color;
    private int id;

    public Province(int id, Color color) {
        this.color = color;
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
