package ud.binmonkey.prog3_proyecto_server.ftp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.FtpException;
import org.springframework.context.support.FileSystemXmlApplicationContext;



public class FTPServer {

    private FtpServer server;

    public FTPServer(String configuration, String bean) {
        server = new FileSystemXmlApplicationContext(configuration).getBean(
                bean, FtpServer.class);
    }

    public static void main(String[] args) throws FtpException {
        (new FTPServer("conf/FTPServer.xml", "myServer")).server.start();
    }

    public FtpServer getServer() {
        return server;
    }

}
