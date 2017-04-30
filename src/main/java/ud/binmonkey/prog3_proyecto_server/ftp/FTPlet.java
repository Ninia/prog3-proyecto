package ud.binmonkey.prog3_proyecto_server.ftp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpletContext;
import ud.binmonkey.prog3_proyecto_server.common.DateUtils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FTPlet extends DefaultFtplet {
    private static String[] forbiddenCommonCommands;
    private static final Logger LOG = Logger.getLogger(FtpServer.class.getName());
    static {
        try {
            LOG.addHandler(new FileHandler(
                    "logs/" + FtpServer.class.getName() +"." +
                            DateUtils.currentFormattedDate() + ".log.xml", true));
        } catch (SecurityException | IOException e) {
            LOG.log(Level.SEVERE, "Unable to create log file.");
        }
    }

    @Override
    public void init(FtpletContext ftpletContext) throws FtpException {
        LOG.log(Level.INFO, "FTPlet initialized.");
        super.init(ftpletContext);
    }

    @Override
    public void destroy() {
        LOG.log(Level.INFO, "FTPlet destroyed");
        super.destroy();
    }

    public static String[] getForbiddenCommonCommands() {
        return FTPlet.forbiddenCommonCommands;
    }

    public static void setForbiddenCommonCommands(String[] forbiddenCommonCommands) {
        FTPlet.forbiddenCommonCommands = forbiddenCommonCommands;
    }

    public static void setForbiddenCommonCommands(String forbiddenCommonCommands) {
        FTPlet.forbiddenCommonCommands = forbiddenCommonCommands.split(";");
    }
}
