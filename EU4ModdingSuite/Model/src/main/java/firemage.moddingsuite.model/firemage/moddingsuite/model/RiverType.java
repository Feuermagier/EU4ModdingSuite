package firemage.moddingsuite.model;

import javafx.scene.paint.Color;

public enum RiverType {

    SOURCE("Quelle", new Color(0, 255/255.0, 0, 1)),
    FLOW_IN("Einfluss", new Color(255/255.0, 0, 0, 1)),
    FLOW_OUT("Ausfluss", new Color(255/255.0, 252/255.0, 0, 1)),
    NARROWEST("Schmalster Fluss", new Color(0, 225/255.0, 255/255.0, 1)),
    NARROW("Schmaler Fluss", new Color(0, 200/255.0, 255/255.0, 1)),
    WIDE("Breiter Fluss", new Color(0, 100/255.0, 255/255.0, 1)),
    WIDEST("Breitester Fluss", new Color(0, 0, 200/255.0, 1));

    public Color color;
    public String text;

    RiverType(String text, Color color) {
        this.color = color;
        this.text = text;
    }
}
