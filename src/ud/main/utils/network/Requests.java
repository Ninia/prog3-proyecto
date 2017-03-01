package ud.main.utils.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Requests {

    private static final String USER_AGENT = "Mozilla/5.0";

    /* HTTP GET request */
    public static String httpGET(String targetURL) throws Exception {

        URL obj = new URL(targetURL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        /* optional default is GET */
        con.setRequestMethod("GET");

        /* add request header */
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();


        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();

    }


    /* HTTP POST request */
    public static String httpPOST(String targetURL, String payload) throws Exception {

        URL obj = new URL(targetURL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        /* add request header */
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = payload;
        /* Send post request */
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();

    }

}
