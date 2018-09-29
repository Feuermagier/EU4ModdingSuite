package firemage.moddingsuite.ui.layers;


import firemage.moddingsuite.model.data.MapProvider;
import firemage.moddingsuite.model.map.EU4Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public abstract class Layer extends Canvas {

    private static final Logger logger = LogManager.getLogger(Layer.class);

    private StringProperty name = new SimpleStringProperty();
    private EU4Map map;
    private int prevX, prevY;

    private LayerPalette palette;

    Layer(String name, EU4Map map, LayerPalette palette) {
        super(map.getWidth(), map.getHeight());

        this.palette = palette;
        this.map = map;

        this.getGraphicsContext2D().drawImage(map.getImageData(), 0, 0);
        map.imageDataProperty().addListener((observable, oldValue, newValue) ->  {
            GraphicsContext gc = this.getGraphicsContext2D();
            //gc.setGlobalBlendMode(BlendMode.SRC_ATOP);
            gc.clearRect(0, 0, getWidth(), getHeight());
            //gc.drawImage(PaintUtil.makeImageTransparent(newValue, 0.3), 0, 0);
            gc.drawImage(newValue, 0, 0);
        });

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
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> saveToMap());
        this.name.set(name);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public EU4Map getMap() { return map; }

    public LayerPalette getPalette() {
        return palette;
    }

    //privates

    private void saveToMap() {
        WritableImage writableImage = new WritableImage(MapProvider.getWidth(), MapProvider.getHeight());
        this.snapshot(null, writableImage);
        map.writeImageData(writableImage);
    }
}
