package ud.binmonkey.prog3_proyecto_server.ftp;

import org.apache.ftpserver.ftplet.FtpException;
import org.junit.Before;
import org.junit.Test;
import ud.binmonkey.prog3_proyecto_server.common.TextFile;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;


public class FTPServerTest {

    private FTPServer testServer;
    private String confFile = "src/test/resources/ftp/conf.xml";
    private String bean = "testServer";

    @Before
    public void setUp() throws Exception {
        /* Init server */
        /* Load configuration */
        testServer = new FTPServer(confFile, bean);
    }

    @Test
    /**
     *  Check if port specified in configuration is available
     */
    public void portAvailable() {
        try {
            testServer.getServer().start();
            testServer.getServer().stop();

        } catch (org.apache.ftpserver.FtpServerConfigurationException e) {
            if (e.getCause() instanceof IOException) {
                System.err.print("\n --- Port specified in ftplet:`" + bean +
                        "` in file:`" + confFile +  "` WAS NOT AVAILABLE --- \n");
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

        /* Check if forbidden commands are loaded correctly from file*/
        DefaultFtplet ftplet = new DefaultFtplet();

        /* dirty sorcery to load commands from file*/
        String[] commands = TextFile.read(confFile).replaceAll(" ", "").split(
                            "propertyname=\"forbiddenCommonCommands\"value=")[1].split(
                            "/>")[0].replace("\"", "").split(";");

        String[] forbidden = DefaultFtplet.getForbiddenCommonCommands();

        Arrays.sort(commands);
        Arrays.sort(forbidden);

        assertArrayEquals(commands, forbidden);
    }
}