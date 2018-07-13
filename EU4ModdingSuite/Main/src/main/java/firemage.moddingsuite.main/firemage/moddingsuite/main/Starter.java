package firemage.moddingsuite.main;

import firemage.moddingsuite.model.data.MapProvider;
import firemage.moddingsuite.ui.MainView;
import firemage.moddingsuite.ui.MainViewModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.FluentViewLoader;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.time.LocalDateTime;


public class Starter extends Application {

    private static Logger logger = LogManager.getLogger( Starter.class );

    public static final int MAP_WIDTH = 5632;
    public static final int MAP_HEIGHT = 2048;

    public static void main(String...args){
        /////////////////////////////////////////////////
        //  Set -Dlog4j.configurationFile=log4j2.xml!!!//
        /////////////////////////////////////////////////
        //System.setProperty("log4j.configurationFile", "log4j2.xml");

        logger.info("Application started at " + LocalDateTime.now().toString());

        MapProvider.setMapSize(MAP_WIDTH, MAP_HEIGHT);

        Application.launch(args);
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
        stage.show();
    }
}
