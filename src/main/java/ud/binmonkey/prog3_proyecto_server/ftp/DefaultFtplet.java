package ud.binmonkey.prog3_proyecto_server.ftp;

import org.apache.ftpserver.ftplet.*;

import java.io.IOException;

public class DefaultFtplet implements Ftplet {
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
}
