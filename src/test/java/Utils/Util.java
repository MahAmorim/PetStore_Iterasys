package Utils;

import java.io.File;
import java.io.IOException;

import static org.testng.reporters.Files.readFile;

public class Util {

    public static String readJson(String path) {

        String jsonData = null;
        {
            try {
                jsonData = readFile(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonData;
        }
    }
}