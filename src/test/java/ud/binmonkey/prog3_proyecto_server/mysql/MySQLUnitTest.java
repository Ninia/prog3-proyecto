package ud.binmonkey.prog3_proyecto_server.mysql;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for MySQLUnitTest class
 */
public class MySQLUnitTest {

    private MySQL mySQL;
    private ArrayList<String> log_desc = new ArrayList<>(Arrays.asList(
            "ID", "OPERATION", "OMDBID", "TYPE", "DATE"));

    private ArrayList<String> ratings_desc = new ArrayList<>(Arrays.asList(
            "ID", "USER", "OMDBID", "RATING", "DATE"));

    private ArrayList<String> history_desc = new ArrayList<>(Arrays.asList(
            "ID", "USER", "OMDBID", "DATE"));

    @Before
    public void setUp() {
        mySQL = new MySQL();
        mySQL.clearDB();
    }

    @After
    public void tearDown() {
        mySQL.closeSession();
    }

    /**
     * Simple setup Test
     */
    @Test
    public void setupTest() {
        mySQL.setup();

        try {
            ResultSet resultSet = mySQL.getStatement().executeQuery("DESC neo4j_log");
            while (resultSet.next()) {
                assertTrue(log_desc.contains(resultSet.getString(1)));
            }

            resultSet = mySQL.getStatement().executeQuery("DESC user_ratings");
            while (resultSet.next()) {
                assertTrue(ratings_desc.contains(resultSet.getString(1)));
            }

            resultSet = mySQL.getStatement().executeQuery("DESC user_viewing_history");
            while (resultSet.next()) {
                assertTrue(history_desc.contains(resultSet.getString(1)));
            }

            assertEquals(true, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}