package firemage.moddingsuite.main;

import firemage.moddingsuite.starter.Starter;

import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;

import java.time.LocalDateTime;

public class Main {

    private static Logger logger = LogManager.getLogger(Main.class);

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String...args){
        ///////////////////////////////////////////////
        //Set -Dlog4j.configurationFile=log4j2.xml!!!//
        ///////////////////////////////////////////////
        //System.setProperty("log4j.configurationFile", "log4j2.xml");
        System.setProperty("javafx.preloader", "firemage.moddingsuite.preloader.EU4Preloader");

        logger.info("Application started at " + LocalDateTime.now().toString());

        Application.launch(Starter.class, args);
    }
}
