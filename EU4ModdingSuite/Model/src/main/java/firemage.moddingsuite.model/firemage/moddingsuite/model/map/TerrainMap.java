package firemage.moddingsuite.model.map;

import firemage.moddingsuite.model.data.ColorModelProvider;
import firemage.moddingsuite.model.data.MapProvider;
import javafx.scene.image.WritableImage;

public class TerrainMap extends IndexedRealMap {

    public TerrainMap() {

        super(MapProvider.terrainMapImageProperty(), ColorModelProvider.getTerrainColorModel(), "terrain");
    }

    @Override
    public void writeImageData(WritableImage image) {
        super.writeImageData(image);

        MapProvider.setTerrainMapImage(image);
    }
}
