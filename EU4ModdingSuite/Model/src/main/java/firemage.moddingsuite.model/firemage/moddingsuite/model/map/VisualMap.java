package firemage.moddingsuite.model.map;

import javafx.scene.image.WritableImage;

import java.awt.image.IndexColorModel;

public abstract class VisualMap extends EU4Map {

    public VisualMap(int width, int height, IndexColorModel colorModel) {
        super(new WritableImage(width, height), colorModel);
    }


}
