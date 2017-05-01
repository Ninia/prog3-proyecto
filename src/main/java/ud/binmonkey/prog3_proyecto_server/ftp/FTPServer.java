package ud.binmonkey.prog3_proyecto_server.ftp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.apache.ftpserver.impl.FtpServerContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import ud.binmonkey.prog3_proyecto_server.common.DateUtils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FTPServer extends DefaultFtpServer{

    private static final Logger logger = Logger.getLogger(FtpServer.class.getName());

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
