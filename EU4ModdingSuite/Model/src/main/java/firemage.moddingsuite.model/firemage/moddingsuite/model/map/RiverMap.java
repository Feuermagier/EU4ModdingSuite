package firemage.moddingsuite.model.map;

import firemage.moddingsuite.model.data.ColorModelProvider;
import firemage.moddingsuite.model.data.MapProvider;
import javafx.scene.image.WritableImage;

public class RiverMap extends  IndexedRealMap {

    public RiverMap() {

        super(MapProvider.riverMapImageProperty(), ColorModelProvider.getRiverColorModel(), "rivers");
    }

    @Override
    public void writeImageData(WritableImage image) {
        super.writeImageData(image);

        MapProvider.setRiverMapImage(image);
    }
}
