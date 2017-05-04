package ud.binmonkey.prog3_proyecto_server.ftp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.apache.ftpserver.impl.FtpServerContext;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import ud.binmonkey.prog3_proyecto_server.common.DateUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FTPServer extends DefaultFtpServer{

    private static final Logger logger = Logger.getLogger(FtpServer.class.getName());
    private static String ftpdLocation = "src/test/resources/ftp/ftpd";

    static {
        try {
            logger.addHandler(new FileHandler(
                    "logs/" + FTPServer.class.getName() + "." +
                            DateUtils.currentFormattedDate() + ".log.xml", true));
        } catch (SecurityException | IOException e) {
            logger.log(Level.SEVERE, "Unable to create log file.");
        }
    }

    public FTPServer(FtpServerContext serverContext) {
        super(serverContext);
    }

    /**
     * XXX: ONLY USE THIS METHOD FOR USER MANEGEMENT
     * @param userName username of new user
     * @param password password of new user
     * @param userFileLocation location to user file
     * @return true if user was created false if username was taken
     */
    public static boolean createUser(String userName, String password, String userFileLocation) throws FtpException {

        FtpServerFactory serverFactory = new FtpServerFactory();
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(new File(userFileLocation));

        UserManager userManager = userManagerFactory.createUserManager();

        if (userManager.getUserByName(userName) != null) {
            /* create basic user */
            BaseUser user = new BaseUser();
            user.setName(userName);
            user.setHomeDirectory(ftpdLocation + "/" + userName);
            user.setPassword(password);
            List<Authority> authorities = new ArrayList<Authority>();
            authorities.add(new WritePermission());

            /* save user */
            try {
                userManager.save(user);
            } catch (FtpException e) {
                throw e;
            }
        } else {
            return false;
        }
        return true;
    }

    public static void deleteUser(String userName, String userFileLocation) throws FtpException {
        FtpServerFactory serverFactory = new FtpServerFactory();
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(new File(userFileLocation));

        UserManager userManager = userManagerFactory.createUserManager();

        if (userManager.getUserByName(userName) != null) {
            userManager.delete(userName);
        }
    }


    @SuppressWarnings("WeakerAccess")  /* for probable later use from outside class*/
    public static FtpServer getFtpServer(String configLocation, String beanName) {
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(configLocation);
        context.setAllowBeanDefinitionOverriding(true);
        context.setBeanName("FTPServer");

        return context.getBean(beanName, FtpServer.class);
    }

    public static void main(String[] args) throws FtpException {
        FtpServer ftpServer = FTPServer.getFtpServer("conf/FTPServer.xml", FTPlet.class.getSimpleName());
        ftpServer.start();
    }
}
