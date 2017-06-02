package ud.binmonkey.prog3_proyecto_server;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.FtpException;
import ud.binmonkey.prog3_proyecto_server.common.DocumentReader;
import ud.binmonkey.prog3_proyecto_server.ftp.FTPServer;
import ud.binmonkey.prog3_proyecto_server.ftp.FTPlet;
import ud.binmonkey.prog3_proyecto_server.http.HTTPSServer;

import java.io.IOException;

/**
 * Main class of project
 */
public enum Server {
    INSTANCE;

    HTTPSServer httpsServer;
    FtpServer ftpServer;
    String ftpLetFile = DocumentReader.getAttr(DocumentReader.getDoc("conf/properties.xml"),
            "network", "ftp-server", "ftplet-file").getTextContent();

    /**
     * Create instances of all services
     */
    Server() {
        /* create admin and common users */
        FTPServer.init();

        /* main services */
        this.httpsServer = HTTPSServer.INSTANCE;
        this.ftpServer = FTPServer.getFtpServer(ftpLetFile, FTPlet.class.getSimpleName());
    }

    /**
     * Start instances
     */
    public void start() {
        try {
            this.httpsServer.init();
            this.httpsServer.getHttpsServer().start();
            this.ftpServer.start();
        } catch (IOException | FtpException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Server.INSTANCE.start();
    }
}
