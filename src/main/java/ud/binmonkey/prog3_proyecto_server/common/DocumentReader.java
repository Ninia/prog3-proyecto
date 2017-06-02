package ud.binmonkey.prog3_proyecto_server.common;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DocumentReader {

    /**
     * Reads XML file and returns
     * @param path @org.w3c.dom.Document
     * @return @org.w3c.dom.Document
     */
    public static Document getDoc(String path) {

        Document document = null;  /*TODO: find a better way to handle this*/

        try {

            File file = new File(path);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(file);

        } catch (FileNotFoundException fnne) {
            fnne.printStackTrace();

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException | SAXException ex) {
            ex.printStackTrace();
        }

        return document;
    }

    /**
     * Get attributes of XML Document
     * @param doc @org.w3c.dom.Document
     * @param attributes list of attributes in order. Last attr is inside @Element attr[-2],
     *                   which is inside @Element attr[-3]...
     * @return @Element with final result
     */
    public static Element getAttr(Document doc, String... attributes) {
        Element element = (Element) doc.getElementsByTagName(attributes[0]).item(0);
        for (int i = 1; i < attributes.length; i++) {
            element = (Element) element.getElementsByTagName(attributes[i]).item(0);
        }
        return element;
    }
}
