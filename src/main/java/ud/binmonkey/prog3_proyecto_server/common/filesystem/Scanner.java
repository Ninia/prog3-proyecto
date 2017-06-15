package ud.binmonkey.prog3_proyecto_server.common.filesystem;

import org.json.JSONArray;
import org.json.JSONObject;
import ud.binmonkey.prog3_proyecto_server.common.DocumentReader;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.DirIsFileException;

import java.io.File;
import java.io.FileNotFoundException;

public class Scanner {

    private static final String ftpd = DocumentReader.getAttr(DocumentReader.getDoc("conf/properties.xml"),
            "network", "ftp-server", "ftpd").getTextContent();


    public static JSONObject scanDir(String path) throws FileNotFoundException, DirIsFileException {

        if (!path.endsWith("/")) {
            path += "/";
        }

        File file = new File(path);
        if (!file.exists()) {
            System.out.println("404");
            System.out.println(file.list());
            throw new FileNotFoundException();
        }

        if (file.isFile()) {
            throw new DirIsFileException(path);
        }

        JSONObject dir = new JSONObject();
        JSONObject directories = new JSONObject();
        JSONArray files = new JSONArray();


        for (String element: file.list()) {

            File currentFile = new File(path, element);

            if (currentFile.isDirectory()) {
                directories.put(element, scanDir(path + element));
            } else if (currentFile.isFile()) {
                files.put(element);
            }
        }

        dir.put("files", files);
        dir.put("directories", directories);

        return dir;
    }

    public static String getFtpd() {
        return ftpd;
    }

    public static void main(String[] args) {
        try {
            JSONObject dir = scanDir(ftpd);
            System.out.println(dir);
        } catch (FileNotFoundException | DirIsFileException e) {
            e.printStackTrace();
        }
    }

}
