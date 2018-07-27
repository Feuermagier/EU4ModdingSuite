package firemage.moddingsuite.model.map;

import firemage.moddingsuite.model.Province;
import firemage.moddingsuite.model.data.MapProvider;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

public class ConstructedMap extends EU4Map {

    private static Logger logger = LogManager.getLogger(ConstructedMap.class);

    private Function<Province, Color> colorFunction;

    public ConstructedMap(int width, int height, Function<Province, Color> colorFunction) {
        super(new SimpleObjectProperty<>(new WritableImage(width, height)));

        this.colorFunction = colorFunction;

        updateImageData();
    }

    public void updateImageData(){

        logger.debug("Updating image data of constructed map");

        WritableImage image = new WritableImage(MapProvider.getWidth(), MapProvider.getHeight());
        PixelWriter writer = image.getPixelWriter();

        for(Province p : MapProvider.getProvinces()) {
            Color color = colorFunction.apply(p);

            for(Integer[] line : p.getLines()) {
                for(int x=line[1]; x<line[2]; x++) {
                    writer.setColor(x, line[0], color);
                }
            }
        }
        this.writeImageData(image);
    }
}
