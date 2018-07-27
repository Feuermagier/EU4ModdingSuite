package firemage.moddingsuite.ui.layers;

import javafx.scene.image.PixelWriter;

public class EmptyPalette extends LayerPalette {

    public EmptyPalette(String name) {
        super(name);
    }

    @Override
    public void draw(int x0, int y0, int x1, int y1, PixelWriter writer) {
        //painting disallowed
    }
}
