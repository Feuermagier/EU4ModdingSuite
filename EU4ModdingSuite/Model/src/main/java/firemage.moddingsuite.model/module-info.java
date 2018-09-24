module firemage.moddingsuite.model {
	exports firemage.moddingsuite.model;
    exports firemage.moddingsuite.model.data;
    exports firemage.moddingsuite.model.map;
    exports firemage.moddingsuite.model.config;
    exports firemage.moddingsuite.model.util;

    requires java.desktop;
    requires javafx.graphics;
    requires javafx.swing;
    requires org.apache.logging.log4j;
    requires opencvjar;
}
