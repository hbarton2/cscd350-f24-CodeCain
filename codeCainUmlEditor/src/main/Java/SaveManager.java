import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;


public class SaveManager {

    public static void saveUMLToJson(Object umlObject, String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(filePath), umlObject);
            System.out.println("UML saved to JSON file successfully.");
        } catch (IOException e) {
            System.err.println("Error saving UML to JSON file: " + e.getMessage());
        }
    }

    public static <T> T loadUMLFromJson(String filePath, Object umlObject ){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), umlObject);
            System.out.println("Loaded your previously saved data");
        } catch (Exception e) {
            System.err.println("Error loading previously saved data" + e.getMessage()); 
            return null;
        }

    }
}