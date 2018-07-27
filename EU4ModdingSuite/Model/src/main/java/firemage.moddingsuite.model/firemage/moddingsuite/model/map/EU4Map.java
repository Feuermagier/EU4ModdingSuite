package firemage.moddingsuite.model.map;

import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public abstract class EU4Map {

    private ObjectProperty<WritableImage> imageData;

    EU4Map(ObjectProperty<WritableImage> imageData) {
        this.imageData = imageData;
    }

    public double getWidth() { return imageData.get().getWidth(); }
    public double getHeight() { return  imageData.get().getHeight(); }

    public void writeImageData(WritableImage image) { this.imageData.set(image); }
    public Image getImageData() { return imageData.get(); }
    public ObjectProperty<WritableImage> imageDataProperty() { return imageData; }
}
