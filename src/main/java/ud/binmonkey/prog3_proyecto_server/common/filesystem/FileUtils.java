package ud.binmonkey.prog3_proyecto_server.common.filesystem;

import ud.binmonkey.prog3_proyecto_server.common.DocumentReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileUtils {

    /**
     * Publish a file from a user directory to the common directory
     * @param filePath path to file
     * @param userName username, used to locate file in complement with @filePath
     */
    public static void publishFile(String filePath, String userName, String newName, String type) throws IOException {

        final String ftpd = DocumentReader.getAttr(DocumentReader.getDoc("conf/properties.xml"),
                "network", "ftp-server", "ftpd").getTextContent();

        if (filePath.startsWith("/")) {
            return; /* root not allowed */
        }

        switch (type.toLowerCase()) {
            case "movie":
                newName = "movies/" + newName;
                break;

            case "episode":
                /* TODO */
                break;

            case "series":
                /* TODO */
                break;

            default: /* same as movies */
                newName = "movies/" + newName;
                break;
        }

        mkPath(ftpd + "/common/movies");
        mkPath(ftpd + "/common/series");

        Files.copy(
                new File(ftpd + "/" + userName + "/" + filePath).toPath(),
                new File(ftpd + "/common/" + newName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);

//        /* @DEPRECATED */
//        String[] components = filePath.split("/");
//
//        /* Obtain file */
//        String path = "";
//        for (int i = 0; i < components.length - 1; i++) {
//            path += components[i];
//            if (i != components.length - 2) {
//                path += "/";
//            }
//        }
//
//        mkPath(path);
//        Files.copy(
//                new File(ftpd + "/" + userName + "/" + filePath).toPath(),
//                new File(ftpd + "/common/" + filePath).toPath(),
//                StandardCopyOption.REPLACE_EXISTING); /* should replacing be allowed? */

    }

    /**
     * Create a full path to a directory, and the directory
     * @param path complete path to be created
     */
    @SuppressWarnings("WeakerAccess")
    public static void mkPath(String path) {
        mkPath(null, path);
    }

    /**
     * Recursively create a full path of directories
     * @param existing already existing directories
     * @param newPath path to be created
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void mkPath(String existing, String newPath) {

        if (existing == null) {
            existing = "";
        }

        if (newPath.startsWith("/") || existing.startsWith("/")) {
            return; /* root not allowed */
        }

        if (!existing.replaceAll(" ", "").equals("") && !existing.endsWith("/")) {
            existing += "/";
        }
        String[] components = newPath.split("/");

        new File(existing  + components[0]).mkdirs();

        if (components.length > 1) {
            newPath = "";
            for (int i = 1; i < components.length; i++) {
                newPath += components[i];
                if (i != components.length -1) {
                    newPath += "/";
                }
            }
            mkPath(existing + components[0], newPath);
        }
    }

    public static void main(String[] args) {
        mkPath("This/Is/A/Test");
    }
}
