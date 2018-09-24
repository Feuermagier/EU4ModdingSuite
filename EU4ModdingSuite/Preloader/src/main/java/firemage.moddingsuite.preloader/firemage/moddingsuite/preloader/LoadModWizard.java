package firemage.moddingsuite.preloader;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

class LoadModWizard {

    private SimpleStringProperty location = new SimpleStringProperty("");
    private boolean load = false;

    String show() {
        Stage stage = new Stage();
        stage.setTitle("Mod laden");
        stage.setAlwaysOnTop(true);

        VBox box = new VBox();
        box.setSpacing(5);
        box.setPadding(new Insets(10));

        box.getChildren().add(new Label("Pfad wählen:"));
        HBox locationBox = new HBox();
        locationBox.setSpacing(5);
        TextField pathField = new TextField();
        pathField.setEditable(false);
        pathField.setPromptText("Pfad zur Mod");
        pathField.textProperty().bind(location);
        Button fileChooserButton = new Button("Durchsuchen");
        locationBox.getChildren().addAll(pathField, fileChooserButton);
        fileChooserButton.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Pfad zur Mod wählen");
            File file = chooser.showDialog(stage);
            if(file != null) location.set(file.getAbsolutePath());
        });
        box.getChildren().add(locationBox);

        TextArea infoArea = new TextArea();
        infoArea.setWrapText(true);
        location.addListener((observable, oldValue, newValue) -> infoArea.setText(checkModDir(newValue)));
        box.getChildren().add(infoArea);

        Button loadButton = new Button("Laden");
        loadButton.setOnAction(event -> {
            load = true;
            stage.close();
        });
        box.getChildren().add(loadButton);

        stage.setScene(new Scene(box));
        stage.showAndWait();

        if(!load)
            return "";
        else
            return location.get();
    }

    private String checkModDir(String path) {
        if(path == null || path.equals("")) return "";
        File file = new File(path);
        if(!file.exists() |! file.isDirectory()) {
            return "Fehler: Dieser Pfad existiert nicht!";
        } else if(!file.canWrite()) {
            return "Ich kann in diesen Ordner nicht schreiben.";
        } else if(file.listFiles() == null || file.listFiles().length == 0) {
            return "Dieser Ordner ist leer.\n Wenn du eine neue Mod erstellen möchtest, schließe dieses Fenster und wähle 'Neue Mod erstellen'";
        } else {
            String ret = "";
            if(new File(file.getAbsolutePath() + "\\map").exists() && Objects.requireNonNull(new File(file.getAbsolutePath() + "\\map").listFiles()).length != 0) {
                ret+="Modifiziert die Karte\n";
            }
            //TODO: Add more possible mod features

            return ret;
        }
    }
}
