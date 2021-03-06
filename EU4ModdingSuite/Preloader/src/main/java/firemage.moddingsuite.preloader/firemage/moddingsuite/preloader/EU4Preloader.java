package firemage.moddingsuite.preloader;

import firemage.moddingsuite.model.config.ConfigParam;
import firemage.moddingsuite.model.config.GlobalConfiguration;
import firemage.moddingsuite.model.data.FileProvider;
import firemage.moddingsuite.preloader.wizards.CreateNewModWizardManager;
import firemage.moddingsuite.starter.Starter;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class EU4Preloader extends Preloader {

    private static Logger logger = LogManager.getLogger(EU4Preloader.class);

    private Stage stage;
    private String workingPath = null;
    private StringProperty steamPath = new SimpleStringProperty("");
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

        box.getChildren().add(new Label("Pfad zur EU4-Installation (z.B. C:\\Program Files\\Steam\\steamapps\\common\\Europa Universalis IV)"));

        HBox steamBox = new HBox();
        steamBox.setSpacing(5);
        TextField steamPathField = new TextField();
        steamPathField.setEditable(false);
        steamPathField.setPromptText("EU4-Speicherort");
        steamPathField.textProperty().bind(steamPath);
        //property
        if(GlobalConfiguration.getInstance().getProperty(ConfigParam.EU4_INSTALL_DIR) != null)
            steamPath.set(GlobalConfiguration.getInstance().getProperty(ConfigParam.EU4_INSTALL_DIR));
        steamPath.addListener((observable, oldValue, newValue) -> {
            GlobalConfiguration.getInstance().setProperty(ConfigParam.EU4_INSTALL_DIR, newValue);
            try {
                GlobalConfiguration.getInstance().save();
            } catch (IOException e) {
                logger.error("Cannot save config file", e);
            }
        });
        Button steamFileChooserButton = new Button("Durchsuchen");
        steamBox.getChildren().addAll(steamPathField, steamFileChooserButton);
        steamFileChooserButton.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("EU4-Speicherort wählen");
            File file = chooser.showDialog(stage);
            if(file != null) steamPath.set(file.getAbsolutePath());
        });
        box.getChildren().add(steamBox);

        HBox buttonBox = new HBox();

        Button loadModButton = new Button("Mod laden");
        loadModButton.setOnAction(event -> {
            workingPath = new LoadModWizard().show();
            if(workingPath != null &! workingPath.equals(""))
                changeState();
        });
        Button createNewModButton = new Button("Neue Mod erstellen");
        createNewModButton.setOnAction(event -> {
            File file = new CreateNewModWizardManager().showAndWait();
            if(file != null) {
                workingPath = file.getAbsolutePath();
                changeState();
            }
        });
        buttonBox.getChildren().addAll(loadModButton, createNewModButton);

        box.getChildren().add(buttonBox);


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

        if(steamPath.get() != null && workingPath != null &! isLoading && starter != null) {
            FileProvider.getInstance().setSteamLocation(new File(steamPath.get()));
            FileProvider.getInstance().setWorkingLocation(new File(workingPath));
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
