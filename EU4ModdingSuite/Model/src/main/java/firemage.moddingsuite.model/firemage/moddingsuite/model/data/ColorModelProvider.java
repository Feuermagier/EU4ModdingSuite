package firemage.moddingsuite.model.data;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.IndexColorModel;
import java.io.File;

public class ColorModelProvider {

    private static final Logger logger = LogManager.getLogger(ColorModelProvider.class);

    private static IndexColorModel terrainColorModel;
    private static IndexColorModel riverColorModel;

    static {
        try {

            terrainColorModel = (IndexColorModel)ImageIO.read(new File("terrain.bmp")).getColorModel();
            riverColorModel = (IndexColorModel)ImageIO.read(new File("rivers.bmp")).getColorModel();
        }catch(Exception ex) {
            logger.fatal(ex.getMessage());
            Platform.exit();
        }
    }

    public static IndexColorModel getTerrainColorModel() {
        return terrainColorModel;
    }

    public static IndexColorModel getRiverColorModel() { return riverColorModel; }
}
