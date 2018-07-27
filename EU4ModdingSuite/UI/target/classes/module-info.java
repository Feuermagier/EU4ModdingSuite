module firemage.moddingsuite.ui {
			requires mvvmfx;
			requires javafx.graphics;
			requires javafx.controls;
			requires javafx.base;
			requires javafx.fxml;
			requires java.desktop;
			requires javafx.swing;
    requires firemage.moddingsuite.model;
    requires org.apache.logging.log4j;

    opens firemage.moddingsuite.ui;

			exports firemage.moddingsuite.ui;
    exports firemage.moddingsuite.ui.layers;
}
