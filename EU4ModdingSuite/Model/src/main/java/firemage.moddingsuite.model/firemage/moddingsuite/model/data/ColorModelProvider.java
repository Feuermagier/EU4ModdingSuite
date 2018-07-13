package firemage.moddingsuite.model.data;

import firemage.moddingsuite.model.util.FileUtil;

import java.awt.image.IndexColorModel;
import java.io.File;

public class ColorModelProvider {

    private static IndexColorModel terrainColorModel;

    static {
        System.out.println(new File("terrain_color.txt").getAbsolutePath());
        try {
            int[] data = FileUtil.readIntegerCSVFile(new File("terrain_colors.txt"), 0);
            byte[] reds = FileUtil.readByteCSVFile(new File("terrain_colors.txt"), 1);
            byte[] blues = FileUtil.readByteCSVFile(new File("terrain_colors.txt"), 2);
            byte[] greens = FileUtil.readByteCSVFile(new File("terrain_colors.txt"), 3);

            terrainColorModel = new IndexColorModel(data[0], data[1], reds, greens, blues);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static IndexColorModel getTerrainColorModel() {
        return terrainColorModel;
    }
}
