package ud.main.utils.network;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ud.main.common.Pair;
import ud.main.utils.DocumentReader;

import java.util.ArrayList;
import java.util.Arrays;

public class URI {

    public static class REST {

        static class RequestTypeException extends Exception {
            public RequestTypeException(){}
            public RequestTypeException(String type){
                super("Wrong 'sendRequest' method for type: " + type);
            }
        }
        static class UnsupportedRequestTypeException extends Exception {
            public UnsupportedRequestTypeException(){}
            public UnsupportedRequestTypeException(String type){
                super("Request type not supported.");
            }
        }

        /** Todo: more request types
         *
         * @param request: GET
         * @param host
         * @param port
         * @param path
         * @param args
         * @return
         * @throws Exception
         * @throws RequestTypeException
         * @throws UnsupportedRequestTypeException
         */
        public static String sendRequest(Requests request, String host,
                                         int port, String path, Pair... args )
                throws Exception, RequestTypeException, UnsupportedRequestTypeException { /*General exception overrides specific*/

            String response;
            String uri = getURI(host, port, path, new ArrayList<Pair>(Arrays.asList(args)));

            switch (request) {
                case GET:
                    response = Request.httpGET(uri);
                    break;
                case POST:
                    throw new RequestTypeException(request.name());
                default:
                    throw new UnsupportedRequestTypeException(request.name());
            }
            return response;
        }

        /** Todo: add more request types
         *
         * @param request POST, PUT...
         * @param host hostname of target
         * @param port port of target
         * @param path rest path of target
         * @param payload payload contained in the request
         * @param args Rest args, in pairs of <key, value>
         * @return response of server
         * @throws Exception
         */
        public static String sendRequest(Requests request, String host,
                                         int port, String path, String payload,  Pair... args )
                throws Exception, RequestTypeException, UnsupportedRequestTypeException { /*General exception overrides specific*/

            String response;
            String uri = getURI(host, port, path, new ArrayList<Pair>(Arrays.asList(args)));

            switch (request) {
                case GET:
                    throw new RequestTypeException(request.name());
                case POST:
                    response = Request.httpPOST(uri, payload);
                    break;
                default:
                    throw new UnsupportedRequestTypeException(request.name());
            }
            return response;
        }


        public static String getURI(String host, int port, String path, ArrayList<Pair> args) {
            String uri = host + ":" + Integer.toString(port) + path;
            if (args.size() > 0) {
                uri += "?" + args.get(0).getKey() + "=" + args.get(0).getValue();
                for (Pair pair: args.subList(1, args.size() - 1)) {
                    uri += "?" + pair.getKey() + "=" + pair.getValue();
                }
            }
            return uri;
        }

    }


    private static NodeList getService(String service) {
        Document document = DocumentReader.getDoc("config/Network.xml");
        return document.getElementsByTagName(service);
    }

    public static String getURI(String service) {

        NodeList networkNode = getService(service);
        Element network = (Element) networkNode.item(0);
        String url = network.getElementsByTagName("host").item(0).getTextContent();
        String port = network.getElementsByTagName("port").item(0).getTextContent();

        return url + ":" + port;

    }

    public static int getPort(String service) {

        NodeList networkNode = getService(service);
        Element network = (Element) networkNode.item(0);
        String port = network.getElementsByTagName("port").item(0).getTextContent();

        return Integer.parseInt(port);
    }

    public static String getHost(String service) {

        NodeList networkNode = getService(service);
        Element network = (Element) networkNode.item(0);
        return network.getElementsByTagName("host").item(0).getTextContent();
    }


}
