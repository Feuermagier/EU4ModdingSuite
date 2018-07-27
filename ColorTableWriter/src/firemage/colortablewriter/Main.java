package firemage.colortablewriter;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.ImageObserver;
import java.awt.image.IndexColorModel;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {

        /*
        Console console = System.console();

        console.printf("Bild:\n");
        String readFile = console.readLine();
        console.printf("Ausgabedatei:\n");
        String writeFile = console.readLine();

        BufferedImage readImg = ImageIO.read(new File(readFile));
        IndexColorModel icm = (IndexColorModel) readImg.getColorModel();

        FileWriter writer = new FileWriter(new File(writeFile));

        writer.write(8 + "," + icm.getMapSize() + "\n");

        byte[] reds = new byte[256];
        icm.getReds(reds);
        for(byte b : reds) { writer.write(createColorValue(b) + ","); }
        writer.write("\n");

        byte[] blues = new byte[256];
        icm.getBlues(blues);
        for(byte b : blues) { writer.write(createColorValue(b) + ","); }
        writer.write("\n");

        byte[] greens = new byte[256];
        icm.getGreens(greens);
        for(byte b : greens) { writer.write(createColorValue(b) + ","); }
        writer.write("\n");

        writer.close();

        */

        testIndexedColor();
    }

    private static int createColorValue(byte b) {
        return (int) b <0?256+ (int) b : (int) b;
    }

    //https://stackoverflow.com/questions/41958769/how-to-save-indexed-color-png-in-java
    private static void testIndexedColor() throws IOException {
        File in = new File("terrain.bmp");
        File out = new File("test_result.bmp");

        try (ImageInputStream input = ImageIO.createImageInputStream(in);
             ImageOutputStream output = ImageIO.createImageOutputStream(out)) {
            ImageReader reader = ImageIO.getImageReaders(input).next(); // Will fail if no reader
            reader.setInput(input);

            ImageWriter writer = ImageIO.getImageWriter(reader); // Will obtain a writer that understands the metadata from the reader
            writer.setOutput(output);  // Will fail if no writer

            // Now, the important part, we'll read the pixel AND metadata all in one go
            IIOImage image = reader.readAll(0, null); // PNGs only have a single image, so index 0 is safe

            BufferedImage bi = (BufferedImage) image.getRenderedImage();
            WritableImage fx = new WritableImage(bi.getWidth(), bi.getHeight());

            /*PixelWriter fxWriter = fx.getPixelWriter();
            for(int x=0; x<bi.getWidth(); x++) {
                for(int y=0; y<bi.getHeight(); y++) {
                    fxWriter.setArgb(x, y, bi.getRGB(x, y));
                }
            }*/

            fx = SwingFXUtils.toFXImage(bi, null);

            PixelReader fxReader = fx.getPixelReader();
                for(int x=0; x<bi.getWidth(); x++) {
                    for(int y=0; y<bi.getHeight(); y++) {
                        bi.setRGB(x, y, fxReader.getArgb(x, y));
                    }
                }

            // Write pixel and metadata back
            writer.write(null, image, writer.getDefaultWriteParam());
        }
    }
}
