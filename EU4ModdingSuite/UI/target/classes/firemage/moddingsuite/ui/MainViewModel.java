package firemage.moddingsuite.ui;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import firemage.moddingsuite.model.data.MapProvider;
import firemage.moddingsuite.model.map.EU4Map;
import firemage.moddingsuite.model.map.TerrainMap;
import firemage.moddingsuite.ui.layers.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.FileWriter;

public class MainViewModel implements ViewModel {

    private IntegerProperty layerSelectionIndex = new SimpleIntegerProperty();

    private DelegateCommand moveLayerUpCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() throws Exception {
            Layer prevUp = layers.get(layerSelectionIndex.get()-1);
            layers.set(layerSelectionIndex.get()-1, layers.get(layerSelectionIndex.get()));
            layers.set(layerSelectionIndex.get(), prevUp);
        }
    });
    private DelegateCommand moveLayerDownCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() throws Exception {
            Layer prevDown = layers.get(layerSelectionIndex.get()+1);
            layers.set(layerSelectionIndex.get()+1, layers.get(layerSelectionIndex.get()));
            layers.set(layerSelectionIndex.get(), prevDown);
        }
    });

    private DelegateCommand saveCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() {
            MainViewModel.this.save();
        }
    });

    private ObservableList<Layer> layers  = FXCollections.observableArrayList();

    public MainViewModel() {
        layers.add(new Layer("Terrain", new TerrainMap(MapProvider.getWidth(), MapProvider.getHeight()), new TerrainPalette()));
    }

    //getter + setter

    ObservableList<Layer> getLayers() {
        return layers;
    }

    public void addLayer(EU4Map map, String name, LayerPalette palette) {
        layers.add( new Layer(name, map, palette));
    }

    int getLayerSelectionIndex() {
        return layerSelectionIndex.get();
    }

    IntegerProperty layerSelectionIndexProperty() {
        return layerSelectionIndex;
    }

    public void setLayerSelectionIndex(int layerSelectionIndex) {
        this.layerSelectionIndex.set(layerSelectionIndex);
    }

    DelegateCommand getMoveLayerUpCommand() { return moveLayerUpCommand; }
    DelegateCommand getMoveLayerDownCommand() { return moveLayerDownCommand; }


    DelegateCommand getSaveCommand() { return saveCommand; }

    //privates

    private void save() {
        try {
            ImageIO.write(layers.get(getLayerSelectionIndex()).getMap().convertToSaveableImage(), "bmp", new FileChooser().showSaveDialog(null));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
