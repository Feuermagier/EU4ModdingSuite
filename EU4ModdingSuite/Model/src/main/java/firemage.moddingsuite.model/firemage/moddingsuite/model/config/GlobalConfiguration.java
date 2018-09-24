package firemage.moddingsuite.model.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class GlobalConfiguration extends Properties {

    private static final Logger logger = LogManager.getLogger(GlobalConfiguration.class);

    private static final String FILENAME = "settings.conf";

    private static GlobalConfiguration instance;

    public static boolean checkForFileExistence() {
        return new File(FILENAME).exists();
    }

    public static GlobalConfiguration getInstance() {
        try {
            if (instance == null) instance = new GlobalConfiguration();
        } catch(IOException ex) {
            logger.fatal("Cannot find settings.conf");
            System.exit(1);
        }

        return instance;
    }

    private GlobalConfiguration() throws IOException {
        this.load(new FileReader(FILENAME));
    }

    @Override
    public String getProperty(String key) {
        throw new UnsupportedOperationException("GlobalConfiguration does not support direct access by string key. Use getProperty(ConfigParam) instead");
    }


    @Override
    public String setProperty(String key, String value) {
        throw new UnsupportedOperationException("GlobalConfiguration does not support direct access by string key. Use setProperty(ConfigParam) instead");
    }

    public String getProperty(ConfigParam param) {
        return super.getProperty(param.param);
    }

    public void setProperty(ConfigParam param, String value) { super.setProperty(param.param, value); }

    public void save() throws IOException {
        super.store(new FileWriter(FILENAME), "Settings file");
    }
}
