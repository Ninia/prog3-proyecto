package ud.binmonkey.prog3_proyecto_server.neo4j;

import org.junit.Test;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;

import static org.junit.Assert.assertEquals;

/**
 * Test class for Neo4j class
 * <p>
 * This tests are not meant to be run all at once rather one at a time
 */
public class Neo4jTest {

    /* Adding Tests */
    @Test
    public void addTest() {
        Neo4j neo4j = new Neo4j();
        neo4j.clearDB();

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

        Neo4j neo4j = new Neo4j();
        neo4j.clearDB();

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

    /**
     * Test queries
     */
    @Test
    public void queryTests() {

        StatementResult result;
        Record record;

        Neo4j neo4j = new Neo4j();
        neo4j.clearDB();

        /* Adding Movies */
        neo4j.addTitle("tt0117951"); /* Trainspotting */
        neo4j.addTitle("tt0289043"); /* 28 Days Later... */
        neo4j.addTitle("tt0470752"); /* Ex Machina */
        neo4j.addTitle("tt0137523"); /* Fight Club */
        neo4j.addTitle("tt0301357"); /* Goodbye Lenin */

        /* LOTR Movies */
        neo4j.addTitle("tt0120737");
        neo4j.addTitle("tt0167261");
        neo4j.addTitle("tt0167260");

        /* Star Wars Movies */
        neo4j.addTitle("tt0120915");
        neo4j.addTitle("tt0121765");
        neo4j.addTitle("tt2488496");
        neo4j.addTitle("tt0076759");
        neo4j.addTitle("tt0080684");
        neo4j.addTitle("tt0086190");
        neo4j.addTitle("tt0121766");

        /* Adding Series */
        neo4j.addTitle("tt2802850"); /* Fargo */

        /* Adding Episodes */
        neo4j.addTitle("tt3097534"); /* Fargo S01E01 */
        neo4j.addTitle("tt3296848"); /* Fargo S01E02 */
        neo4j.addTitle("tt3519062"); /* Fargo S01E03 */
        neo4j.addTitle("tt3578722"); /* Fargo S01E04 */
        neo4j.addTitle("tt3514096"); /* Fargo S01E05 */

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

        System.out.println("\nList movies produced by a Producer, ordering them by year and rating:");
        result = neo4j.getSession().run("MATCH p = (n:Producer)-[r:" +
                " PRODUCED]->(m:Movie)" +
                " WHERE n.name = '20th Century Fox'" +
                " RETURN m.title, m.imdbRating, m.year" +
                " ORDER BY m.year, m.imdbRating DESCENDING");

        while (result.hasNext()) {
            record = result.next();
            System.out.println(record.toString());

            assertEquals(false, record.toString().isEmpty());
        }

        System.out.println("\nList languages and the number of title they are spoken in," +
                " ordering them by the number of title:");
        result = neo4j.getSession().run("MATCH(a:Language)-[b:" +
                " SPOKEN_LANGUAGE]->(c)" +
                " RETURN a.name, COUNT(c)" +
                " ORDER BY COUNT(c) DESCENDING");

        while (result.hasNext()) {
            record = result.next();
            System.out.println(record.toString());

            assertEquals(false, record.toString().isEmpty());
        }

        System.out.println("\nList movies of a Language, ordering by year:");
        result = neo4j.getSession().run("MATCH p = (n:Language)-[r:" +
                " SPOKEN_LANGUAGE]->(m:Movie)" +
                " WHERE n.name = 'English'" +
                " RETURN m.title, m.imdbRating, m.year" +
                " ORDER BY m.year");

        while (result.hasNext()) {
            record = result.next();
            System.out.println(record.toString());

            assertEquals(false, record.toString().isEmpty());
        }

        System.out.println("\nList movies of a Language, ordering by year:");
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
    }
}