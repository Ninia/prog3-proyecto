package ud.binmonkey.prog3_proyecto_server.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCommandException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.*;
import ud.binmonkey.prog3_proyecto_server.common.security.UserAuthentication;
import ud.binmonkey.prog3_proyecto_server.common.time.DateUtils;
import ud.binmonkey.prog3_proyecto_server.users.User;
import ud.binmonkey.prog3_proyecto_server.users.attributes.Language;
import ud.binmonkey.prog3_proyecto_server.users.attributes.Role;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class MongoDB {

    /* TODO: tests */
    private static final Logger LOG = Logger.getLogger(MongoDB.class.getName());
    private static String COLLECTION = "users";
    static {
        try {
            LOG.addHandler(new FileHandler(
                    "logs/" + MongoDB.class.getName() + "." +
                            DateUtils.currentFormattedDate() + ".log.xml", true));
        } catch (SecurityException | IOException e) {
            LOG.log(Level.SEVERE, "Unable to create log file.");
        }
    }

    /* should there be MongoClient, MongoDB and MongoCollection attrs? */

    /**
     * Change username of existing user
      * @param oldUserName current username
     * @param newUserName new username
     */
    public static void changeUserName(String oldUserName, String newUserName)
            throws UserNotFoundException, NewUserExistsException, AdminEditException, InvalidNameException {

        /* lowercase usernames */
        oldUserName = oldUserName.toLowerCase();
        newUserName = newUserName.toLowerCase();

        UserAuthentication.checkAdmin(oldUserName, newUserName);
        UserAuthentication.isValidName(newUserName);

        if(userExists(oldUserName)) {
            if (!userExists(newUserName)) {
                MongoDatabase db = getUsersDB();
                MongoCollection collection = db.getCollection(COLLECTION);
                collection.updateMany(
                        new BasicDBObject("username", oldUserName),
                        new BasicDBObject("$set",
                                new BasicDBObject("username", newUserName)
                        )
                );
                LOG.log(Level.INFO, "Renamed user `" + oldUserName +  "` to user `" + newUserName + "`");
            } else {
                throw new NewUserExistsException(newUserName);
            }
        } else {
            throw new UserNotFoundException(oldUserName);
        }

    }

    /**
     * @return User MongoDatabase object
     */
    private static MongoDatabase getUsersDB(){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase db = mongoClient.getDatabase(COLLECTION);
        try {
            db.createCollection(COLLECTION);
        } catch (MongoCommandException e) {
            /* Database is already created */
        }
        return db;
    }

    /**
     * Find user in database
     * @param userName username of user to be found
     * @return Document containing user
     */
    public static Document getUser(String userName) throws UserNotFoundException {

        /* lowercase usernames */
        userName = userName.toLowerCase();

        MongoDatabase db = getUsersDB();
        MongoCollection collection = db.getCollection(COLLECTION);
        FindIterable iterable = collection.find(new BasicDBObject("username", userName));
        for (Object o: iterable) {
            if (o instanceof Document) {
                if(((Document) o).get("username").equals(userName)) {
                    return (Document) o;
                }
            }
        }
        throw new UserNotFoundException(userName);
    }

    /**
     * Returns password of username
     * @param userName username whose password is to be found
     * @return password as char array
     * @throws UserNotFoundException user was not found
     * @throws AdminEditException tried to get admin password
     */
    public static char[] getPassword(String userName) throws UserNotFoundException, AdminEditException {
        userName = userName.toLowerCase();
        UserAuthentication.checkAdmin(userName);
        if (!userExists(userName)) {
            throw new UserNotFoundException("User " + userName + "not found.");
        }
        Document user = getUser(userName);
        return ((String) user.get("password")).toCharArray();
    }

    /**
     * Checks if username is already registered in database
     * @param userName username to be checked
     * @return true if it exists, false if not
     */
    public static boolean userExists(String userName) {

        /* lowercase usernames */
        userName = userName.toLowerCase();

        MongoDatabase db = getUsersDB();
        MongoCollection collection = db.getCollection(COLLECTION);
        BasicDBObject query = new BasicDBObject();
        query.put("username", userName);

        /* if user count != 0 */
        return collection.count(query) != 0;
    }

    /**
     * DON'T CALL DIRECTLY UNLESS IT'S FROM UserManager TO AVOID CONFLICT WITH FTP
     * Creates a Document in MongoDB with the Object passed as arg, if one with same username does not already exist
     * @param user user (as an object) to be created
     */
    @SuppressWarnings("unchecked")
    public static void createUser(User user) throws NewUserExistsException, AdminEditException, InvalidNameException {

        /* lowercase usernames */
        user.setUserName(user.getUserName().toLowerCase());
        UserAuthentication.checkAdmin(user.getUserName());
        UserAuthentication.isValidName(user.getUserName());

        if (!userExists(user.getUserName())) {
            MongoDatabase db = getUsersDB();
            MongoCollection collection = db.getCollection(COLLECTION);
            BasicDBObject doc = new BasicDBObject("username", user.getUserName())
                    .append("display_name", user.getDisplayName())
                    .append("birth_date", user.getBirthdDate())
                    .append("email", user.getEmail())
                    .append("gender", user.getGender())
                    .append("password", new String(user.getPassword()))
                    .append("preferred_language", user.getPreferredLanguage().toString())
                    .append("role", user.getRole().toString());
            collection.insertOne(Document.parse(doc.toJson()));
            StringBuilder logInfo = new StringBuilder();
            int i = 1;
            for(String key: doc.keySet()) {
                logInfo.append("\n").append(key).append(": '").append(doc.get(key)).append("'");
                if (i < doc.size()) {
                    logInfo.append(",");
                }
                i++;
            }
            LOG.log(Level.INFO, "New user created:" + logInfo);
        } else {
            throw new NewUserExistsException(user.getUserName());
        }
    }

    /**
     * Change birth date of user
     * @param userName username of user whose birth_date will be changed
     * @param birthDate new birth_date
     */
    public static void changeBirthdate(String userName, String birthDate) throws AdminEditException {

        /*todo: check validity */
        userName = userName.toLowerCase();
        UserAuthentication.checkAdmin(userName);
        if (userExists(userName)) {
            MongoDatabase db = getUsersDB();
            MongoCollection collection = db.getCollection(COLLECTION);
            collection.updateMany(
                    new BasicDBObject("username", userName),
                    new BasicDBObject("$set",
                            new BasicDBObject("birth_date", birthDate)
                    )
            );
        }
        LOG.log(Level.INFO, "Birthdate of user `" + userName +  "` has been changed to `" + birthDate + "''.");
    }

    /**
     * Change email of user
     * @param userName username of user whose email will be changed
     * @param email new email
     * @throws AdminEditException
     */
    public static void changeEmail(String userName, String email) throws AdminEditException {

        /*todo: check validity */
        userName = userName.toLowerCase();
        UserAuthentication.checkAdmin(userName);

        if (userExists(userName)) {
            MongoDatabase db = getUsersDB();
            MongoCollection collection = db.getCollection(COLLECTION);
            collection.updateMany(
                    new BasicDBObject("username", userName),
                    new BasicDBObject("$set",
                            new BasicDBObject("email", email)
                    )
            );
        }
        LOG.log(Level.INFO, "Email of user `" + userName +  "` has been changed to `" + email + "''.");
    }

    /**
     * Change display name of user
     * @param userName username of user whose display name will be changed
     * @param displayName new display name
     */
    public static void changeDisplayName(String userName, String displayName) throws AdminEditException {

        /*todo: check validity */
        userName = userName.toLowerCase();
        UserAuthentication.checkAdmin(userName);

        if (userExists(userName)) {
            MongoDatabase db = getUsersDB();
            MongoCollection collection = db.getCollection(COLLECTION);
            collection.updateMany(
                    new BasicDBObject("username", userName),
                    new BasicDBObject("$set",
                            new BasicDBObject("display_name", displayName)
                    )
            );
        }
        LOG.log(Level.INFO, "DisplayName of user `" + userName +  "` has been changed to `" + displayName + "''.");
    }


    public static void changeGender(String userName, String gender) throws AdminEditException {

        /*todo: check validity */
        userName = userName.toLowerCase();
        UserAuthentication.checkAdmin(userName);

        if (userExists(userName)) {
            MongoDatabase db = getUsersDB();
            MongoCollection collection = db.getCollection(COLLECTION);
            collection.updateMany(
                    new BasicDBObject("username", userName),
                    new BasicDBObject("$set",
                            new BasicDBObject("gender", gender)
                    )
            );
        }
        LOG.log(Level.INFO, "Gender of user `" + userName +  "` has been changed to `" + gender + "''.");
    }

    /**
     * change preferred language of user
     * @param userName username of user whose email will be changed
     * @param language new preferred language, MUST BE IN @Language ENUM
     */
    public static void changePreferredLanguage(String userName, String language)
            throws AdminEditException, UnsupportedLanguageException {

        /*todo: check validity */
        userName = userName.toLowerCase();
        UserAuthentication.checkAdmin(userName);

        boolean validLang = false;
        for (Language lang: Language.values()) {
            if (language.equals(lang.toString())) {
                validLang = true;
                break;
            }
        }
        if (!validLang) {
            throw new UnsupportedLanguageException(language);
        }
        if (userExists(userName)) {
            MongoDatabase db = getUsersDB();
            MongoCollection collection = db.getCollection(COLLECTION);
            collection.updateMany(
                    new BasicDBObject("username", userName),
                    new BasicDBObject("$set",
                            new BasicDBObject("preferred_language", language)
                    )
            );
        }
        LOG.log(Level.INFO, "Preferred language of user `" + userName +
                "` has been changed to `" + language + "''.");
    }

    /**
     * change role of user
     * @param userName username of user whose role will be changed
     * @param role new role. MUST BE IN @Role ENUM
     */
    public static void changeRole(String userName, String role) throws AdminEditException, InvalidRoleException {

        /*todo: check validity */
        userName = userName.toLowerCase();
        UserAuthentication.checkAdmin(userName);

        boolean validRole = false;
        for(Role r: Role.values()) {
            if (role.equals(r.toString())) {
                validRole = true;
                break;
            }
        }
        if (!validRole) {
            throw new InvalidRoleException(role);
        }
        if (userExists(userName)) {
            MongoDatabase db = getUsersDB();
            MongoCollection collection = db.getCollection(COLLECTION);
            collection.updateMany(
                    new BasicDBObject("username", userName),
                    new BasicDBObject("$set",
                            new BasicDBObject("role", role)
                    )
            );
        }
        LOG.log(Level.INFO, "Role of user `" + userName +  "` has been changed to `" + role + "''.");
    }

    /**
     * Change password of MongoDB user
     * @param userName username whose password will be changed
     * @param oldPassword current password
     * @param newPassword new password
     * @throws UserNotFoundException user was not found
     */
    public static void changePassword(String userName, String oldPassword, String newPassword)
            throws UserNotFoundException, IncorrectPasswordException, AdminEditException {

        userName = userName.toLowerCase();
        UserAuthentication.checkAdmin(userName);

        if(userExists(userName)) {
            if (getUser(userName).get("password").equals(oldPassword)) {
                MongoDatabase db = getUsersDB();
                MongoCollection collection = db.getCollection(COLLECTION);
                collection.updateMany(
                        new BasicDBObject("username", userName),
                        new BasicDBObject("$set",
                                new BasicDBObject("password", newPassword)
                        )
                );
                LOG.log(Level.INFO, "Changed password of user `" + userName +  "`.");
            } else {
                throw new IncorrectPasswordException(userName);
            }
        } else {
            throw new UserNotFoundException(userName);
        }
    }

    public static void changePassword(String userName, char[] oldPassword, char[] newPassword)
            throws UserNotFoundException, IncorrectPasswordException, AdminEditException {
        changePassword(userName, new String(oldPassword), new String(newPassword));
    }

    /**
     * Create user from document
     * @param user Document containing user
     */
    @SuppressWarnings("unchecked")
    public static void createUser(Document user) {
        MongoDatabase db = getUsersDB();
        MongoCollection collection = db.getCollection(COLLECTION);
        collection.insertOne(user);
    }

    /**
     * DON'T CALL DIRECTLY UNLESS IT'S FROM UserManager TO AVOID CONFLICT WITH FTP
     * Removes all users with username in args (which should only be one)
     * @param userName username of user to be deleted
     */
    public static void deleteUser(String userName) throws UserNotFoundException {

        /* lowercase username */
        userName = userName.toLowerCase();

        if (userExists(userName)) {
            MongoDatabase db = getUsersDB();
            MongoCollection collection = db.getCollection(COLLECTION);
            BasicDBObject query = new BasicDBObject();
            query.append("username", userName);
            collection.deleteOne(query);
            LOG.log(Level.INFO, "User `" + userName + "` deleted.");
        } else {
            throw new UserNotFoundException(userName);
        }
    }

    public static void main(String[] args) throws UserNotFoundException,
            NewUserExistsException, InvalidNameException, AdminEditException {
        try {
            deleteUser("ben10");
        } catch (UserNotFoundException e) {
            /* Intentionally empty */
        }
        try {
            deleteUser("10ben");
        } catch (UserNotFoundException e) {
            /* Intentionally empty */
        }

        MongoDB.createUser(new User(
                "10-10-2010", "Ben Ten", "ben10@ben.ten", "Male",
                "10ben".toCharArray(), Language.EN, Role.USER, "ben10"
        ));
        System.out.println(MongoDB.userExists("ben10"));
        changeUserName("ben10", "10ben");
        System.out.println(MongoDB.userExists("10ben"));
        getUser("10ben");
    }
}
