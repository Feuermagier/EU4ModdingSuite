package firemage.moddingsuite.model.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileUtil {

    public static int[] readIntegerCSVFile(File file, int line) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int lineCounter = 0;
            while(true) {
                String text = br.readLine();
                if(lineCounter == line) {
                    String[] stringValues = text.split(",");
                    int[] intValues = new int[stringValues.length];
                    for(int i=0; i<stringValues.length; i++) intValues[i] = Integer.valueOf(stringValues[i]);
                    return intValues;
                }
                lineCounter++;
            }
        }
    }

    public static byte[] readByteCSVFile(File file, int line) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int lineCounter = 0;
            while(true) {
                String text = br.readLine();
                if(lineCounter == line) {
                    String[] stringValues = text.split(",");
                    byte[] byteValues = new byte[stringValues.length];
                    for(int i=0; i<stringValues.length; i++) byteValues[i] = Byte.valueOf(stringValues[i]);
                    return byteValues;
                }
                lineCounter++;
            }
        }
    }
}
