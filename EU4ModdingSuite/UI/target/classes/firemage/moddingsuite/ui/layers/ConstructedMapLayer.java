package firemage.moddingsuite.ui.layers;

import firemage.moddingsuite.model.Province;
import firemage.moddingsuite.model.map.ConstructedMap;
import javafx.scene.paint.Color;

import java.util.function.Function;

public class ConstructedMapLayer extends Layer {


    public ConstructedMapLayer(String name, ConstructedMap map, LayerPalette palette) {
        super(name, map, palette);
    }
}
