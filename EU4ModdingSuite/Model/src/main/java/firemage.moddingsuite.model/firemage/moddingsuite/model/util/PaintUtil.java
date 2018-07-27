package firemage.moddingsuite.model.util;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class PaintUtil {

    public static void line(int x0, int y0, int x1, int y1, PixelWriter writer, Color color) {
        //Bresenham-Algorithmus
        int dx = Math.abs(x1-x0), sx = x0<x1 ? 1 : -1;
        int dy = -Math.abs(y1-y0), sy = y0<y1 ? 1 : -1;
        int err = dx+dy, e2;
        while (true) {
            //custom - draw rect
            for(int i=-5; i<=5; i++) {
                for (int q = -5; q <= 5; q++) {
                    writer.setColor(x0+i, y0+q, color);
                }
            }
            if (x0==x1 && y0==y1) break;
            e2 = 2*err;
            if (e2 > dy) { err += dy; x0 += sx; } /* e_xy+e_x > 0 */
            if (e2 < dx) { err += dx; y0 += sy; } /* e_xy+e_y < 0 */
        }
    }
}
