package ud.binmonkey.prog3_proyecto_server.common.network;

import ud.binmonkey.prog3_proyecto_server.common.Pair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Request {

    private static final String USER_AGENT = "Mozilla/5.0";

    /* HTTP GET request */
    static String httpGET(String targetURL) throws Exception {

        URL obj = new URL(targetURL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        /* optional default is GET */
        con.setRequestMethod("GET");

        /* add request header */
        con.setRequestProperty("User-Agent", USER_AGENT);


        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();

    }


    /*
     *
     * Simple requests
     *
     *
     */

    /* HTTP POST request */
    static String httpPOST(String targetURL, String payload) throws Exception {

        URL obj = new URL(targetURL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        /* add request header */
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        /* Send post request */
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(payload);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();

    }

    public static class REST {

        /**
         * (GET) Todo: more request types
         *
         * @param request: GET
         * @param host     hostname of target
         * @param port     port of target
         * @param path     rest path of target
         * @param args     Rest args, in pairs of <key, value>
         * @return response of server
         */
        static String sendRequest(HttpMethods request, String host,
                                  int port, String path, ArrayList<Pair> args)
                throws Exception { /*General exception overrides specific*/

            String response;
            String uri = getURI(host, port, path, args);

            switch (request) {
                case GET:
                    response = Request.httpGET(uri);
                    break;
                case POST:
                    throw new RequestTypeException(request.name());
                default:
                    throw new UnsupportedRequestTypeException();
            }
            return response;
        }

        /**
         * (GET) same, but takes as many pairs as wanted instead of arraylist
         *
         * @param request: GET
         * @param host     hostname of target
         * @param port     port of target
         * @param path     rest path of target
         * @param args     Rest args, in pairs of <key, value>
         * @return response of server
         */
        @SuppressWarnings({"SameParameterValue", "UnusedReturnValue" /* they may be used in the future */})
        public static String sendRequest(HttpMethods request, String host,
                                         int port, String path, Pair... args)
                throws Exception { /*General exception overrides specific*/

            return sendRequest(request, host, port, path, new ArrayList<>(Arrays.asList(args)));
        }

        /**
         * (POST, PUSH) Todo: add more request types
         *
         * @param request POST, PUT...
         * @param host    hostname of target
         * @param port    port of target
         * @param path    rest path of target
         * @param payload payload contained in the request
         * @param args    Rest args, in pairs of <key, value>
         * @return response of server
         */
        static String sendRequest(HttpMethods request, String host,
                                  int port, String path, String payload, ArrayList<Pair> args)
                throws Exception { /*General exception overrides specific*/

            String response;
            String uri = getURI(host, port, path, args);

            switch (request) {
                case GET:
                    throw new RequestTypeException(request.name());
                case POST:
                    response = Request.httpPOST(uri, payload);
                    break;
                default:
                    throw new UnsupportedRequestTypeException();
            }
            return response;
        }

        /**
         * (POST, PUSH) same, but takes as many pairs as wanted instead of arraylist
         *
         * @param request POST, PUT...
         * @param host    hostname of target
         * @param port    port of target
         * @param path    rest path of target
         * @param payload payload contained in the request
         * @param args    Rest args, in pairs of <key, value>
         * @return response of server
         */
        @SuppressWarnings({"SameParameterValue", "UnusedReturnValue" /* they may be used in the future */})
        public static String sendRequest(HttpMethods request, String host,
                                         int port, String path, String payload, Pair... args)
                throws Exception { /*General exception overrides specific*/

            return sendRequest(request, host, port, path, payload, new ArrayList<>(Arrays.asList(args)));
        }

        static String getURI(String host, int port, String path, ArrayList<Pair> args) {
            StringBuilder uri = new StringBuilder(host + ":" + Integer.toString(port) + path);
            if (args.size() > 0) {
                uri.append("?").append(args.get(0).getKey()).append("=").append(args.get(0).getValue());
                if (args.size() > 1) {
                    for (Pair pair : args.subList(1, args.size() - 1)) {
                        uri.append("?").append(pair.getKey()).append("=").append(pair.getValue());
                    }
                }
            }
            return uri.toString();
        }

        static class RequestTypeException extends Exception {
            RequestTypeException(String type) {
                super("Wrong 'sendRequest' method for type: " + type);
            }
        }

        static class UnsupportedRequestTypeException extends Exception {
            UnsupportedRequestTypeException() {
                super("Request type not supported.");
            }
        }
    }

}
