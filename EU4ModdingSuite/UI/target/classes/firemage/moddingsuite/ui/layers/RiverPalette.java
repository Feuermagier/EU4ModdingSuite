package firemage.moddingsuite.ui.layers;

import firemage.moddingsuite.model.RiverType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.image.PixelWriter;

public class RiverPalette extends LayerPalette {

    private ObjectProperty<RiverType> selectedRiverType = new SimpleObjectProperty<>();

    public RiverPalette() {
        super("Flusskarte");

        ComboBox<RiverType> typeBox = new ComboBox<>();
        typeBox.getItems().setAll(RiverType.values());

        typeBox.setCellFactory(param -> new ListCell<>() {

            @Override
            protected void updateItem(RiverType item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null || empty) setText("#empty#");
                else setText(item.text);
            }
        });

        typeBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(RiverType item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null || empty) setText("#empty#");
                else setText(item.text);
            }
        });

        typeBox.valueProperty().bindBidirectional(selectedRiverType);
        this.getChildren().add(typeBox);
    }

    @Override
    public void draw(int x0, int y0, int x1, int y1, PixelWriter writer) {
        if(selectedRiverType.get() == null || writer == null) return;
        bresenhamLine(x0, y0, x1, y1, 1, writer, selectedRiverType.get().color);
    }
}
