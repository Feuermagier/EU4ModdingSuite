package firemage.moddingsuite.preloader;

import firemage.moddingsuite.model.data.FileProvider;
import firemage.moddingsuite.starter.Starter;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class EU4Preloader extends Preloader {

    private static Logger logger = LogManager.getLogger(EU4Preloader.class);

    private Stage stage;
    private StringProperty workingPath = new SimpleStringProperty("C:\\Users\\Jakob\\Documents\\Paradox Interactive\\Europa Universalis IV\\mod\\dwarf_test");
    private StringProperty steamPath = new SimpleStringProperty("D:\\Steam\\steamapps\\common\\Europa Universalis IV");
    private BorderPane progressPane;

    private Starter starter;

    private boolean isLoading = false;

    private Scene createPreloaderScene() {


        StackPane stack = new StackPane();

        //loading
        progressPane = new BorderPane();
        progressPane.setStyle("-fx-background-color: lightgrey");
        ProgressIndicator indicator = new ProgressIndicator(-1);
        progressPane.setCenter(indicator);

        stack.getChildren().add(progressPane);

        //files
        VBox box = new VBox();
        box.setSpacing(10);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: lightgrey");

        HBox workingBox = new HBox();
        workingBox.setSpacing(5);
        TextField workingPathField = new TextField();
        workingPathField.setEditable(false);
        workingPathField.setPromptText("Mod-Speicherort");
        workingPathField.textProperty().bind(workingPath);
        Button workingFileChooserButton = new Button("Durchsuchen");
        workingBox.getChildren().addAll(workingPathField, workingFileChooserButton);
        workingFileChooserButton.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Mod-Speicherort wählen");
            File file = chooser.showDialog(stage);
            if(file != null) workingPath.set(file.getAbsolutePath());
        });
        box.getChildren().add(workingBox);

        HBox steamBox = new HBox();
        steamBox.setSpacing(5);
        TextField steamPathField = new TextField();
        steamPathField.setEditable(false);
        steamPathField.setPromptText("EU4-Speicherort");
        steamPathField.textProperty().bind(steamPath);
        Button steamFileChooserButton = new Button("Durchsuchen");
        steamBox.getChildren().addAll(steamPathField, steamFileChooserButton);
        steamFileChooserButton.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("EU4-Speicherort wählen");
            File file = chooser.showDialog(stage);
            if(file != null) steamPath.set(file.getAbsolutePath());
        });
        box.getChildren().add(steamBox);

        Button startButton = new Button("Ok");
        startButton.setOnAction(event -> changeState());
        box.getChildren().add(startButton);


        stack.getChildren().add(box);
        box.toFront();

        return new Scene(stack);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(event -> {Platform.exit();System.exit(0);});
        stage.setScene(createPreloaderScene());
        stage.show();
    }

    @Override
    public void handleProgressNotification(ProgressNotification pn) {

    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        starter = (Starter) evt.getApplication();
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            changeState();
        }
    }

    private void changeState() {

        logger.debug("changeState");

        if(workingPath.get() != null && steamPath.get() != null &! isLoading && starter != null) {
            FileProvider.getInstance().setSteamLocation(new File(steamPath.get()));
            FileProvider.getInstance().setWorkingLocation(new File(workingPath.get()));

            Platform.runLater(() -> progressPane.toFront());
            isLoading = true;
            starter.startLoading();
            logger.debug("loading...");

        } else if(isLoading) {
            logger.debug("loaded");
            Platform.runLater(() -> stage.hide());
        }
    }
}
