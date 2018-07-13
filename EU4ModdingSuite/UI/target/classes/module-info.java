module firemage.moddingsuite.ui {
			requires mvvmfx;
			requires javafx.graphics;
			requires javafx.controls;
			requires javafx.base;
			requires javafx.fxml;
			requires java.desktop;
			requires javafx.swing;
    requires firemage.moddingsuite.model;

    opens firemage.moddingsuite.ui;

			exports firemage.moddingsuite.ui;
}
