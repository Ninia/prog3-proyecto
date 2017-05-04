package ud.binmonkey.prog3_proyecto_server.common;

import org.apache.ftpserver.ftplet.FtpException;
import ud.binmonkey.prog3_proyecto_server.ftp.FTPServer;
import ud.binmonkey.prog3_proyecto_server.mongodb.MongoDB;

public class UserManager {

    /* TODO: update users */

    public static void createUser(User user, String ftpUserFileLocation) throws FtpException {
        MongoDB.createUser(user);
        FTPServer.createUser(user.getUserName(), user.getPassword().toString(), ftpUserFileLocation);
    }

    public static void deleteUser(String userName, String ftpUserFileLocation) throws FtpException {
        MongoDB.deleteUser(userName);
        FTPServer.deleteUser(userName, ftpUserFileLocation);

    }

    public static void main(String[] args) {
//        FTPServer.createUser("hi", "there", "conf/properties/ftpusers.properties");
    }
}
