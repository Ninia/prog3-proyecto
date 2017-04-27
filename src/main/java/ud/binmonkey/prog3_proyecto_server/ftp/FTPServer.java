package ud.binmonkey.prog3_proyecto_server.ftp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.FtpException;
import org.springframework.context.support.FileSystemXmlApplicationContext;



public class FTPServer {

    private FtpServer server;

    public FTPServer() {

        /* Extended version for debug */

        FileSystemXmlApplicationContext fileSysContext = new FileSystemXmlApplicationContext(
                "conf/FTPServer.xml");
        assert true;
        server = fileSysContext.getBean("myServer", FtpServer.class);
        /* /* short version *\/
        server = new FileSystemXmlApplicationContext("conf/FTPServer.xml").getBean(
                "myServer", FtpServer.class); */
    }

    public static void main(String[] args) throws FtpException {
        (new FTPServer()).server.start();
    }
}
