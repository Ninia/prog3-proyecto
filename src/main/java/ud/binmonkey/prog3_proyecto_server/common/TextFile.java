package ud.binmonkey.prog3_proyecto_server.common;

import java.io.*;

public class TextFile {

    /**
     * Returns string with the content of a textfile
     * @param file file to be read
     * @return string with the content of the file
     * @throws IOException
     */
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

    /**
     * Checks if line starts with a string
     * @param fileName file to check
     * @param condition starting string
     * @return
     * @throws IOException
     */
    public static boolean startsWith(String fileName, String condition) throws IOException {
        String f = read(fileName);
        return f.startsWith(condition);
    }

    /**
     * Deletes last n lines of a text file
     * @param fileName name of the file to be modified
     * @param lines number of ending lines to be removed
     * @throws IOException
     */
    public static void deleteLastLines(String fileName, int lines) throws IOException {
        int count = 0;
        byte b;
        RandomAccessFile f = new RandomAccessFile(fileName, "rw");
        while (count < lines) {
            long length = f.length() - 1;
            do {
                length -= 1;
                f.seek(length);
                b = f.readByte();
            } while(b != 10 && length > 0);
            if (length == 0) {
                f.setLength(length);
            } else {
                f.setLength(length + 1);
            }
            count++;
        }
    }

    /**
     * Deletes last n lines of a text file if it ends with specific string
     * @param fileName name of the file to be modified
     * @param lines number of ending lines to be removed
     * @param content ending string
     * @throws IOException
     */
    public static void deleteLastLines(String fileName, int lines, String content) throws IOException {

        if (TextFile.read(fileName).endsWith(content)) {
            deleteLastLines(fileName, lines);
        }
    }
}
