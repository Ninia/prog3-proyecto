package ud.main.utils.network;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ud.main.utils.ReadDocument;

public class URI {

    public static String getURI(String service) {

        Document document = ReadDocument.getDoc("config/Network.xml");

        NodeList networkNode = document.getElementsByTagName(service);

        Element network = (Element) networkNode.item(0);
        String url = network.getElementsByTagName("host").item(0).getTextContent();
        String port = network.getElementsByTagName("port").item(0).getTextContent();

        return url + ":" + port;

    }
}
