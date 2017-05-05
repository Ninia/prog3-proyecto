package ud.binmonkey.prog3_proyecto_server.ftp;

import org.apache.commons.io.FileUtils;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.apache.ftpserver.impl.FtpServerContext;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import ud.binmonkey.prog3_proyecto_server.common.DateUtils;
import ud.binmonkey.prog3_proyecto_server.common.Security;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FTPServer extends DefaultFtpServer{

    private static final Logger LOG = Logger.getLogger(FtpServer.class.getName());
    private static String ftpdLocation = "src/test/resources/ftp/ftpd";

    static {
        try {
            LOG.addHandler(new FileHandler(
                    "logs/" + FTPServer.class.getName() + "." +
                            DateUtils.currentFormattedDate() + ".log.xml", true));
        } catch (SecurityException | IOException e) {
            LOG.log(Level.SEVERE, "Unable to create log file.");
        }
    }

    public FTPServer(FtpServerContext serverContext) {
        super(serverContext);
    }

    /**
     * XXX: ONLY USE THIS METHOD FOR USER MANAGEMENT
     * @param userName username of new user
     * @param password password of new user
     * @param userFileLocation location to user file
     */
    public static void createUser(String userName, String password, String userFileLocation) throws FtpException,
            NewUserExistsException, AdminEditException, InvalidNameException {

        /* lowercase usernames */
        userName = userName.toLowerCase();
        Security.checkAdmin(userName);
        Security.isValidName(userName);

        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(new File(userFileLocation));

        UserManager userManager = userManagerFactory.createUserManager();

        if (userManager.getUserByName(userName) == null) {
            /* create basic user */
            BaseUser user = new BaseUser();
            user.setName(userName);
            user.setHomeDirectory(ftpdLocation + "/" + userName);
            user.setPassword(password);

            /* save user */
            userManager.save(user);
            LOG.log(Level.INFO, "New user `" + userName + "` created.");
        } else {
            throw new NewUserExistsException(userName);
        }
    }

    /**
     * Creates new dir, copies all files from old dir to new dir and deletes old dir
     * @param oldDir current directory
     * @param newDir new directory
     */
    private static void migrateAllFiles(String oldDir, String newDir) throws IOException {
        FileUtils.copyDirectory(new File(oldDir), new File(newDir));
        LOG.log(Level.INFO, "Copied directory + `" + oldDir + "` to new directory `" + newDir + "`");
        FileUtils.deleteDirectory(new File(oldDir));
        LOG.log(Level.INFO, "Deleted directory `" + oldDir + "`");
    }

    /**
     * Renames username and copies all it's files to dir of new user
     * @param oldUserName current username
     * @param newUserName new username
     * @param userFileLocation location to user properties file
     */
    public static void renameUser(String oldUserName, String newUserName, String userFileLocation)
            throws FtpException, IOException, NewUserExistsException, UserNotFoundException,
            AdminEditException, InvalidNameException {

        /* lowercase usernames */
        oldUserName = oldUserName.toLowerCase();
        newUserName = newUserName.toLowerCase();

        Security.checkAdmin(oldUserName, newUserName);
        /* validity of name will be checked at @createUser */

        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(new File(userFileLocation));

        UserManager userManager = userManagerFactory.createUserManager();

        if (userManager.getUserByName(oldUserName) != null) {
            if (userManager.getUserByName(newUserName) == null) {
            /* create basic user */
                User user = userManager.getUserByName(oldUserName);
                BaseUser newUser = new BaseUser();
                newUser.setName(newUserName);
                newUser.setHomeDirectory(ftpdLocation + "/" + newUserName);
                newUser.setPassword(user.getPassword());

            /* save user */
                userManager.save(newUser);
                userManager.delete(oldUserName);
                LOG.log(Level.INFO, "User `" + oldUserName + "` migrated to user `" + newUserName + "`.");
                migrateAllFiles(user.getHomeDirectory(), newUser.getHomeDirectory());
            } else {
                throw new NewUserExistsException(newUserName);
            }
        } else {
            throw new UserNotFoundException(oldUserName);
        }
    }

    /**
     * Change password of FTP user. TODO: there ust be a better way of doing this that does not involve total rebuild
     * @param userName username of user changing it's password
     * @param oldPassword current password
     * @param newPassword new password
     * @param userFileLocation location of user properties file
     */
    @SuppressWarnings({"unused", "unchecked"})
    public static void changePassword(String userName, String oldPassword, String newPassword, String userFileLocation)
            throws AdminEditException, FtpException, UserNotFoundException, IncorrectPasswordException {

        /* lowercase username */
        userName = userName.toLowerCase();

        Security.checkAdmin(userName);

        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(new File(userFileLocation));

        UserManager userManager = userManagerFactory.createUserManager();

        if (userManager.getUserByName(userName) != null) {
            User user = userManager.getUserByName(userName);
            if (user.getPassword().equals(oldPassword)) {
                throw new IncorrectPasswordException(userName);
            }
            BaseUser newUser = new BaseUser();
            newUser.setAuthorities((List<Authority>) user.getAuthorities());
            newUser.setName(user.getName());
            newUser.setPassword(newPassword);
            newUser.setHomeDirectory(user.getHomeDirectory());
            deleteUser(userName, userFileLocation);
            userManager.save(newUser);
            LOG.log(Level.INFO, "Changed password of user `" + userName +  "`.");
        } else {
            throw new UserNotFoundException(userName);
        }
    }

    public static void deleteUser(String userName, String userFileLocation) throws FtpException, UserNotFoundException, AdminEditException {

        /* lowercase usernames */
        userName = userName.toLowerCase();
        Security.checkAdmin(userName);

        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(new File(userFileLocation));

        UserManager userManager = userManagerFactory.createUserManager();

        if (userManager.getUserByName(userName) != null) {
            userManager.delete(userName);
        } else {
            throw new UserNotFoundException(userName);
        }
    }


    @SuppressWarnings("WeakerAccess")  /* for probable later use from outside class*/
    public static FtpServer getFtpServer(String configLocation, String beanName) {
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(configLocation);
        context.setAllowBeanDefinitionOverriding(true);
        context.setBeanName("FTPServer");

        return context.getBean(beanName, FtpServer.class);
    }

    public static void main(String[] args) throws FtpException, InvalidNameException, AdminEditException {

        FtpServer ftpServer = FTPServer.getFtpServer("conf/FTPServer.xml", FTPlet.class.getSimpleName());
        ftpServer.start();
    }
}
