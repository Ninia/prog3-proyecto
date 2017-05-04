package ud.binmonkey.prog3_proyecto_server.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ud.binmonkey.prog3_proyecto_server.common.Language;
import ud.binmonkey.prog3_proyecto_server.common.Role;
import ud.binmonkey.prog3_proyecto_server.common.User;

public class MongoDB {

    /* should there be MongoClient, MongoDB and MongoCollection attrs? */

    /**
     * @return User MongoDatabase object
     */
    public static MongoDatabase getUsersDB(){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase db = mongoClient.getDatabase("users");
        try {
            db.createCollection("users");
        } catch (MongoCommandException e) {}
        return db;
    }

    /**
     * Checks if username is already registered in database
     * @param userName username to be checked
     * @return true if it exists, false if not
     */
    public static boolean userExists(String userName) {

        MongoDatabase db = getUsersDB();
        MongoCollection collection = db.getCollection("users");
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
    public static void createUser(User user) {
        if (!userExists(user.getUserName())) {
            MongoDatabase db = getUsersDB();
            MongoCollection collection = db.getCollection("users");
            BasicDBObject doc = new BasicDBObject("username", user.getUserName())
                    .append("display_name", user.getDisplayName())
                    .append("email", user.getEmail())
                    .append("gender", user.getGender())
                    .append("password", user.getPassword())
                    .append("preferred_language", user.getPreferredLanguage().toString())
                    .append("role", user.getRole().toString());
            collection.insertOne(Document.parse(doc.toJson()));
        }
    }

    /**
     * DON'T CALL DIRECTLY UNLESS IT'S FROM UserManager TO AVOID CONFLICT WITH FTP
     * Removes all users with username in args (which should only be one)
     * @param userName
     */
    public static void deleteUser(String userName) {
        MongoDatabase db = getUsersDB();
        MongoCollection collection = db.getCollection("users");
        BasicDBObject query = new BasicDBObject();
        query.append("username", userName);
        collection.deleteOne(query);
    }

    public static void main(String[] args) {

        MongoDB.createUser(new User(
                "10-10-2010", "Ben Ten", "ben10@ben.ten", "Male",
                "10ben".toCharArray(), Language.EN, Role.USER, "ben10"
        ));
        System.out.println(MongoDB.userExists("ben10"));
    }

}
