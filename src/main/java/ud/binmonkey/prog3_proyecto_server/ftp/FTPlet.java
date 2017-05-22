package ud.binmonkey.prog3_proyecto_server.ftp;

import org.apache.ftpserver.ftplet.*;
import ud.binmonkey.prog3_proyecto_server.common.time.DateUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ftplet that will handle all requests
 */
@SuppressWarnings("WeakerAccess")
public class FTPlet extends DefaultFtplet {

    private static final Logger LOG = Logger.getLogger(FTPlet.class.getName());
    private static String[] testAllowedCommands; /* only for testing */

    static {
        try {
            LOG.addHandler(new FileHandler(
                    "logs/" + FTPlet.class.getName() + "." +
                            DateUtils.currentFormattedDate() + ".log.xml", true));
        } catch (SecurityException | IOException e) {
            LOG.log(Level.SEVERE, "Unable to create log file.");
        }
    }

    private String[] allowedCommonCommands;

     /* used in test FTPlet generation from XML file */
    public static String[] getTestAllowedCommands() {
        return testAllowedCommands;
    }

    public static void setTestAllowedCommands(String[] testAllowedCommands) {
        FTPlet.testAllowedCommands = testAllowedCommands;
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

    /**
     * Runs before any command is executed, prevents execution without explicit permission in common folder.
     * @param session current session
     * @param request request to be processed
     * @return {link @org.apache.ftpserver.ftplet.FtpletResult}
     * @throws FtpException error in ftp execution
     * @throws IOException error managing connections or files
     */
    @Override
    public FtpletResult beforeCommand(FtpSession session, FtpRequest request) throws FtpException, IOException {
        switch (request.getCommand()) {

            case "AUTH":
                /* XXX: response code 431 is about ssl. Ignore by now. */
                LOG.log(Level.INFO, "AUTH request received from address: `" + session.getClientAddress() + "`");
                break;

            case "USER":
                LOG.log(Level.INFO, "USER request received for user: `" + request.getArgument() + "`");
                break;

            case "PASS":
                LOG.log(Level.INFO, "PASS request received." /* `" + request.getArgument() + "`" */);
                break;

            default:
                LOG.log(Level.INFO, "Request received. User: `" + session.getUser().getName() + "`, Command: `" +
                        request.getCommand() + "`.");

                /* avoid execution of forbidden commands */
                if (session.getUser().getHomeDirectory().matches(".*/common") &&
                        !Arrays.asList(allowedCommonCommands).contains(request.getCommand())) {

                    /* Log attempt*/
                    LOG.log(Level.WARNING, "Attempted command: `" + request.getCommand() + "`");

                    /* Write reply*/
                    String[] path = session.getUser().getHomeDirectory().split("/");
                    session.write(new DefaultFtpReply(FtpReply.REPLY_452_REQUESTED_ACTION_NOT_TAKEN,
                                "Only user: `admin` is allowed to execute command: `" + request.getCommand() +
                                        "` in directory: `" + path[path.length - 1] + "`."));
                    /* request will be skipped */
                    return FtpletResult.SKIP;
                }
                break;
        }
        return super.beforeCommand(session, request);
    }

    @Override
    public FtpletResult afterCommand(FtpSession session, FtpRequest request, FtpReply reply)
            throws FtpException, IOException {
        LOG.log(Level.INFO, "Reply sent. Code: `" + reply.getCode() + "`, Message: `" + reply.getMessage() + "`");
        return super.afterCommand(session, request, reply);
    }

    @SuppressWarnings("unused")  /* used in FTPlet generation from XML file */
    public String[] getAllowedCommonCommands() {
        return this.allowedCommonCommands;
    }

    /**
     * Sets allowed commands for user `common`
     * @param allowedCommonCommands single string with commands separated by semicolon `;`
     */
    @SuppressWarnings("unused")  /* used in FTPlet generation from XML file */
    public void setAllowedCommonCommands(String[] allowedCommonCommands) {
        this.allowedCommonCommands = allowedCommonCommands[0].split(";");

        /* TODO: the following try-catch is only for testing */
        try {
            FTPlet.setTestAllowedCommands(allowedCommonCommands[0].split(";"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public String getAllowedCommonCommandsAsString() {
        StringBuilder commands = new StringBuilder();
        int count = 0;
        for (String cmd: this.allowedCommonCommands) {
            commands.append(cmd);
            if (count < this.allowedCommonCommands.length) {
                commands.append(";");
            }
        }
        return commands.toString();
    }
}
