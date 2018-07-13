package firemage.colortablewriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;

public class Main {

    public static void main(String[] args) throws Exception {

        Console console = System.console();

        console.printf("Bild:\n");
        String readFile = console.readLine();
        console.printf("Ausgabedatei:\n");
        String writeFile = console.readLine();

        BufferedImage readImg = ImageIO.read(new File(readFile));
        IndexColorModel icm = (IndexColorModel) readImg.getColorModel();

        FileWriter writer = new FileWriter(new File(writeFile));

        writer.write(8 + "," + icm.getMapSize() + "\n");

        byte[] reds = new byte[1000];
        icm.getReds(reds);
        for(byte b : reds) writer.write(b + ",");
        writer.write("\n");

        byte[] blues = new byte[1000];
        icm.getBlues(blues);
        for(byte b : blues) writer.write(b + ",");
        writer.write("\n");

        byte[] greens = new byte[1000];
        icm.getGreens(greens);
        for(byte b : greens) writer.write(b + ",");
        writer.write("\n");

        writer.close();
    }
}
