package firemage.moddingsuite.preloader;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

class CreateNewModWizard {

    private SimpleStringProperty location = new SimpleStringProperty();

    File show() {
        Stage stage = new Stage();
        stage.setTitle("Neue Mod erstellen");
        stage.setAlwaysOnTop(true);

        VBox box = new VBox();

        box.getChildren().add(new Label("Pfad wählen:"));
        HBox locationBox = new HBox();
        locationBox.setSpacing(5);
        TextField steamPathField = new TextField();
        steamPathField.setEditable(false);
        steamPathField.setPromptText("Pfad zur Mod wählen");
        steamPathField.textProperty().bind(location);
        Button steamFileChooserButton = new Button("Durchsuchen");
        locationBox.getChildren().addAll(steamPathField, steamFileChooserButton);
        steamFileChooserButton.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Pfad zur Mod wählen");
            File file = chooser.showDialog(stage);
            if(file != null) location.set(file.getAbsolutePath());
        });
        box.getChildren().add(locationBox);

        Button loadButton = new Button("Laden");
        loadButton.setOnAction(event -> {
            if(!location.get().equals("")) stage.close();
        });

        stage.setScene(new Scene(box));
        stage.showAndWait();

        return new File(location.get());
    }
}

