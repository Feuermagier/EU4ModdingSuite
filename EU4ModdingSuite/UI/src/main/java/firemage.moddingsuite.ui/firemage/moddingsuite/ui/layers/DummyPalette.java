package firemage.moddingsuite.ui.layers;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class DummyPalette extends LayerPalette {

    public DummyPalette(String name) {
        super(name);
    }

    @Override
    public void draw(int x0, int y0, int x1, int y1, PixelWriter writer) {
        super.bresenhamLine(x0, y0, x1, y1, 10, writer, Color.valueOf("Black"));
    }
}
