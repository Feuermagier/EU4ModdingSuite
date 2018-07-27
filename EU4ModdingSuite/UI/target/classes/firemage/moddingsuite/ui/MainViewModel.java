package firemage.moddingsuite.ui;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import firemage.moddingsuite.model.HeightmapProducer;
import firemage.moddingsuite.model.RivermapProducer;
import firemage.moddingsuite.model.data.FileProvider;
import firemage.moddingsuite.model.data.MapProvider;
import firemage.moddingsuite.model.map.EU4Map;
import firemage.moddingsuite.model.map.IndexedRealMap;
import firemage.moddingsuite.model.map.RealMap;
import firemage.moddingsuite.ui.layers.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Optional;

public class MainViewModel implements ViewModel {

    private static Logger logger = LogManager.getLogger(MainViewModel.class);

    private IntegerProperty layerSelectionIndex = new SimpleIntegerProperty();

    private DelegateCommand moveLayerUpCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() throws Exception {
            Layer prevUp = LayerProvider.getLayers().get(layerSelectionIndex.get()-1);
            LayerProvider.getLayers().set(layerSelectionIndex.get()-1, LayerProvider.getLayers().get(layerSelectionIndex.get()));
            LayerProvider.getLayers().set(layerSelectionIndex.get(), prevUp);
        }
    });
    private DelegateCommand moveLayerDownCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() throws Exception {
            Layer prevDown = LayerProvider.getLayers().get(layerSelectionIndex.get()+1);
            LayerProvider.getLayers().set(layerSelectionIndex.get()+1, LayerProvider.getLayers().get(layerSelectionIndex.get()));
            LayerProvider.getLayers().set(layerSelectionIndex.get(), prevDown);
        }
    });

    private DelegateCommand saveCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() {
            MainViewModel.this.save();
        }
    });

    private DelegateCommand produceHeightmapCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() {
            try {
                MainViewModel.this.produceHeightmap();
            }catch (Exception ex) {
                logger.error(ex.getMessage());
                new Alert(Alert.AlertType.ERROR, "Heightmap konnte nicht gespeichert werden (s. Log)", ButtonType.CLOSE).showAndWait();
            }
        }
    });

    private DelegateCommand produceRivermapCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() {
            MainViewModel.this.produceRivermap();
        }
    });

    public MainViewModel() {}

    //getter + setter

    int getLayerSelectionIndex() {
        return layerSelectionIndex.get();
    }

    IntegerProperty layerSelectionIndexProperty() {
        return layerSelectionIndex;
    }

    DelegateCommand getMoveLayerUpCommand() { return moveLayerUpCommand; }
    DelegateCommand getMoveLayerDownCommand() { return moveLayerDownCommand; }


    DelegateCommand getSaveCommand() { return saveCommand; }

    DelegateCommand getProduceHeightmapCommand() {
        return produceHeightmapCommand;
    }

    DelegateCommand getProduceRivermapCommand() { return produceRivermapCommand; }

    //privates

    private void save() {
        File dir = new File(FileProvider.getInstance().getWorkingLocation().getAbsolutePath() + "/map");
        logger.debug("Saving to " + dir.getAbsolutePath());
        dir.mkdirs();

        try {
            for(Layer layer : LayerProvider.getLayers()) {
                EU4Map map = layer.getMap();
                if(map instanceof RealMap) {

                        ImageIO.write(((RealMap)map).convertToSaveableImage(), "bmp", new File(dir.getAbsolutePath() + "/" + ((RealMap)map).getFilename() + ".bmp"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage());
            new Alert(Alert.AlertType.ERROR, "Konnte nicht speichern", ButtonType.CANCEL).showAndWait();
        }
    }

    private void produceHeightmap() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Die bestehende Heightmap wird überschrieben!", ButtonType.APPLY, ButtonType.CANCEL);
        Optional<ButtonType> button = alert.showAndWait();
        if(button.isPresent() && button.get().equals(ButtonType.APPLY)) {
            MapProvider.setHeightmapImage(new HeightmapProducer().createHeightmap(MapProvider.getTerrainMapImage()));
        }
    }

    private void produceRivermap() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Soll die bestehende Flusskarte überschrieben werden?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        Optional<ButtonType> button = alert.showAndWait();
        if(button.isPresent()) {
            if(button.get().equals(ButtonType.YES)) {
                MapProvider.setRiverMapImage(new RivermapProducer().createRivermap(MapProvider.getTerrainMapImage()));
            } else if(button.get().equals(ButtonType.NO)) {
                MapProvider.setRiverMapImage(new RivermapProducer().createRivermap(MapProvider.getTerrainMapImage(), MapProvider.getRiverMapImage()));
            }
        }
    }
}
