package ud.binmonkey.prog3_proyecto_server.common;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;

public class DocumentReader {

    /* Logger for DocumentReader */
    private static final boolean ADD_TO_FIC_LOG = false; /* set false to overwrite */
    private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DocumentReader.class.getName());

    static {
        try {
            logger.addHandler(new FileHandler(
                    "logs/" + DocumentReader.class.getName() + ".log.xml", ADD_TO_FIC_LOG));
        } catch (SecurityException | IOException e) {
            logger.log(Level.SEVERE, "Error in log file creation");
        }
    }
    /* END Logger for DocumentReader */

    public static Document getDoc(String path) {

        Document document = null;  /*TODO: find a better way to handle this*/

        try {

            File file = new File(path);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(file);

        } catch (FileNotFoundException fnne) {
            logger.log(Level.SEVERE, "File not found");
            fnne.printStackTrace();

        } catch (ParserConfigurationException pce) {
            logger.log(Level.SEVERE, "Incorrect config file");
            pce.printStackTrace();
        } catch (IOException | SAXException ex) {
            ex.printStackTrace();
        }

        return document;
    }
}
