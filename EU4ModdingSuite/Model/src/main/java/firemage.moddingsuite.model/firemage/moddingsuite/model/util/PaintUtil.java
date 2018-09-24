package firemage.moddingsuite.model.util;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.image.*;

public class PaintUtil {

    public static void line(int x0, int y0, int x1, int y1, PixelWriter writer, Color color) {
        //Bresenham-Algorithmus
        int dx = Math.abs(x1-x0), sx = x0<x1 ? 1 : -1;
        int dy = -Math.abs(y1-y0), sy = y0<y1 ? 1 : -1;
        int err = dx+dy, e2;
        while (true) {
            //custom - draw rect
            for(int i=-5; i<=5; i++) {
                for (int q = -5; q <= 5; q++) {
                    writer.setColor(x0+i, y0+q, color);
                }
            }
            if (x0==x1 && y0==y1) break;
            e2 = 2*err;
            if (e2 > dy) { err += dy; x0 += sx; } /* e_xy+e_x > 0 */
            if (e2 < dx) { err += dx; y0 += sy; } /* e_xy+e_y < 0 */
        }
    }

    public static WritableImage makeImageTransparent(WritableImage im, final double opacity) {
        /*
        ImageFilter filter = new RGBImageFilter() {
            public final int filterRGB(int x, int y, int rgb) {
                // Mark the alpha bits as zero - transparent
                return 0x00FFFFFF & rgb;
            }
        };

        ImageProducer ip = new FilteredImageSource(SwingFXUtils.fromFXImage(im, null).getSource(), filter);
        return SwingFXUtils.toFXImage(imageToBufferedImage(Toolkit.getDefaultToolkit().createImage(ip)), null);
        */
        WritableImage out = new WritableImage((int)im.getWidth(), (int)im.getHeight());
        PixelReader reader = im.getPixelReader();
        PixelWriter writer = out.getPixelWriter();
        for(int x=0; x<im.getWidth(); x++) {
            for(int y=0; y<im.getHeight(); y++) {
                Color c = reader.getColor(x, y);
                writer.setColor(x, y, new Color(c.getRed(), c.getGreen(), c.getBlue(), opacity));
            }
        }
        return out;
    }

    private static BufferedImage imageToBufferedImage(Image image) {

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return bufferedImage;

    }
}
