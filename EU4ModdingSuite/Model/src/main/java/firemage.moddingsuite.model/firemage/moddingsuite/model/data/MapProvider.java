package firemage.moddingsuite.model.data;

import firemage.moddingsuite.model.map.TerrainMap;

import java.util.Map;

public class MapProvider {

    private static int width, height;

    private static TerrainMap terrain;

    public static void setMapSize(int mapWidth, int mapHeight) {
        width = mapWidth;
        height = mapHeight;

        terrain = new TerrainMap(width, height);
    }

    public static TerrainMap getTerrain() {
        return terrain;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
