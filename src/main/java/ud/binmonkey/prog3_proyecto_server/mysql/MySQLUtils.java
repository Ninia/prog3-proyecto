package ud.binmonkey.prog3_proyecto_server.mysql;

import ud.binmonkey.prog3_proyecto_server.omdb.MediaType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;

public class MySQLUtils extends MySQL {

    public static void main(String[] args) {
        MySQLUtils mySQL = new MySQLUtils();
        mySQL.dbSetup();
    }

    public void dbSetup() {
        try {

            ScriptRunner scriptRunner = new ScriptRunner(getConnect(), false, false);
            Reader reader = new BufferedReader(new FileReader("src/main/resources/mysql/setup/setup.sql"));
            scriptRunner.runScript(reader);

            clearDB();
            closeSession();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Logs the creation of a Title in the DWH
     *
     * @param id   - imdbID of the Title
     * @param type - MediaType of the Title
     */
    public void dwhLog(String operation, String id, MediaType type) {
        try {

            getStatement().executeUpdate("INSERT INTO neo4j_log VALUES (default, '" + operation + "'," +
                    " '" + id + "'," + " '" + type.toString() + "', CURRENT_TIMESTAMP);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
