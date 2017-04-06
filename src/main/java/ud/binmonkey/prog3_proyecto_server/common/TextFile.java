package ud.binmonkey.prog3_proyecto_server.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextFile {
    public static String read(String file) throws IOException {
        String content = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            content = sb.toString();
        } finally {
            br.close();
        }
        return content;
    }
}
