package firemage.moddingsuite.model.map;

import firemage.moddingsuite.model.data.MapProvider;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;

public class Heightmap extends RealMap {

    public Heightmap() {
        super(MapProvider.heightmapImageProperty(), "heightmap");
    }

    @Override
    public void writeImageData(WritableImage image) {
        super.writeImageData(image);

        MapProvider.setRiverMapImage(image);
    }

    @Override
    public BufferedImage convertToSaveableImage() {
        BufferedImage writeImage = new BufferedImage((int)getWidth(), (int)getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        writeImage.createGraphics().drawImage(SwingFXUtils.fromFXImage(getImageData(), null), 0, 0, null);
        return writeImage;
    }
}
