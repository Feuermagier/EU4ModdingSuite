package firemage.moddingsuite.model.map;

import firemage.moddingsuite.model.Province;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.image.IndexColorModel;
import java.util.ArrayList;

public abstract class ProvinceMap extends EU4Map {

    private ArrayList<Province> provinces;

    public ProvinceMap(int width, int height, ArrayList<Province> provinces, IndexColorModel colorModel) {
        super(new WritableImage(width, height), colorModel);

        this.provinces = provinces;
    }

    public Province getProvinceAt(int x, int y) {
        return findProvince(getImageData().getPixelReader().getColor(x, y));
    }

    private Province findProvince(Color color) {
        for(Province p : provinces) {
            if(p.getColor().equals(color)) return p;
        }
        return null;
    }
}
