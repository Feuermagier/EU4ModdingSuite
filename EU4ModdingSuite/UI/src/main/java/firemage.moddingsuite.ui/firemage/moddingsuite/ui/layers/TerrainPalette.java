package firemage.moddingsuite.ui.layers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class TerrainPalette extends LayerPalette {

    private ComboBox<TerrainType> typeBox = new ComboBox<>();
    private ObjectProperty<TerrainType> selectedTerrain = new SimpleObjectProperty<>();

    private Spinner<Integer> sizeSpinner = new Spinner<Integer>(1, 1000, 10);
    private int size = sizeSpinner.getValue();

    public TerrainPalette() {
        super("Terrain");

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

    private enum TerrainType {

        GRASSLANDS("Grasland", Color.valueOf("#567c1b")),
        HILLS("Hügel", Color.valueOf("#005606")),
        DESERT_MOUNTAIN("Wüste (Bergig)", Color.valueOf("#704a1f")),
        DESERT("Wüste", Color.valueOf("#cea963")),
        PLAINS("Ebene", Color.valueOf("#c8d66b")),
        MOUNTAIN("Berg", Color.valueOf("#412a11")),
        DESERT_MOUNTAIN_LOW("Wüste (Hügelig)", Color.valueOf("#9e824d")),
        MARSH("Sumpf", Color.valueOf("#4b93ae")),
        OCEAN("Ozean", Color.valueOf("#081f82")),
        SNOW("Schnee/Gipfel (=Berg)", Color.valueOf("#ffffff")),
        INLAND_OCEAN_17("Meer", Color.valueOf("#375adc")),
        COASTAL_DESERT_18("Küste (Wüste)", Color.valueOf("#cbbf67")),
        COASTLINE("Küste", Color.valueOf("#fff700"));
        //not used - change Color string to hex
        /*TERRAIN_5("terrain_5 (=Grasland)", Color.valueOf("13,96,62")),
        TERRAIN_8("terrain_8 (=Hügel)", Color.valueOf("53,77,17")),
        TERRAIN_10("terrain_10 (=Ackerland)", Color.valueOf("155,155,155")),
        TERRAIN_11("terrain_11 (=Ackerland)", Color.valueOf("255,0,0")),
        //Wald nicht vorhanden?
        TERRAIN_12("terrain_12 (=Wald)", Color.valueOf("42,55,22")),
        TERRAIN_13("terrain_13 (=Wald)", Color.valueOf("213,144,199")),
        TERRAIN_14("terrain_14 (=Wald)", Color.valueOf("127,24,60"))*/;


        String text;
        Color color;

        TerrainType(String text, Color color) {
            this.text = text;
            this.color = color;
        }
    }
}
