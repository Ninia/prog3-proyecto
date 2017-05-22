package ud.binmonkey.prog3_proyecto_server.common.posix;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShellCommand {

    /**
     * Run a shell command
     * @param command command to be run
     * @return stout of command
     */
    public static String executeCommand(String command) {

        StringBuilder output = new StringBuilder();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            @SuppressWarnings("UnusedAssignment")
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }

}
