package firemage.moddingsuite.model.map;

import firemage.moddingsuite.model.data.MapProvider;
import javafx.scene.image.WritableImage;

public class ProvinceMap extends RealMap {
    public ProvinceMap() {
        super(MapProvider.provinceMapImageProperty(), "provinces");
    }

    @Override
    public void writeImageData(WritableImage image) {
        super.writeImageData(image);

        MapProvider.setProvinceMapImage(image);
    }
}
