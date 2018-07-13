package firemage.moddingsuite.model.map;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;

public abstract class EU4Map {

    private WritableImage imageData;
    private IndexColorModel colorModel;

    public EU4Map(WritableImage imageData, IndexColorModel colorModel) {
        this.imageData = imageData;
        this.colorModel = colorModel;
    }

    public double getWidth() { return imageData.getWidth(); }
    public double getHeight() { return  imageData.getHeight(); }

    public void writeImageData(WritableImage image) { this.imageData = image; }
    public Image getImageData() { return imageData; }

    public IndexColorModel getColorModel() { return colorModel; }

    public BufferedImage convertToSaveableImage() throws Exception {
        BufferedImage writeImage = new BufferedImage((int)getWidth(), (int)getHeight(), BufferedImage.TYPE_BYTE_INDEXED, getColorModel());
        writeImage.createGraphics().drawImage(SwingFXUtils.fromFXImage(getImageData(), null), 0, 0, null);
        return writeImage;
    }
}
