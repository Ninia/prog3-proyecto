package ud.binmonkey.prog3_proyecto_server.ftp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.SaltedPasswordEncryptor;
import org.w3c.dom.Element;
import ud.binmonkey.prog3_proyecto_server.common.DocumentReader;

import java.io.File;

/*
 * TODO: Manage listeners and users using files
 */
public class FTPSServer {

    FtpServer server;

    public FTPSServer() {

        /* create serverFactory */
        FtpServerFactory serverFactory = new FtpServerFactory();

        /* create and configure listenerFactory */
        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(8021);

        /* Define SSL configuration */
        SslConfigurationFactory sslConfigurationFactory = new SslConfigurationFactory();
        sslConfigurationFactory.setKeystoreFile(new File("src/test/resources/keys/ftpserver.jks"));

        /* load keyStore password */
        String keyStorePasswd = ((Element)
                (DocumentReader.getDoc("conf/SSL.xml")
                ).getElementsByTagName("ssl").item(0)
        ).getElementsByTagName("keypasswd").item(0).getTextContent();

        sslConfigurationFactory.setKeystorePassword(keyStorePasswd);

        /* set SSL configuration for the listener */
        listenerFactory.setSslConfiguration(sslConfigurationFactory.createSslConfiguration());
        listenerFactory.setImplicitSsl(true);

        /* replace default listener */
        serverFactory.addListener("default", listenerFactory.createListener());

        /* user management */
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(new File("conf/properties/ftpusers.properties")); /* TODO: users.properties */
        userManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
//        UserManager um = userManagerFactory.createUserManager();

        /* creation of user */
//        UserFactory userFact = new UserFactory();
//        userFact.setName("testuser");
//        userFact.setPassword("password");
//        userFact.setHomeDirectory("resources/ftpd");
//        User user = userFact.createUser();
//        try {
//            um.save(user);
//        } catch (FtpException e) {
//            e.printStackTrace();
//        }

        /* alt creation of base user*/
//        BaseUser baseUser = new BaseUser();
//        baseUser.setName("testuser");
//        baseUser.setPassword("testpasswd");
//        baseUser.setHomeDirectory("/home/ewvem/.p3p/ftpd/testuser");
        serverFactory.setUserManager(userManagerFactory.createUserManager());

        /* create server */
        this.server = serverFactory.createServer();
    }

    public static void main(String[] args) {

        /* create server */
        FTPSServer ftpServer = new FTPSServer();
        try {
            /* start server */
            ftpServer.server.start();
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }
}
