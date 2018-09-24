module firemage.moddingsuite.preloader {
    requires javafx.graphics;
    requires javafx.controls;
    requires firemage.moddingsuite.model;
    requires firemage.moddingsuite.starter;
    requires org.apache.logging.log4j;

    requires controlsfx;
    requires java.desktop;

    exports firemage.moddingsuite.preloader;
}
