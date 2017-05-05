package ud.binmonkey.prog3_proyecto_server.common;

import org.apache.ftpserver.ftplet.FtpException;
import org.bson.Document;
import org.json.JSONObject;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.AdminEditException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.InvalidNameException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.NewUserExistsException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UserNotFoundException;
import ud.binmonkey.prog3_proyecto_server.ftp.FTPServer;
import ud.binmonkey.prog3_proyecto_server.mongodb.MongoDB;

import java.io.IOException;
import java.util.Arrays;
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
     */
    @SuppressWarnings("unused")
    public static void changeUserName(String oldUserName, String newUserName, String ftpUserFileLocation)
            throws FtpException, IOException, AdminEditException, InvalidNameException {
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
     */
    private static void createUser(User user, String ftpUserFileLocation)
            throws FtpException, InvalidNameException, AdminEditException {
        try {
            MongoDB.createUser(user);
            try {
                FTPServer.createUser(user.getUserName(), Arrays.toString(user.getPassword()), ftpUserFileLocation);
                return;
            } catch (NewUserExistsException e) {
                LOG.log(Level.SEVERE, "FTP user `" + user.getUserName() + "` already exists.");
            }
            /* revert changes */
            MongoDB.deleteUser(user.getUserName());
        } catch (UserNotFoundException e) {
            LOG.log(Level.SEVERE, "MongoDB user `" + user.getUserName() + "` not found.");
        } catch (NewUserExistsException e) {
            LOG.log(Level.SEVERE, "MongoDB user `" + user.getUserName() + "` already exists.");
        }
    }

    /**
     * Create user on both FTP and MongoDB
     * @param userJson user to be created
     * @param ftpUserFileLocation location of user properties file
     */
    private static void createUser(JSONObject userJson, String ftpUserFileLocation) throws FtpException, InvalidNameException, AdminEditException {
        User user = new User(
                    (String) userJson.get("birthdate"),
                    (String) userJson.get("display_name"),
                    (String) userJson.get("email"),
                    (String) userJson.get("gender"),
                    ((String) userJson.get("password")).toCharArray(),
                    null,
                    null,
                    (String) userJson.get("username"));

            /* TODO: there must be a better way of doing this */
            switch ((String) userJson.get("role")) {
                case "admin":
                    user.setRole(Role.ADMIN);
                    break;
                case "mod":
                    user.setRole(Role.MOD);
                    break;
                default:
                    user.setRole(Role.USER);
                    break;
            }

            switch ((String) userJson.get("preferred_language")) {
                case "es":
                    user.setPreferredLanguage(Language.ES);
                    break;
                case "eus":
                    user.setPreferredLanguage(Language.EUS);
                    break;
                default:
                    user.setPreferredLanguage(Language.EN);
                    break;
            }
            createUser(user, ftpUserFileLocation);
    }

    /**
     * Deletes user from both FTP and MongoDB
     * @param userName username to be deleted
     * @param ftpUserFileLocation location of user properties file
     */
    @SuppressWarnings("unused")
    public static void deleteUser(String userName, String ftpUserFileLocation) throws FtpException, AdminEditException {
        try {
            /* document to restore user if FTP deletion fails */
            Document backUpUser = MongoDB.getUser(userName);
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

    public static void main(String[] args) throws UserNotFoundException, FtpException, IOException,
            InvalidNameException, AdminEditException {

        String USERFILE = "conf/properties/ftpusers.properties";
        String userFile = "src/main/resources/mongodb/examples/users.json";
        JSONObject users = new JSONObject(TextFile.read(userFile));
        for (Object o: users.getJSONArray("users")) {
            if (o instanceof JSONObject) {
                createUser((JSONObject) o, USERFILE);
            }
        }
    }
}
