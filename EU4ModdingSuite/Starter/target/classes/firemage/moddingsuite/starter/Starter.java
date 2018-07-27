package firemage.moddingsuite.starter;

import firemage.moddingsuite.model.data.FileProvider;
import firemage.moddingsuite.model.data.MapProvider;
import firemage.moddingsuite.model.map.Heightmap;
import firemage.moddingsuite.model.map.RiverMap;
import firemage.moddingsuite.model.map.TerrainMap;
import firemage.moddingsuite.ui.MainView;
import firemage.moddingsuite.ui.MainViewModel;
import firemage.moddingsuite.ui.layers.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.FluentViewLoader;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Starter extends Application {

    private static Logger logger = LogManager.getLogger( Starter.class );
    private boolean startLoading = false;

    @Override
    public void init() throws  Exception {
        while(!startLoading) {Thread.sleep(100);}   //wait for preloader
        FileProvider.getInstance().loadFiles();

        MapProvider.updateProvinceMap();

        ObservableList<Layer> layers = FXCollections.observableArrayList();

        layers.add(new RealMapLayer("Terrain", new TerrainMap(), new TerrainPalette()));
        layers.add(new RealMapLayer("Flüsse", new RiverMap(), new RiverPalette()));
        layers.add(new RealMapLayer("Heightmap", new Heightmap(), new EmptyPalette("Heightmap")));  //painting disabled -> TODO: Enable painting
        layers.add(new ProvinceMapLayer());
        //layers.add(new ConstructedMapLayer("Blue", new ConstructedMap(MapProvider.getWidth(), MapProvider.getHeight(), p -> p.getColor()), new EmptyPalette("Blue")));

        LayerProvider.setLayers(layers);
    }


    @Override
    public void start(Stage stage){
        stage.setTitle("EU4 Modding Suite");
        stage.setMaximized(true);
        stage.setResizable(true);
        ViewTuple<MainView, MainViewModel> viewTuple = FluentViewLoader.javaView(MainView.class).load();
        Pane root = (Pane)viewTuple.getView();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        viewTuple.getCodeBehind().fitToScreen();
        mayBeShow(stage);
    }

    private void mayBeShow(Stage stage) {
        if(FileProvider.getInstance().hasLocations()) {
            Platform.runLater(stage::show);
        } else {
            logger.fatal("Location(s) not set");
            System.exit(1);
        }
    }

    public void startLoading() {
        startLoading = true;
    }
}
