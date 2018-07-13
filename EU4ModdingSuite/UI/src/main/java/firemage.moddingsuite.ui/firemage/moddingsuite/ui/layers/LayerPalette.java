package firemage.moddingsuite.ui.layers;

import javafx.scene.image.PixelWriter;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public abstract class LayerPalette extends VBox {

    private String name;

    LayerPalette(String name) {

        this.name = name;
    }

    public abstract void draw(int x0, int y0, int x1, int y1, PixelWriter writer);

    //can be used - Bresenham-Algorithmus
    void bresenhamLine(int x0, int y0, int x1, int y1, int size, PixelWriter writer, Color color) {

        int dx = Math.abs(x1-x0), sx = x0<x1 ? 1 : -1;
        int dy = -Math.abs(y1-y0), sy = y0<y1 ? 1 : -1;
        int err = dx+dy, e2;
        while (true) {
            drawRectAt(x0, y0, size, writer, color);
            if (x0==x1 && y0==y1) break;
            e2 = 2*err;
            if (e2 > dy) { err += dy; x0 += sx; } /* e_xy+e_x > 0 */
            if (e2 < dx) { err += dx; y0 += sy; } /* e_xy+e_y < 0 */
        }
    }

    void drawRectAt(int x, int y, int size, PixelWriter writer, Color color) {
        for(int i=-size/2; i<=size/2; i++) {
            for (int q = -size/2; q <= size/2; q++) {
                writer.setColor(x+i, y+q, color);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
