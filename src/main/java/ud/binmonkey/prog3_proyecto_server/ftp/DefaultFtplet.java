package ud.binmonkey.prog3_proyecto_server.ftp;

import org.apache.ftpserver.ftplet.*;

import java.io.IOException;

public class DefaultFtplet implements Ftplet {

    private static String[] forbiddenCommonCommands;

    @Override
    public void init(FtpletContext ftpletContext) throws FtpException {
        System.out.println(new Object(){}.getClass().getEnclosingMethod().getName());
    }

    @Override
    public void destroy() {
        System.out.println(new Object(){}.getClass().getEnclosingMethod().getName());
    }

    @Override
    public FtpletResult beforeCommand(FtpSession ftpSession, FtpRequest ftpRequest) throws FtpException, IOException {
        System.out.println(new Object(){}.getClass().getEnclosingMethod().getName());

        /* avoid deletion of files inc common folder */
        for (String command: forbiddenCommonCommands) {
            if (ftpRequest.getCommand().equals(command)) {
                /* Only admin is allowed to delete files */
                if (!ftpSession.getUser().getName().equals("admin")) {
                    System.out.println("Attempt to remove common file by non admin user");
                    return null;
                }
            }
        }

        return null;
    }

    @Override
    public FtpletResult afterCommand(FtpSession ftpSession, FtpRequest ftpRequest, FtpReply ftpReply) throws FtpException, IOException {
        System.out.println(new Object(){}.getClass().getEnclosingMethod().getName());
        return null;
    }

    @Override
    public FtpletResult onConnect(FtpSession ftpSession) throws FtpException, IOException {
        System.out.println(new Object(){}.getClass().getEnclosingMethod().getName());
        return null;
    }

    @Override
    public FtpletResult onDisconnect(FtpSession ftpSession) throws FtpException, IOException {
        System.out.println(new Object(){}.getClass().getEnclosingMethod().getName());
        return null;
    }

    public static String[] getForbiddenCommonCommands() {
        return forbiddenCommonCommands;
    }

//    public void setForbiddenCommonCommands(String[] forbiddenCommonCommands) {
//        this.forbiddenCommonCommands = forbiddenCommonCommands;
//    }

    public void setForbiddenCommonCommands(String forbiddenCommonCommands) {
        DefaultFtplet.forbiddenCommonCommands = forbiddenCommonCommands.split(";");
    }

}
