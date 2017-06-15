package ud.binmonkey.prog3_proyecto_server.ftp;

import org.apache.commons.net.ftp.FTPClient;
import ud.binmonkey.prog3_proyecto_server.common.network.URI;

import java.io.IOException;

public class FTPlib {

    private static String host = URI.getHost("ftp");
    private static int port = URI.getPort("ftp");

    /**
     * ACCESS IS PRIVATE SO ALL FTP CALLS ARE DEFINED IN THIS CLASS AND CALLED FROM WHEREVER
     * Returns logged in client.
     *
     * @param userName username to log in with
     * @param password password
     * @return logged in @FTPClient if success, null if false
     * @throws IOException FTP or connection error
     */
    private static FTPClient logIn(String userName, String password) throws IOException {
        FTPClient client = new FTPClient();
        client.connect(host, port);
        client.login(userName, password);
        return client;
    }

    /**
     * Rename a file on the FTP server
     * @param username username of the user that will rename the file
     * @param password password of user
     * @param oldFile old name of file
     * @param newFile new name of file
     * @param sameDir if true the file will change name but not directory
     * @throws IOException FTP or connection error
     */
    @SuppressWarnings("SameParameterValue") /* sameDir is expected to eventually be false */
    public static void rename(String username, String password, String oldFile, String newFile, boolean sameDir)
            throws IOException {

        FTPClient client = logIn(username, password);

        String newDir = "";
        if (sameDir) {
            String[] oldDir = oldFile.split("/");
            oldDir[oldDir.length - 1] = null;
            for (String dir: oldDir) {
                if (dir != null) {
                    newDir += dir + "/";
                }
            }
        }
        newFile = newDir + newFile;

        client.rename(oldFile, newFile);
    }

    /**
     * Delete a file or directory  in the FTP server
     * @param username username of the user that will delete the file or directory
     * @param password password of user
     * @param file file to be deleted
     * @throws IOException FTP or connection error
     */
    public static void delete(String username, String password, String file) throws IOException {
        FTPClient client = logIn(username, password);
        client.deleteFile(file);
        client.removeDirectory(file);
    }

    /**
     * Create a new directory in the FTP server
     * @param username username of the user that will crate the directory
     * @param password password of user
     * @param dirname name of new directory
     * @throws IOException FTP or connection error
     */
    public static void mkdir(String username, String password, String dirname) throws IOException {
        FTPClient client = logIn(username, password);
        client.makeDirectory(dirname);
    }

}
