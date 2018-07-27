package firemage.moddingsuite.model;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RivermapProducer {

    private static final Color SEA_GRAY = new Color(122/255.0, 122/255.0, 122/255.0, 1);

    private static Logger logger = LogManager.getLogger(RivermapProducer.class);

    public WritableImage createRivermap(Image terrainMap) {
        logger.debug("creating new rivermap");

        WritableImage rivermap = new WritableImage((int)terrainMap.getWidth(), (int)terrainMap.getHeight());

        PixelReader reader = terrainMap.getPixelReader();
        PixelWriter writer = rivermap.getPixelWriter();

        for(int x=0; x<terrainMap.getWidth(); x++) {
            for(int y=0; y<terrainMap.getHeight(); y++) {
                TerrainType type = TerrainType.findTerrainByColor(reader.getColor(x, y));
                if(type != null && type.isSea)
                    writer.setColor(x, y, SEA_GRAY);
                else
                    writer.setColor(x, y, Color.WHITE);
            }
        }

        return rivermap;
    }

    public WritableImage createRivermap(Image terrainMap, Image prevRivermap) {
        logger.debug("creating rivermap from existing");

        WritableImage rivermap = new WritableImage((int)terrainMap.getWidth(), (int)terrainMap.getHeight());

        PixelReader reader = terrainMap.getPixelReader();
        PixelReader prevReader = prevRivermap.getPixelReader();
        PixelWriter writer = rivermap.getPixelWriter();

        for(int x=0; x<terrainMap.getWidth(); x++) {
            for(int y=0; y<terrainMap.getHeight(); y++) {
                Color color = reader.getColor(x, y);
                if(!color.equals(SEA_GRAY) &! color.equals(Color.WHITE)) {
                    writer.setColor(x, y, prevReader.getColor(x, y));
                } else {
                    TerrainType type = TerrainType.findTerrainByColor(color);
                    if (type != null && type.isSea) {
                        writer.setColor(x, y, SEA_GRAY);
                    } else {
                        writer.setColor(x, y, Color.WHITE);
                    }
                }
            }
        }

        return rivermap;
    }
}
