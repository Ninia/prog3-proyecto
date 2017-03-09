package ud.main.utils.network;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ud.main.utils.DocumentReader;

public class URI {

    private static NodeList getService(String service) {
        Document document = DocumentReader.getDoc("config/Network.xml");
        return document.getElementsByTagName(service);
    }

    /** Obtain URI of service specified on config/Network.xml
     *
     * @param service wanted service
     * @return string containing URI
     */
    public static String getURI(String service) {

        NodeList networkNode = getService(service);
        Element network = (Element) networkNode.item(0);
        String url = network.getElementsByTagName("host").item(0).getTextContent();
        String port = network.getElementsByTagName("port").item(0).getTextContent();

        return url + ":" + port;

    }

    /** Obtain port of service specified on config/Network.xml
     *
     * @param service wanted service
     * @return string containing port
     */
    public static int getPort(String service) {

        NodeList networkNode = getService(service);
        Element network = (Element) networkNode.item(0);
        String port = network.getElementsByTagName("port").item(0).getTextContent();

        return Integer.parseInt(port);
    }

    /** Obtain ns of service specified on config/Network.xml
     *
     * @param service wanted service
     * @return string containing ns
     */
    public static String getHost(String service) {

        NodeList networkNode = getService(service);
        Element network = (Element) networkNode.item(0);
        return network.getElementsByTagName("host").item(0).getTextContent();
    }


}
