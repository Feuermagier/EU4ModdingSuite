package firemage.moddingsuite.model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;

public class HeightmapProducer {

    public WritableImage createHeightmap(Image terrainMap) {
        WritableImage heightmap = new WritableImage((int)terrainMap.getWidth(), (int)terrainMap.getHeight());

        PixelReader reader = terrainMap.getPixelReader();
        PixelWriter writer = heightmap.getPixelWriter();

        for(int x=0; x<terrainMap.getWidth(); x++) {
            for(int y=0; y<terrainMap.getHeight(); y++) {
                TerrainType type = TerrainType.findTerrainByColor(reader.getColor(x, y));
                if(type == null) continue;
                double height = type.height/255.0;
                writer.setColor(x, y, new Color(height, height, height, 1));
            }
        }

        BufferedImage out = new BufferedImage((int)heightmap.getWidth(), (int)heightmap.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        out.createGraphics().drawImage(SwingFXUtils.fromFXImage(heightmap, null), 0, 0, null);

        Mat source = bufferedImageToMat(out);
        Mat destination = new Mat(source.rows(),source.cols(),source.type());
        Imgproc.GaussianBlur(source, destination,new Size(11,11), 0);

        return SwingFXUtils.toFXImage(Objects.requireNonNull(matToBufferedImage(destination)), null);
    }

    //https://stackoverflow.com/questions/14958643/converting-bufferedimage-to-mat-in-opencv
    private Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC1);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }

    //https://stackoverflow.com/questions/27086120/convert-opencv-mat-object-to-bufferedimage
    private BufferedImage matToBufferedImage(Mat matrix) {
        try {

            MatOfByte mob = new MatOfByte();
            Imgcodecs.imencode(".jpg", matrix, mob);
            byte ba[] = mob.toArray();

            return ImageIO.read(new ByteArrayInputStream(ba));

        } catch(IOException ex) {
            //will not occur
            return null;
        }
    }

}
