package firemage.moddingsuite.model.map;

import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;

public abstract class RealMap extends EU4Map {

    private String filename;

     RealMap(ObjectProperty<WritableImage> mapImage, String filename) {
        super(mapImage);

        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public BufferedImage convertToSaveableImage() {
        BufferedImage writeImage = new BufferedImage((int)getWidth(), (int)getHeight(), BufferedImage.TYPE_3BYTE_BGR);      //buffered image type for province map must be BufferedImage.TYPE_3BYTE_BGR (=5)
        writeImage.createGraphics().drawImage(SwingFXUtils.fromFXImage(getImageData(), null), 0, 0, null);
        for(int x=0; x<writeImage.getWidth(); x++) {
            for(int y=0; y<writeImage.getHeight(); y++) {
                if(writeImage.getRGB(x,y) < 0xFF000000) {
                    writeImage.setRGB(x, y, writeImage.getRGB(x, y) | 0xFF000000);
                }
            }
        }
        return writeImage;
    }
}
