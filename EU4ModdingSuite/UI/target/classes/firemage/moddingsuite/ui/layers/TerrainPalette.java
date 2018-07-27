package firemage.moddingsuite.ui.layers;

import firemage.moddingsuite.model.TerrainType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class TerrainPalette extends LayerPalette {

    private ObjectProperty<TerrainType> selectedTerrain = new SimpleObjectProperty<>();

    private Spinner<Integer> sizeSpinner = new Spinner<Integer>(1, 100, 10);
    private int size = sizeSpinner.getValue();

    public TerrainPalette() {
        super("Terrain");

        ComboBox<TerrainType> typeBox = new ComboBox<>();
        typeBox.getItems().setAll(TerrainType.values());

        typeBox.setCellFactory(param -> new ListCell<>() {

            @Override
            protected void updateItem(TerrainType item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null || empty) setText("#empty#");
                else setText(item.text);
            }
        });

        typeBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TerrainType item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null || empty) setText("#empty#");
                else setText(item.text);
            }
        });

        typeBox.valueProperty().bindBidirectional(selectedTerrain);
        this.getChildren().add(typeBox);

        sizeSpinner.setEditable(true);
        sizeSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            size = newValue;
        });
        this.getChildren().add(sizeSpinner);

    }

    @Override
    public void draw(int x0, int y0, int x1, int y1, PixelWriter writer) {
        if(selectedTerrain.get() == null || writer == null) return;
        bresenhamLine(x0, y0, x1, y1, size, writer, selectedTerrain.get().color);
    }
}
