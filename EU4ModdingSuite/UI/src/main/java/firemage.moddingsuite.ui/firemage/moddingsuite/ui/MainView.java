package firemage.moddingsuite.ui;

import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.JavaView;
import firemage.moddingsuite.ui.layers.Layer;
import firemage.moddingsuite.ui.layers.LayerProvider;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class MainView extends AnchorPane implements JavaView<MainViewModel>, Initializable {

    public static final  int SCENE_BOTTOM_INSETS = 50;

    @FXML
    private VBox rootPane;

    @FXML
    private ListView<Layer> layerListView;

    @FXML
    private Pane centerPane;

    @FXML
    private ScrollPane palettePane;

    @InjectViewModel
    private MainViewModel viewModel;

    public MainView() throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainView.fxml"));
        loader.setController(this);
        Node rootNode = loader.load();
        getChildren().add(rootNode);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //initialize() is called twice (by FXMLLoader and by the mvvmFX framework) and the first time viewModel is null
        if(viewModel == null) {
            return;
        }

        getStylesheets().addAll(getClass().getResource("/css/modena_dark.css").toExternalForm(), this.getClass().getResource("MainView.css").toExternalForm());


        centerPane.setSnapToPixel(true);
        updateLayers();

        LayerProvider.getLayers().addListener((ListChangeListener<Layer>) c -> {
            updateLayers();
        });

        viewModel.layerSelectionIndexProperty().addListener((observable, oldValue, newValue) -> MainView.this.updateLayers());

        viewModel.layerSelectionIndexProperty().bind(layerListView.getSelectionModel().selectedIndexProperty());

        layerListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Layer> call(ListView<Layer> param) {
                ListCell<Layer> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Layer t, boolean empty) {
                        super.updateItem(t, empty);
                        if (t != null) setText(t.getName());
                    }
                };
                return cell;
            }
        });

        layerListView.setItems(LayerProvider.getLayers());
        layerListView.getSelectionModel().selectFirst();
    }

    public void updateLayers() {
        if(viewModel.getLayerSelectionIndex() < 0) return;

        centerPane.getChildren().clear();
        for(int i=LayerProvider.getLayers().size()-1; i>=viewModel.getLayerSelectionIndex(); i--) {
            try {
                centerPane.getChildren().add(LayerProvider.getLayers().get(i));
            } catch(IllegalArgumentException ex) {
                //ignore duplicate children exception
            }
        }
        palettePane.setContent(LayerProvider.getLayers().get(viewModel.getLayerSelectionIndex()).getPalette());
    }

    @FXML
    public void moveLayerUpAction() {
        viewModel.getMoveLayerUpCommand().execute();
    }

    @FXML
    public void moveLayerDownAction() {
        viewModel.getMoveLayerDownCommand().execute();
    }

    @FXML
    public void save() { viewModel.getSaveCommand().execute(); }

    @FXML
    public void createHeightmap() { viewModel.getProduceHeightmapCommand().execute(); }

    @FXML
    public void createRivermap() {viewModel.getProduceRivermapCommand().execute(); }

    @FXML
    public void createDummyProvincemap() {viewModel.getProduceDummyProvincemapCommand().execute(); }


    public void fitToScreen() {

        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        rootPane.setMaxSize(screen.getWidth(), screen.getHeight()-SCENE_BOTTOM_INSETS);
    }
}
