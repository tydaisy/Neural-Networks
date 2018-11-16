import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtil {

    public static String[] readFile(String filename) {
        try {

            FileReader fr = new FileReader(filename);
            BufferedReader bfr = new BufferedReader(fr);
            bfr.readLine();
            ArrayList<String> content = new ArrayList<String>();
            String paragraph = null;
            while ((paragraph = bfr.readLine()) != null) {
                content.add(paragraph);
            }
            String[] paragraphs = new String[content.size()];
            for (int i = 0; i < content.size(); i++) {
                paragraphs[i] = content.get(i);
            }
            bfr.close();
            return paragraphs;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O Ooops: " + e.getMessage());
        }

        String[] paragraphs = new String[1];
        paragraphs[0] = "";
        return paragraphs;
    }
}
