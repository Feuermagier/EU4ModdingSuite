package firemage.moddingsuite.ui.layers;

import firemage.moddingsuite.model.Province;
import firemage.moddingsuite.model.data.MapProvider;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class ProvincePalette extends LayerPalette {

    private static final int COLOR_RECT_SIZE = 50;

    private ObjectProperty<Province> selectedProvince = new SimpleObjectProperty<>();
    private IntegerProperty size = new SimpleIntegerProperty(10);

    ProvincePalette() {
        super("Provinzen");


        VBox box = new VBox();

        HBox selectionBox = new HBox();
        Canvas c = new Canvas(COLOR_RECT_SIZE, COLOR_RECT_SIZE);
        c.getGraphicsContext2D().rect(0, 0, COLOR_RECT_SIZE, COLOR_RECT_SIZE);  //white default square
        VBox selectionInfoBox = new VBox();
        Label provNameLabel = new Label();
        Label provColorLabel = new Label();
        selectionInfoBox.getChildren().addAll(provNameLabel, provColorLabel);
        selectionBox.getChildren().addAll(c, selectionInfoBox);
        box.getChildren().add(selectionBox);

        Spinner<Integer> sizeSpinner = new Spinner<>(1, 100, 10);
        size.bind(sizeSpinner.valueProperty());
        box.getChildren().add(sizeSpinner);

        HBox addProvinceBox = new HBox();
        Button addUsedProvinceButton = new Button("Neue genutzte Farbe");
        addUsedProvinceButton.setOnAction(event -> {
            Province p = MapProvider.findNewUnusedProvince();
            if(p == null) {
                new Alert(Alert.AlertType.INFORMATION, "Es gibt keine ungenutzten Farben mehr", ButtonType.CANCEL).showAndWait();
                return;
            }
            selectedProvince.set(p);
        });
        addProvinceBox.getChildren().add(addUsedProvinceButton);
        box.getChildren().add(addProvinceBox);

        Button updateMapButton = new Button("Update");
        updateMapButton.setOnAction(event -> MapProvider.updateProvinceMap());
        box.getChildren().add(updateMapButton);

        this.getChildren().add(box);


        selectedProvince.addListener((observable, oldValue, newValue) ->{
            GraphicsContext g = c.getGraphicsContext2D();
            g.setFill(newValue.getColor());
            g.fillRect(0, 0, COLOR_RECT_SIZE, COLOR_RECT_SIZE);

            provNameLabel.setText(newValue.getName());
            provColorLabel.setText("R:" + (int)(newValue.getColor().getRed()*255) + " G:" + (int)(newValue.getColor().getGreen()*255) + " B:" + (int)(newValue.getColor().getBlue()*255));
        });
    }

    public void setSelectedProvince(Province selection) {
        this.selectedProvince.set(selection);
    }

    @Override
    public void draw(int x0, int y0, int x1, int y1, PixelWriter writer) {
        if(selectedProvince.get() == null || writer == null) return;
        bresenhamLine(x0, y0, x1, y1, size.get(), writer, selectedProvince.get().getColor());
        selectedProvince.get().setOnMap(true);
    }
}
