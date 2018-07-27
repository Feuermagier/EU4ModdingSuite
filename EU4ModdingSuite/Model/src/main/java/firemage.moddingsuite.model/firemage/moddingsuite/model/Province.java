package firemage.moddingsuite.model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Province {
    private Color color;
    private int id;
    private List<Integer[]> lines = new ArrayList<>();
    private String name;
    private boolean isOnMap = false;

    public Province(int id, String name, Color color) {
        this.color = color;
        this.id = id;
        this.name = name;
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

    public List<Integer[]> getLines() {
        return lines;
    }

    public void setLines(List<Integer[]> lines) {
        this.lines = lines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnMap() {
        return isOnMap;
    }

    public void setOnMap(boolean onMap) {
        isOnMap = onMap;
    }

    //privates----------------------------------------------------------------------------------------------------

}
