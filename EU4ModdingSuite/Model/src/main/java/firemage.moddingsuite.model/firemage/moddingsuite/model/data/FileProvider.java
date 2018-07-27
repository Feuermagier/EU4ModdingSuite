package firemage.moddingsuite.model.data;

import firemage.moddingsuite.model.Province;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

//singleton
public class FileProvider {

    private static final String DEFINITION_RELATIVE_PATH = "/map/definition.csv";
    private static final String PROVINCEMAP_RELATIVE_PATH = "/map/provinces.bmp";
    private static final String TERRAINMAP_RELATIVE_PATH = "/map/terrain.bmp";
    private static final String RIVERMAP_RELATIVE_PATH = "/map/rivers.bmp";
    private static final String HEIGHTMAP_RELATIVE_PATH = "/map/heightmap.bmp";

    private static Logger logger = LogManager.getLogger(FileProvider.class);

    private static FileProvider instance;

    private File workingLocation = null;
    private File steamLocation = null;

    private FileProvider() {}

    public static FileProvider getInstance() {
        if(instance == null) instance = new FileProvider();
        return instance;
    }

    public void setWorkingLocation(File location) throws  IllegalArgumentException {
        if(location == null || !location.isDirectory()) throw new IllegalArgumentException("location must be a non-null directory");
        workingLocation = location;
    }

    public void setSteamLocation(File location) throws  IllegalArgumentException {
        if(location == null || !location.isDirectory()) throw new IllegalArgumentException("location must be a non-null directory");
        steamLocation = location;
    }

    public File getWorkingLocation() {
        return workingLocation;
    }

    public void loadFiles() throws Exception {
        loadProvinces();
        loadTerrain();
        loadRivers();
        loadHeightmap();
    }

    public boolean hasLocations() {
        return workingLocation != null && steamLocation != null;
    }

    //privates---------------------------------------------------------------------------------------------------------------

    private void loadProvinces() throws Exception {
        ArrayList<Province> provinces = new ArrayList<>();

        //generate provinces
        logger.debug("generating provinces");
        try (BufferedReader definitonReader = new BufferedReader(new FileReader(createPath(DEFINITION_RELATIVE_PATH)))) {
            definitonReader.readLine(); //skip first line, only comment
            String line;
            while((line = definitonReader.readLine()) != null) {
                String[] split = line.split(";");
                provinces.add(new Province(Integer.valueOf(split[0]), split[4], new Color(Integer.valueOf(split[1])/255.0, Integer.valueOf(split[2])/255.0, Integer.valueOf(split[3])/255.0, 1.0)));
            }
            MapProvider.setProvinces(provinces);
        }
        Image image = new Image(createPath(PROVINCEMAP_RELATIVE_PATH).toURI().toString());
        MapProvider.setProvinceMapImage(new WritableImage(image.getPixelReader(), (int)image.getWidth(), (int)image.getHeight()));
    }

    private void loadTerrain() throws Exception {
        logger.debug("reading terrain map image from " + createPath(TERRAINMAP_RELATIVE_PATH).getAbsolutePath());

        Image image = new Image(createPath(TERRAINMAP_RELATIVE_PATH).toURI().toString());
        MapProvider.setTerrainMapImage(new WritableImage(image.getPixelReader(), (int)image.getWidth(), (int)image.getHeight()));
    }

    private void loadRivers() throws Exception {
        logger.debug("reading river map image from " + createPath(RIVERMAP_RELATIVE_PATH).getAbsolutePath());

        Image image = new Image(createPath(RIVERMAP_RELATIVE_PATH).toURI().toString());
        MapProvider.setRiverMapImage(new WritableImage(image.getPixelReader(), (int)image.getWidth(), (int)image.getHeight()));
    }

    private void loadHeightmap() throws Exception {
        logger.debug("reading heightmap image from " + createPath(HEIGHTMAP_RELATIVE_PATH).getAbsolutePath());

        Image image = new Image(createPath(HEIGHTMAP_RELATIVE_PATH).toURI().toString());
        MapProvider.setHeightmapImage(new WritableImage(image.getPixelReader(), (int)image.getWidth(), (int)image.getHeight()));
    }

    private File createPath(String relative) throws WrongLocationException {
            File file = new File(workingLocation.getAbsolutePath() + relative);
            if(file.exists()) return  file;
            file = new File(steamLocation.getAbsolutePath() + relative);
            if(file.exists()) return file;

            throw new WrongLocationException("EU4 steam folder seems to be wrong or damaged");
    }
}
