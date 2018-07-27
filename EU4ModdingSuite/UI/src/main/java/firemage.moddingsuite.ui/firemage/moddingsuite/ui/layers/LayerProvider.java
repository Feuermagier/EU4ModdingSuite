package firemage.moddingsuite.ui.layers;

import javafx.collections.ObservableList;

public class LayerProvider {
    private static ObservableList<Layer> layers;

    public static ObservableList<Layer> getLayers() {
        return layers;
    }

    public static void setLayers(ObservableList<Layer> layers) {
        LayerProvider.layers = layers;
    }
}
