package firemage.moddingsuite.model.map;

import firemage.moddingsuite.model.data.ColorModelProvider;

public class TerrainMap extends VisualMap {

    public TerrainMap(int width, int height) {
        super(width, height, ColorModelProvider.getTerrainColorModel());
    }
}
