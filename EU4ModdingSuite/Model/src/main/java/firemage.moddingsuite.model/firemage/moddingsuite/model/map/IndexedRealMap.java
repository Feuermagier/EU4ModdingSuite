package firemage.moddingsuite.model.map;

import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;

public class IndexedRealMap extends RealMap {

    private IndexColorModel colorModel;

    public IndexedRealMap(ObjectProperty<WritableImage> mapImage, IndexColorModel colorModel, String filename) {
        super(mapImage, filename);

        this.colorModel = colorModel;
    }


    public IndexColorModel getColorModel() { return colorModel; }

    @Override
    public BufferedImage convertToSaveableImage() {
        //BufferedImage writeImage = new BufferedImage((int)getWidth(), (int)getHeight(), BufferedImage.TYPE_BYTE_INDEXED, getColorModel());
        //writeImage.createGraphics().drawImage(SwingFXUtils.fromFXImage(getImageData(), null), 0, 0, null);

        BufferedImage writeImage = new BufferedImage((int)getWidth(), (int)getHeight(), BufferedImage.TYPE_BYTE_INDEXED, getColorModel());

        PixelReader fxReader = getImageData().getPixelReader();
        for(int x=0; x<writeImage.getWidth(); x++) {
            for(int y=0; y<writeImage.getHeight(); y++) {
                writeImage.setRGB(x, y, fxReader.getArgb(x, y));
            }
        }

        return writeImage;
    }

    private void printPixelARGB(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);
    }
}
