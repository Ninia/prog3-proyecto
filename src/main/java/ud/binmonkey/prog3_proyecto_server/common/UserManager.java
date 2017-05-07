package ud.binmonkey.prog3_proyecto_server.common;

import org.apache.ftpserver.ftplet.FtpException;
import org.bson.Document;
import org.json.JSONObject;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.*;
import ud.binmonkey.prog3_proyecto_server.ftp.FTPServer;
import ud.binmonkey.prog3_proyecto_server.mongodb.MongoDB;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
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
                FTPServer.createUser(user.getUserName(), String.valueOf(user.getPassword()), ftpUserFileLocation);
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
     * Change password of both FTP and MongoDB
     * @param userName username of user whose password will be changed
     * @param oldPassword current password
     * @param newPassword new password
     * @param ftpUserFileLocation location of FTP user properties file
     */
    public static void changePassword(String userName, String oldPassword, String newPassword,
                                      String ftpUserFileLocation)
            throws UserNotFoundException, IncorrectPasswordException, AdminEditException {
        MongoDB.changePassword(userName, oldPassword, newPassword);
        try {
            FTPServer.changePassword(userName, oldPassword, newPassword, ftpUserFileLocation);
        } catch (FtpException | AdminEditException | IncorrectPasswordException | UserNotFoundException e) {
            /* revert chagnes */
            MongoDB.changePassword(userName, newPassword, oldPassword);
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
    @SuppressWarnings("WeakerAccess")
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
        String userList = "src/main/resources/mongodb/examples/users.json";
        JSONObject users = new JSONObject(TextFile.read(userList));
        for (Object o: users.getJSONArray("users")) {
            if (o instanceof JSONObject) {
                /* remove user from both ftp and mongodb */
                try {
                    MongoDB.deleteUser((String) ((JSONObject) o).get("username"));
                } catch (UserNotFoundException e) {
                    /* as expected */
                }
                try {
                    FTPServer.deleteUser((String) ((JSONObject) o).get("username"), USERFILE);
                } catch (UserNotFoundException e) {
                    /* as expected */
                }
                createUser((JSONObject) o, USERFILE);
            }
        }
    }

    /**
     * Change birth date of user
     * @param userName username of user whose birthdate will be changed
     * @param birthDate new birthdate
     */
    public void changeBirthDate(String userName, String birthDate) throws AdminEditException {
        MongoDB.changeBirthdate(userName, birthDate);
    }

    /**
     * Change display name of user
     * @param userName username of user whose display name will be changed
     * @param displayName new display name
     */
    public void changeDisplayName(String userName, String displayName) throws AdminEditException {
        MongoDB.changeDisplayName(userName, displayName);
    }

    /**
     * Change email of user
     * @param userName username of user whose email will be changed
     * @param email new email
     */
    public void changeEmail(String userName, String email) throws AdminEditException {
        MongoDB.changeEmail(userName, email);
    }

    /**
     * change preferred language of user
     * @param userName username of user whose email will be changed
     * @param language new preferred language, MUST BE IN @Language ENUM
     * @param language new preferred language, MUST BE IN @Language ENUM
     */
    public void changePreferredLanguage(String userName, String language)
            throws AdminEditException, UnsupportedLanguageException {
        MongoDB.changePreferredLanguage(userName, language);
    }

    /**
     * change role of user
     * @param userName username of user whose role will be changed
     * @param role new role. MUST BE IN @Role ENUM
     */
    public void changeRole(String userName, String role) throws AdminEditException, InvalidRoleException {
        MongoDB.changeRole(userName, role);
    }
}
