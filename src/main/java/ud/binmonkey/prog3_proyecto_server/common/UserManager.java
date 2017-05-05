package ud.binmonkey.prog3_proyecto_server.common;

import org.apache.ftpserver.ftplet.FtpException;
import org.bson.Document;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.NewUserExistsException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UserNotFoundException;
import ud.binmonkey.prog3_proyecto_server.ftp.FTPServer;
import ud.binmonkey.prog3_proyecto_server.mongodb.MongoDB;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserManager {

    /* todo tests */
    private static final Logger LOG = Logger.getLogger(UserManager.class.getName());
    static {
        try {
            LOG.addHandler(new FileHandler(
                    "logs/" + UserManager.class.getName() + "." +
                            DateUtils.currentFormattedDate() + ".log.xml", true));
        } catch (SecurityException | IOException e) {
            LOG.log(Level.SEVERE, "Unable to create log file.");
        }
    }

    /**
     * Changes username in both FTP and MongoDB
     * @param oldUserName current username
     * @param newUserName old username
     * @param ftpUserFileLocation location of users properties file
     * @throws FtpException
     * @throws IOException
     */
    public static void changeUserName(String oldUserName, String newUserName, String ftpUserFileLocation) throws FtpException, IOException {
        try {
            MongoDB.changeUserName(oldUserName, newUserName);
            try {
                FTPServer.renameUser(oldUserName, newUserName, ftpUserFileLocation);
                return;
            } catch (UserNotFoundException e) {
                LOG.log(Level.SEVERE, "FTP user `" + oldUserName + "` not found.");
            } catch (NewUserExistsException e) {
                LOG.log(Level.SEVERE, "FTP user `" + newUserName + "` already exists.");
            }
            /* Revert changes */
            MongoDB.changeUserName(newUserName, oldUserName);
        } catch (UserNotFoundException e) {
            LOG.log(Level.SEVERE, "MongoDB user `" + oldUserName + "` not found.");
        } catch (NewUserExistsException e) {
            LOG.log(Level.SEVERE, "MongoDB user `" + newUserName + "` already exists.");
        }

    }

    /**
     * Create user on both FTP and MongoDB
     * @param user user to be created
     * @param ftpUserFileLocation location of user properties file
     * @throws FtpException
     */
    public static void createUser(User user, String ftpUserFileLocation) throws FtpException {
        try {
            MongoDB.createUser(user);
            try {
                FTPServer.createUser(user.getUserName(), user.getPassword().toString(), ftpUserFileLocation);
                return;
            } catch (NewUserExistsException e) {
                LOG.log(Level.SEVERE, "FTP user `" + user.getUserName() + "` already exists.");
            }
            /* revert changes */
            MongoDB.deleteUser(user.getUserName());
        } catch (UserNotFoundException e) {
            LOG.log(Level.SEVERE, "MongoDB user `" + user.getUserName() + "` already exists.");
        } catch (NewUserExistsException e) {
            LOG.log(Level.SEVERE, "MongoDB user `" + user.getUserName() + "` already exists.");
        }
    }

    /**
     * Deletes user from both FTP and MongoDB
     * @param userName username to be deleted
     * @param ftpUserFileLocation location of user properties file
     * @throws FtpException
     */
    public static void deleteUser(String userName, String ftpUserFileLocation) throws FtpException {
        try {
            /* document to restore user if FTP deletion fails */
            Document backUpUser = null;
            try {
                backUpUser = MongoDB.getUser(userName);
            } catch (UserNotFoundException e) {
                throw e;
            }
            MongoDB.deleteUser(userName);
            try {
                FTPServer.deleteUser(userName, ftpUserFileLocation);
                return;
            } catch (UserNotFoundException e) {
                LOG.log(Level.SEVERE, "FTP user `" + userName + "` not found.");
            }
            /* restore user */
            MongoDB.createUser(backUpUser);
        } catch (UserNotFoundException e) {
            LOG.log(Level.SEVERE, "MongoDB user `" + userName + "` not found.");
        }
    }

    public static void main(String[] args) {
//        FTPServer.createUser("hi", "there", "conf/properties/ftpusers.properties");
    }
}
