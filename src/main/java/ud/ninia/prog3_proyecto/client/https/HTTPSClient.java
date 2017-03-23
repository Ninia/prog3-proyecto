package ud.ninia.prog3_proyecto.client.https;

import ud.ninia.prog3_proyecto.utils.network.URI;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;

public class HTTPSClient {
    private String host = URI.getHost("https-client");
    private int port = URI.getPort("https-client");

    static {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> hostname.equals("localhost"));
    }

    public HTTPSClient() {
    }

    public SSLContext createSSLContext() {
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("src/test/resources/keys/httpserver.jks"), "changeit".toCharArray());

            /* key manager */
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, "changeit".toCharArray());
            KeyManager[] kms = kmf.getKeyManagers();

            /* trust manager */
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            TrustManager[] tms = tmf.getTrustManagers();

            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            sslContext.init(kms, tms, null);

            return sslContext;

        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException |
                UnrecoverableKeyException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void run() {

        SSLContext sslContext = this.createSSLContext();

        try {
            HttpsURLConnection conn = (HttpsURLConnection) new URL("https://" + host + ":" + port).openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            System.out.println("RESPONSE: " + conn.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    static class ClientThread extends Thread {

        private SSLSocket sslSocket;

        ClientThread(SSLSocket sslSocket) {
            this.sslSocket = sslSocket;
        }

        /* @Override */
        public void run() {
            sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());

            try {
                sslSocket.startHandshake();
                SSLSession sslSession = sslSocket.getSession();

                System.out.println("SSLSession :");
                System.out.println("\tProtocol : "+sslSession.getProtocol());
                System.out.println("\tCipher suite : "+sslSession.getCipherSuite());

                /* Handle application content */
                InputStream is = sslSocket.getInputStream();
                OutputStream os = sslSocket.getOutputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));

                /* write data */
                pw.println("Hi there");
                pw.flush();

                /* read response */
                String response;
                while((response = br.readLine()) != null) {
                    System.out.println("Input: " + response);

                    if(response.trim().equals("HTTP/1.1 200\r\n")){
                        break;
                    }
                }
                sslSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    public static void main(String[] args) {
        HTTPSClient httpsClient = new HTTPSClient();
        httpsClient.run();
    }

}
