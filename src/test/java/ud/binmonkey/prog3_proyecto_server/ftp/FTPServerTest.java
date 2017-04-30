package ud.binmonkey.prog3_proyecto_server.ftp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.FtpException;
import org.junit.Before;
import org.junit.Test;
import ud.binmonkey.prog3_proyecto_server.common.TextFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

import static org.junit.Assert.assertArrayEquals;


public class FTPServerTest {

    private FtpServer testServer;
    private String confPath = "src/test/resources/ftp/conf.xml";
    private String bean = "testServer";

    @Before
    public void setUp() throws Exception {
        /* Init server */
        /* Load configuration */
        testServer = FTPServer.getFtpServer(confPath, bean);
    }

    @Test
    /**
     *  Check if port specified in configuration is available
     */
    public void portAvailable() {
        try {
            testServer.start();
            testServer.stop();

        } catch (org.apache.ftpserver.FtpServerConfigurationException e) {
            if (e.getCause() instanceof IOException) {
                System.err.print("\n --- Port specified in ftplet:`" + bean +
                        "` in file:`" + confPath +  "` WAS NOT AVAILABLE --- \n");
            } else {
                e.printStackTrace();
            }
        } catch (FtpException e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

    @Test
    /**
     * Check correct configuration of class properties in xml file
     */
    public void loadProperties() throws IOException {

        /* Regex to obtain all property elements*/
        String propertiesRegex = "name=\"forbiddenCommonCommands\"( )?value=\"(\\w+[;|\"]+)+";
        String confFile = TextFile.read(confPath);

        /* Check if forbidden commands are loaded correctly from file*/
        FTPlet ftplet = new FTPlet();

        /* dirty sorcery to load commands from file*/
        if (Pattern.compile(propertiesRegex).matcher(confFile).find())
        {
            String[] commands = confFile.split("forbiddenCommonCommands")[1].replaceAll("( |\")", "").split(
                    "value=")[1].split("/>")[0].split(";");

            String[] forbidden = FTPlet.getForbiddenCommonCommands();

            Arrays.sort(commands);
            Arrays.sort(forbidden);

            assertArrayEquals(commands, forbidden);
        }
    }
}