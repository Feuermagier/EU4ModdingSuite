package firemage.moddingsuite.ui.layers;

import java.awt.image.RenderedImage;

import firemage.moddingsuite.model.data.MapProvider;
import firemage.moddingsuite.ui.MainViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import firemage.moddingsuite.model.map.EU4Map;

public class Layer extends Canvas {

    private StringProperty name = new SimpleStringProperty();
    private EU4Map map;
    private int prevX, prevY;

    private LayerPalette palette;

    public Layer(String name, EU4Map map, LayerPalette palette) {
        super(map.getWidth(), map.getHeight());

        this.palette = palette;
        this.map = map;

        saveToMap();

        PixelWriter writer = this.getGraphicsContext2D().getPixelWriter();

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            prevX = (int)event.getX();
            prevY = (int)event.getY();
        });
        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            palette.draw(prevX, prevY, (int) event.getX(), (int) event.getY(), writer);
            prevX = (int) event.getX();
            prevY = (int) event.getY();
        });
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            saveToMap();
        });
        this.name.set(name);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
    public EU4Map getMap() { return map; }

    public LayerPalette getPalette() {
        return palette;
    }

    //privates

    private void line(int x0, int y0, int x1, int y1, PixelWriter writer, Color color) {
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

    private void saveToMap() {
        WritableImage writableImage = new WritableImage(MapProvider.getWidth(), MapProvider.getHeight());
        this.snapshot(null, writableImage);
        map.writeImageData(writableImage);
    }
}
