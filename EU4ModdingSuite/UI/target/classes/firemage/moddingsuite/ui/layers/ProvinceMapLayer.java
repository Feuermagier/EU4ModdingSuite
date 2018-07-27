package firemage.moddingsuite.ui.layers;

import firemage.moddingsuite.model.data.MapProvider;
import firemage.moddingsuite.model.map.ProvinceMap;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProvinceMapLayer extends Layer {

    private static Logger logger = LogManager.getLogger(ProvinceMapLayer.class);

    public ProvinceMapLayer() {

        super("Provinzen", new ProvinceMap(), new ProvincePalette());

        Tooltip tooltip = new Tooltip("");

        this.setOnMouseMoved(event -> {
            try {
                tooltip.setText(MapProvider.getProvinceAt((int) event.getX(), (int) event.getY()).getName());
            } catch(NullPointerException ex) {
                logger.error(ex.getMessage() + " MouseX: " + event.getX() + " MouseY: " + event.getY());
            }
            tooltip.show(this, event.getScreenX()+10, event.getScreenY());
        });

        this.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.SECONDARY) {
                ((ProvincePalette)super.getPalette()).setSelectedProvince(MapProvider.getProvinceAt((int)event.getX(), (int)event.getY()));
            }
        });

        this.setOnMouseExited(event -> tooltip.hide());
    }
}
