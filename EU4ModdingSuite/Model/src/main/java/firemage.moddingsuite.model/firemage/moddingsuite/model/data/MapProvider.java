package firemage.moddingsuite.model.data;

import firemage.moddingsuite.model.Province;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.*;

public class MapProvider {

    private static Logger logger = LogManager.getLogger( MapProvider.class );
    private static int width, height;
    private static ArrayList<Province> provinces = new ArrayList<>();
    private static ArrayList<Integer[]> borders = new ArrayList<>();

    private static ObjectProperty<WritableImage> provinceMapImage = new SimpleObjectProperty<>();
    private static ObjectProperty<WritableImage> terrainMapImage = new SimpleObjectProperty<>();
    private static ObjectProperty<WritableImage> riverMapImage = new SimpleObjectProperty<>();
    private static ObjectProperty<WritableImage> heightmapImage = new SimpleObjectProperty<>();

    public static void setProvinceMapImage(WritableImage image) {
        width = (int)image.getWidth();
        height = (int)image.getHeight();
        provinceMapImage.set(image);
    }

    public static Province findNewUnusedProvince() {
        for(Province p : provinces) {
            if(!p.isOnMap()) return p;
        }
        return null;
    }


    public static WritableImage getTerrainMapImage() {
        return terrainMapImage.get();
    }

    public static void setTerrainMapImage(WritableImage image) {
        terrainMapImage.set(image);
    }

    public static ObjectProperty<WritableImage> provinceMapImageProperty() {
        return provinceMapImage;
    }

    public static ObjectProperty<WritableImage> terrainMapImageProperty() {
        return terrainMapImage;
    }

    public static ObjectProperty<WritableImage> riverMapImageProperty() {
        return riverMapImage;
    }

    public static ObjectProperty<WritableImage> heightmapImageProperty() {
        return heightmapImage;
    }

    public static void setHeightmapImage(WritableImage heightmapImage) {
        MapProvider.heightmapImage.set(heightmapImage);
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static ArrayList<Province> getProvinces() {
        return provinces;
    }

    public static void setProvinces(ArrayList<Province> provinces) {
        MapProvider.provinces = provinces;
    }

    public static WritableImage getRiverMapImage() {
        return riverMapImage.get();
    }

    public static void setRiverMapImage(WritableImage riverMapImage) {
        MapProvider.riverMapImage.set(riverMapImage);
    }

    public static Province findProvince(Color color) {
        for(Province p : provinces) {
            if(p.getColor().equals(color)) return p;
        }
        return null;
    }

    public static ArrayList<Integer[]> getBorders() {
        return borders;
    }

    public static void setBorders(ArrayList<Integer[]> borders) {
        MapProvider.borders = borders;
    }

    public static boolean isPixelBorder(Integer[] pixel) {
        for(Integer[] b : borders) {
            if(Arrays.equals(b, pixel)) return true;
        }
        return false;
    }

    public static Province getProvinceAt(int x, int y) {
        return findProvince(provinceMapImage.get().getPixelReader().getColor(x, y));
    }

    public static void updateProvinceMap() {
        logger.debug("updating map");

        PixelReader reader = provinceMapImage.get().getPixelReader();

        HashMap<Color, List<Integer[]>> provLines = new HashMap<>();
        HashMap<Integer[], Object> tmpBorders = new HashMap<>();

        Color rgb;
        int width = (int) provinceMapImage.get().getWidth();
        int height = (int) provinceMapImage.get().getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rgb = reader.getColor(x, y);

                provLines.computeIfAbsent(rgb, k -> new ArrayList<>(100));

                Integer[] points = new Integer[3];

                // Store the first point
                points[0] = y;
                points[1] = x;

                // Go until it's not the same color or it hits the edge of the image
                while (x < width && rgb.equals(reader.getColor(x, y))) {
                    x++;
                }

                points[2] = x;

                provLines.get(rgb).add(points);

                if (!(rgb.equals(Color.BLACK)) && (x < width) & !(reader.getColor(x - 1, y).equals(Color.BLACK))) { // it's PTI, so don't bother with a border
                    tmpBorders.put(new Integer[]{x, y}, null);
                }

                x--;
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                rgb = reader.getColor(x, y);

                do {
                    y++;
                } while (y < height && rgb.equals(reader.getColor(x, y)));
                if (!(rgb.equals(Color.BLACK)) && (y < height) & !(reader.getColor(x, y - 1).equals(Color.BLACK))) { // it's PTI, so don't bother with a border
                    tmpBorders.put(new Integer[]{x, y}, null);
                }
            }
        }

        MapProvider.setBorders(new ArrayList<>(tmpBorders.keySet()));

        provLines.forEach((color, lines) -> {
            Province p = MapProvider.findProvince(color);
            if(p == null) return;
            p.setLines(lines);
            p.setOnMap(true);
        });
    }
}
