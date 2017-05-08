package ud.binmonkey.prog3_proyecto_server.neo4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import ud.binmonkey.prog3_proyecto_server.mysql.MySQL;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * Test class for Neo4j class
 * <p>
 * This tests are not meant to be run all at once rather one at a time
 */
public class Neo4jTest {

    private Neo4j neo4j;

    @Before
    public void setUp() {

        /* Clear DWH */
        MySQL mySQL = new MySQL();
        try {
            mySQL.clearDB();
        } catch (SQLException ignored) {
        }

        neo4j = new Neo4j();
        neo4j.clearDB();
    }

    @After
    public void tearDown() {
        neo4j.closeSession();
    }

    /* Adding Tests */

    /**
     * Simple Test
     */
    @Test
    public void addTest() {

        neo4j.addTitle("tt0117951"); /* Trainspotting */

        StatementResult result = neo4j.getSession().run("MATCH (n:Movie) " +
                "RETURN n.name AS name, n.title AS title");

        while (result.hasNext()) {
            Record record = result.next();

            assertEquals("Trainspotting", record.get("title").asString());
            assertEquals("tt0117951", record.get("name").asString());
        }
    }

    /**
     * Tests adding Episodes without adding the Series
     */
    @Test
    public void addEpisodesTest() {

        /* Adding Episodes */
        neo4j.addTitle("tt2169080");
        neo4j.addTitle("tt3333824");
        neo4j.addTitle("tt3333828");
        neo4j.addTitle("tt3333830");
        neo4j.addTitle("tt3333832");

        StatementResult result = neo4j.getSession().run("MATCH (n:Series) " +
                "RETURN n.name AS name, n.title AS title");

        while (result.hasNext()) {
            Record record = result.next();

            assertEquals("Rick and Morty", record.get("title").asString());
            assertEquals("tt2861424", record.get("name").asString());
        }
    }

    @Test
    public void addListTest() {
        neo4j.addList("Lord of The Rings Saga", "tt0120737", "tt0167261");
        neo4j.addList("Lord of The Rings Saga", "tt0167260", "tt0167261");

        StatementResult result = neo4j.getSession().run("MATCH p=(n:List)-[r:CONTAINS]-(m) " +
                "WHERE m.name='tt0167260' RETURN n.name AS name");

        while (result.hasNext()) {
            Record record = result.next();

            assertEquals("Lord of The Rings Saga", record.get("name").asString());
        }
    }

    /**
     * Test rename
     */
    @Test
    public void renameTest() {

        /* Star Wars Movies */
        neo4j.addList("Star Wars Saga", "tt0120915", "tt0121765", "tt2488496", "tt0076759", "tt0080684",
                "tt0086190", "tt0121766");

        /* Fixes duplicate because of different name */
        neo4j.renameNode("Twentieth Century Fox", "20th Century Fox", "Producer");
    }

    /**
     * Test queries
     * TODO better assert equals
     */
    @Test
    public void queryTests() {

        StatementResult result;
        Record record;

        /* Adding Movies */
        neo4j.addTitle("tt0117951"); /* Trainspotting */
        neo4j.addTitle("tt0289043"); /* 28 Days Later... */
        neo4j.addTitle("tt0470752"); /* Ex Machina */
        neo4j.addTitle("tt0137523"); /* Fight Club */
        neo4j.addTitle("tt0301357"); /* Goodbye Lenin */

        /* LOTR Movies */
        neo4j.addList("Lord of The Rings Saga", "tt0120737", "tt0167261", "tt0167260");

        /* Star Wars Movies */
        neo4j.addList("Star Wars Saga", "tt0120915", "tt0121765", "tt2488496", "tt0076759", "tt0080684",
                "tt0086190", "tt0121766");

        /* Adding Series */
        neo4j.addTitle("tt2802850"); /* Fargo */

        /* Adding Episodes */
        neo4j.addTitle("tt3097534"); /* Fargo S01E01 */
        neo4j.addTitle("tt3296848"); /* Fargo S01E02 */
        neo4j.addTitle("tt3519062"); /* Fargo S01E03 */
        neo4j.addTitle("tt3578722"); /* Fargo S01E04 */
        neo4j.addTitle("tt3514096"); /* Fargo S01E05 */

        /* Fixes duplicate because of different name */
        neo4j.renameNode("Twentieth Century Fox", "20th Century Fox", "Producer");

        System.out.println("\nList titles where a certain person acted, ordering them by rating:");
        result = neo4j.getSession().run("MATCH p = (n:Person)-[r:ACTED_IN]->(m) " +
                " WHERE n.name = 'Ewan McGregor'" +
                " RETURN m.title, m.imdbRating" +
                " ORDER BY m.imdbRating DESCENDING");

        while (result.hasNext()) {
            record = result.next();
            System.out.println(record.toString());

            assertEquals(false, record.toString().isEmpty());
        }

        System.out.println("\nList 5 best Movies in the DB:");

        result = neo4j.getSession().run("MATCH(m:Movie)" +
                " RETURN m.title, m.imdbRating" +
                " ORDER BY m.imdbRating DESCENDING" +
                " LIMIT 5");

        while (result.hasNext()) {
            record = result.next();
            System.out.println(record.toString());

            assertEquals(false, record.toString().isEmpty());
        }

        System.out.println("\nList Episodes of a Series, ordering by season and episode:");
        result = neo4j.getSession().run("MATCH p = (n:Episode)-[r:" +
                " BELONGS_TO]->(m:Series)" +
                " WHERE m.name = 'tt2802850'" +
                " RETURN n.title, r.season, r.episode" +
                " ORDER BY r.season, r.episode");

        while (result.hasNext()) {
            record = result.next();
            System.out.println(record.toString());

            assertEquals(false, record.toString().isEmpty());
        }

        System.out.println("\nShow Titles by Age Rating");
        result = neo4j.getSession().run("MATCH p=(n)-[r:RATED]->(m:Movie)" +
                " RETURN n.name, m.name" +
                " ORDER BY n.name, m.year");

        while (result.hasNext()) {
            record = result.next();
            System.out.println(record.toString());

            assertEquals(false, record.toString().isEmpty());
        }
    }
}