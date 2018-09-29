package firemage.moddingsuite.preloader.wizards;

import firemage.moddingsuite.model.data.ColorModelProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;
import org.controlsfx.tools.ValueExtractor;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CreateNewModWizardManager {

    private static final Logger logger = LogManager.getLogger(CreateNewModWizardManager.class);

    private static final String MOD_NAME_ID = "modName";
    private static final String GAME_VERSiON_ID = "gameVersion";
    private static final String WORKING_PATH_ID = "workingPath";
    private static final String MOD_TYPE_ID = "modType";
    private static final String MAP_HEIGHT_ID = "mapHeight";
    private static final String MAP_WIDTH_ID = "mapWidth";


    /**
     * Creates and shows a wizard for creating a new mod and then writes all required files.
     * @return The path to the directory where the mod was created or null if the user cancelled the wizard
     */
    public File showAndWait() {

        Wizard wizard = new Wizard();
        Stack<AdvancedWizardPane> pages = new Stack<>();
        pages.push(new FinalWizardPage());
        pages.push(new ModTypeWizardPage());
        pages.push(new GameVersionWizardPage());
        pages.push(new LocationWizardPage());
        pages.push(new NameWizardPage());


        Wizard.Flow flow = new Wizard.Flow() {
            @Override
            public Optional<WizardPane> advance(WizardPane wizardPane) {
                return Optional.of(getNext(wizardPane));
            }

            @Override
            public boolean canAdvance(WizardPane wizardPane) {
                return !(pages.empty());
            }

            private WizardPane getNext(WizardPane current) {
                if(current instanceof ModTypeWizardPage && ((ModTypeWizardPage)current).getSelectedModType().showMapPages) {
                    pages.push(new SizeWizardPage());
                }
                wizard.invalidProperty().bind(pages.peek().getValidation().invalidProperty());
                return pages.pop();
            }
        };
        wizard.setFlow(flow);
        ValueExtractor.addObservableValueExtractor(c -> c instanceof Spinner,        c -> ((Spinner)c).valueProperty());
        ValueExtractor.addValueExtractor( n -> n instanceof Spinner, n -> ((Spinner)n).getValue());
        Optional<ButtonType> result = wizard.showAndWait();
        if(result.isPresent() && result.get().equals(ButtonType.FINISH)) {
            logger.debug("Wizard closed; settings are: " + wizard.getSettings());
            ObservableMap<String, Object> settings = wizard.getSettings();
            File path = new File( settings.get(WORKING_PATH_ID) + "\\" + settings.get(MOD_NAME_ID));
            path.mkdirs();
            createModFile(path, (String)settings.get(MOD_NAME_ID), (String)settings.get(GAME_VERSiON_ID));
            if(((ModType)settings.get(MOD_TYPE_ID)).showMapPages)
                createMapImages(path, (int)settings.get(MAP_WIDTH_ID), (int)settings.get(MAP_HEIGHT_ID));

            return path.getAbsoluteFile();
        }

        return null;
    }

    private boolean createMapImages(File path, int width, int height) {
        try {
            path = new File(path.getAbsolutePath() + "\\map");
            path.mkdir();
            ImageIO.write(new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, ColorModelProvider.getTerrainColorModel()), "bmp", new File(path.getAbsolutePath() + "\\terrain.bmp"));
            ImageIO.write(createFilledImage(new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, ColorModelProvider.getRiverColorModel()), Color.WHITE), "bmp", new File(path.getAbsolutePath() + "\\rivers.bmp"));
            ImageIO.write(new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR), "bmp", new File(path.getAbsolutePath() + "\\" + "provinces.bmp"));
            ImageIO.write(createFilledImage(new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY), new Color(96, 96, 96)), "bmp", new File(path.getAbsolutePath() + "\\" + "heightmap.bmp"));
        } catch (IOException ex) {
            logger.error("Can't create new map files", ex);
            return false;
        }
        return true;
    }

    private BufferedImage createFilledImage(BufferedImage img, Color color) {
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        return img;
    }

    private boolean createModFile(File path, String modName, String gameVersion) {
        File file = new File(path.getParent() + "\\" + modName + ".mod");
        logger.debug(file.getAbsolutePath());
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("name = \"" + modName + "\"\n");
            writer.write("path = \"mod/" + modName + "\"\n");
            writer.write("supported_version = \"" + gameVersion + "\"\n");
            writer.flush();
        } catch (IOException ex) {
            logger.error("Can't write mod file", ex);
            return false;
        }
        return true;
    }

    private class SizeWizardPage extends AdvancedWizardPane {

        @Override
        Node createHeader() {
            setHeaderText("Wie groß soll deine Karte werden?");
            return null;
        }

       @Override
        GridPane createContent() {
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            Spinner<Integer> widthInput = new Spinner<>(2048, 5632, 2048, 256);
            widthInput.setEditable(false);
            Spinner<Integer> heightInput = new Spinner<>(256, 2048, 2048, 256);
            heightInput.setEditable(false);

            grid.add(new Label("Breite (in px):"), 0, 0);
            grid.add(widthInput, 1, 0);
            grid.add(new Label("Höhe (in px):"), 0, 1);
            grid.add(heightInput, 1, 1);

            widthInput.setId(MAP_WIDTH_ID);
            heightInput.setId(MAP_HEIGHT_ID);
            return grid;
        }
    }

    private class ModTypeWizardPage extends AdvancedWizardPane {

        private ModType selectedModType = null;

        @Override
        Node createHeader() {
            setHeaderText("Wie viel möchtest du verändern?");
            return null;
        }

        @Override
        Node createContent() {
            ListView<ModType> typeList = new ListView<>(FXCollections.observableArrayList(ModType.values()));
            typeList.setCellFactory(new Callback<>() {
                @Override
                public ListCell<ModType> call(ListView<ModType> param) {
                    return new ListCell<>() {
                        @Override
                        protected void updateItem(ModType t, boolean empty) {
                            super.updateItem(t, empty);
                            if (t != null) setText(t.text);
                        }
                    };
                }
            });
            typeList.setId(MOD_TYPE_ID);
            typeList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedModType = newValue);
            return typeList;
        }

        ModType getSelectedModType() {
            return selectedModType;
        }
    }

    private class NameWizardPage extends AdvancedWizardPane {

        @Override
        Node createHeader() {
            setHeaderText("Als erstes benenne dein Werk (Arbeitstitel):");
            return null;
        }
        @Override
        Node createContent() {
            TextField text = new TextField();
            text.setPromptText("Name");
            text.setId(MOD_NAME_ID);
            getValidation().registerValidator(text, true, Validator.createEmptyValidator("Name ist leer!"));
            return text;
        }
    }
    private class FinalWizardPage extends AdvancedWizardPane {

        @Override
        Node createHeader() {
            setHeaderText("Los geht's!");
            return null;
        }
        @Override
        Node createContent() {
            return null;
        }
    }
    private class LocationWizardPage extends AdvancedWizardPane {

        @Override
        Node createHeader() {
            setHeaderText("Wo soll ich deine Mod speichern? (Standard ist der Windows-Mod-Pfad)");
            return null;
        }
        @Override
        Node createContent() {
            HBox box = new HBox();
            box.setSpacing(10);

            TextField pathField = new TextField(System.getProperty("user.home") + "\\Documents\\Paradox Interactive\\Europa Universalis IV\\mod");
            pathField.setEditable(false);
            pathField.setPromptText("Pfad");
            pathField.setId(WORKING_PATH_ID);
            Button dirChooserButton = new Button("Durchsuchen");
            dirChooserButton.setOnAction(event -> {
                DirectoryChooser dirChooser = new DirectoryChooser();
                pathField.setText(dirChooser.showDialog(null).getAbsolutePath());
            });

            box.getChildren().addAll(pathField, dirChooserButton);
            return box;
        }
    }

    private class GameVersionWizardPage extends AdvancedWizardPane {

        private static final String GAME_VERSION_REGX = "^\\d\\.\\d{1,2}$";

        @Override
        Label createHeader() {
            setHeaderText("Für welche Version von EU4 ist deine Mod gedacht?");
            return null;
        }

        @Override
        Node createContent() {
            this.setPadding(new Insets(20));

            TextField text = new TextField();
            text.setPromptText("Name");
            text.setId(GAME_VERSiON_ID);
            Validator<String> validator = (control, s) -> ValidationResult.fromErrorIf(text, "Format falsch (z.B. 1.25)", !s.matches(GAME_VERSION_REGX));

            super.getValidation().registerValidator(text, true, validator);

            return text;
        }
    }

    private enum ModType {
        MAP("Vollständig neue Karte erstellen", true),
        MINOR_MAP("Kleinere Veränderung der bestehenden Karte", false);

        public String text;
        public boolean showMapPages;
        ModType(String text, boolean showMapPages) {
            this.text = text;
            this.showMapPages = showMapPages;
        }
    }
}
