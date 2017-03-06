package ud.main.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DocumentReader {
    public static Document getDoc(String path) {

        Document document = null;  /*TODO: find a better way to handle this*/

        try {

            File file = new File(path);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(file);

        } catch (FileNotFoundException fnne) {
            System.err.println("FILE NOT FOUND.");
            fnne.printStackTrace();

        } catch (ParserConfigurationException pce) {
            System.err.println("INCORRECT CONFIGURATION FILE");
            pce.printStackTrace();
        } catch (IOException | SAXException ex) {
            ex.printStackTrace();
        }

        return document;
    }
}
