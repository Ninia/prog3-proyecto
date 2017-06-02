package ud.binmonkey.prog3_proyecto_server.mysql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;

public class MySQLSetup {


    public static void dbSetup() {
        try {
            MySQL mySQL = new MySQL();
            mySQL.startSession();

            ScriptRunner scriptRunner = new ScriptRunner(mySQL.getConnect(), false, false);
            Reader reader = new BufferedReader(new FileReader("src/main/resources/mysql/setup/setup.sql"));
            scriptRunner.runScript(reader);

            mySQL.clearDB();
            mySQL.closeSession();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        MySQLSetup.dbSetup();
    }
}
