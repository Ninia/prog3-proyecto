package ud.main.server.https;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import org.w3c.dom.Element;
import ud.main.utils.DocumentReader;
import ud.main.utils.network.URI;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {

    private HttpsServer httpsServer;
    private HashMap<String, HttpHandler> contexts = new HashMap<String, HttpHandler>() {{
        put("/test", new HttpHandlers.ServerHandlers.TestHandler());
        put("/antigravity", new HttpHandlers.ServerHandlers.AntigravityHandler());
    }};


    public Server() throws IOException {

        try {

            /* obtain keyword from config xml*/
            Element settings = (Element) DocumentReader.getDoc(
                         "config/Network.xml").getElementsByTagName("https-server").item(0);
            String keyword = settings.getElementsByTagName("keyword").item(0).getTextContent();

            /* initialize ssl context */
            SSLContext sslContext = SSLContext.getInstance("TLS");

            /* password from keyword */
            char[] passwd = keyword.toCharArray();

            /* configure key store */
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load( new FileInputStream("keys/test.keystore"), passwd);

            /* configure key management factory */
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, passwd);

            /* configure trust manager factory */
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);

            /* setup SSL context */
            sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            /* create https server */
            this.httpsServer = HttpsServer.create(new InetSocketAddress(  URI.getHost("https-server"),
                    URI.getPort("https-server")), 0);

            /* set https configurator */
            this.httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
                public void configure(HttpsParameters parameters) {
                    try {

                        /* initialize ssl context */
                        SSLContext sslC = SSLContext.getDefault();
                        SSLEngine sslEngine = sslC.createSSLEngine();
                        parameters.setNeedClientAuth(false);
                        parameters.setCipherSuites( sslEngine.getEnabledCipherSuites() );
                        parameters.setProtocols( sslEngine.getEnabledProtocols() );

                        /* set default parameters */
                        parameters.setSSLParameters( sslC.getDefaultSSLParameters() );


                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            });

            /* assign contexts*/
            for (String context: contexts.keySet()) {
                httpsServer.createContext(context, contexts.get(context));
            }

            /* set multithreaded executor */
            this.httpsServer.setExecutor(   new ThreadPoolExecutor(4, 8, 30,
                                            TimeUnit.SECONDS,
                                            new ArrayBlockingQueue<>(100)));

        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException |
                CertificateException | KeyManagementException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        try {
            (new Server()).httpsServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
