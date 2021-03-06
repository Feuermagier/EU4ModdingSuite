module firemage.moddingsuite.main {
	requires javafx.base;
	requires mvvmfx;
	requires javafx.graphics;

	requires firemage.moddingsuite.ui;
    requires org.apache.logging.log4j;
    requires firemage.moddingsuite.model;
	requires firemage.moddingsuite.starter;
	requires firemage.moddingsuite.preloader;
    requires opencvjar;

    exports firemage.moddingsuite.main;
}
