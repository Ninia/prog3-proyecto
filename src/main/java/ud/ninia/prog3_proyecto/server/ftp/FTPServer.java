package ud.ninia.prog3_proyecto.server.ftp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.w3c.dom.Element;
import ud.ninia.prog3_proyecto.utils.DocumentReader;

import java.io.File;

public class FTPServer {

    FtpServer server;

    public FTPServer() {

        /* create serverFactory */
        FtpServerFactory serverFactory = new FtpServerFactory();

        /* create and configure listenerFactory */
        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(8021);

        /* Define SSL configuration */
        SslConfigurationFactory sslConfigurationFactory = new SslConfigurationFactory();
        sslConfigurationFactory.setKeystoreFile(new File("keys/ftpsserver.jks"));

        /* load keyStore password */
        String keyStorePasswd = ((Element)
                                ( DocumentReader.getDoc("config/SSL.xml")
                                ).getElementsByTagName("ssl-server").item(0)
                                ).getElementsByTagName("keypasswd").item(0).getTextContent();

        sslConfigurationFactory.setKeystorePassword(keyStorePasswd);

        /* set SSL configuration for the listener */
        listenerFactory.setSslConfiguration(sslConfigurationFactory.createSslConfiguration());
        listenerFactory.setImplicitSsl(true);

        /* replace default listener */
        serverFactory.addListener("default", listenerFactory.createListener());
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(new File("users.properties")); /* TODO: users.properties*/
        serverFactory.setUserManager(userManagerFactory.createUserManager());

        /* create server */
        this.server = serverFactory.createServer();
    }

    public static void main(String[] args) {

        /* create server */
        FTPServer ftpServer = new FTPServer();
        try {
            /* start server */
            ftpServer.server.start();
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }


}
