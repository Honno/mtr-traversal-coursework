package mtr;
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
 
public class Importer {
    public static HashMap<String, String[]> generateMap() {
        // HashMap to represent MTR, with the MTR line's name as key and the
        // respective terminis as the value
        HashMap<String, String[]> map = new HashMap<String, String[]>();
 
        String path = "resources/MTRsystem_partial.csv";
        String line = "";
 
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
 
            while ((line = br.readLine()) != null) {
 
                String[] lineElements = line.split(",");
                map.put(lineElements[0], Arrays.copyOfRange(lineElements, 1, lineElements.length));
            }
 
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
 
        return map;
    }
}