package ud.binmonkey.prog3_proyecto_server.ftp;


import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.FtpException;

public class FTPServer {

    private FtpServer server;

    public FTPServer() {

//        FtpServerFactory serverFactory = new FtpServerFactory();
//        ListenerFactory factory = new ListenerFactory();
//        /* set the port of the listener */
//        factory.setPort(8021);
//        /* replace the default listener */
//        serverFactory.addListener("default", factory.createListener());
//
//        /* user management */
//        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
//        userManagerFactory.setFile(new File("conf/properties/ftpusers.properties")); /* TODO: users.properties*/
//        userManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
//        serverFactory.setUserManager(userManagerFactory.createUserManager());
//
//        this.server = serverFactory.createServer();
    }

    public static void main(String[] args) throws FtpException {
        (new FTPServer()).server.start();
    }
}
