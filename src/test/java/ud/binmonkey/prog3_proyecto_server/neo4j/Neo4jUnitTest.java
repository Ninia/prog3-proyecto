package ud.binmonkey.prog3_proyecto_server.neo4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import ud.binmonkey.prog3_proyecto_server.omdb.Omdb;
import ud.binmonkey.prog3_proyecto_server.omdb.OmdbEpisode;
import ud.binmonkey.prog3_proyecto_server.omdb.OmdbMovie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class for Neo4j class
 * This tests are not meant to be run all at once rather one at a time
 */
public class Neo4jUnitTest {

    private Neo4jUtils neo4j;

    @Before
    public void setUp() {

        neo4j = new Neo4jUtils();
        neo4j.clearDB();
    }

    @After
    public void tearDown() {
        neo4j.closeSession();
    }

    /**
     * Simple adding Test
     */
    @Test
    public void addTest() {

        neo4j.addTitle(new OmdbMovie(Omdb.getTitle("tt0117951"))); /* Trainspotting */

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
        neo4j.addTitle(new OmdbEpisode(Omdb.getTitle("tt2169080")));
        neo4j.addTitle(new OmdbEpisode(Omdb.getTitle("tt3333824")));
        neo4j.addTitle(new OmdbEpisode(Omdb.getTitle("tt3333828")));
        neo4j.addTitle(new OmdbEpisode(Omdb.getTitle("tt3333830")));
        neo4j.addTitle(new OmdbEpisode(Omdb.getTitle("tt3333832")));

        StatementResult result = neo4j.getSession().run("MATCH (n:Series) " +
                "RETURN n.name AS name, n.title AS title");

        while (result.hasNext()) {
            Record record = result.next();

            assertEquals("Rick and Morty", record.get("title").asString());
            assertEquals("tt2861424", record.get("name").asString());
        }
    }

    /**
     * Test adding list
     */
    @Test
    public void addListTest() {
        neo4j.addList("Lord of The Rings Saga", new OmdbMovie(Omdb.getTitle("tt0120737")),
                new OmdbMovie(Omdb.getTitle("tt0167261")));
        neo4j.addList("Lord of The Rings Saga", new OmdbMovie(Omdb.getTitle("tt0167260")),
                new OmdbMovie(Omdb.getTitle("tt0167261")));

        StatementResult result = neo4j.getSession().run("MATCH p=(n:List)-[r:CONTAINS]-(m) " +
                "WHERE m.name='tt0167260' RETURN n.name AS name");

        while (result.hasNext()) {
            Record record = result.next();

            assertEquals("Lord of The Rings Saga", record.get("name").asString());
        }
    }

    /**
     * Test manual removal
     */
    @Test
    public void removeTest() {

        /* Star Wars Movies */
        neo4j.addList("Star Wars Saga", new OmdbMovie(Omdb.getTitle("tt0120915")),
                new OmdbMovie(Omdb.getTitle("tt0121765")), new OmdbMovie(Omdb.getTitle("tt2488496")),
                new OmdbMovie(Omdb.getTitle("tt0076759")), new OmdbMovie(Omdb.getTitle("tt0080684")),
                new OmdbMovie(Omdb.getTitle("tt0086190")), new OmdbMovie(Omdb.getTitle("tt0121766")));

        neo4j.removeTitle("tt0120915", "Movie");
        neo4j.removeTitle("tt0121765", "Movie");
        neo4j.removeTitle("tt2488496", "Movie");
        neo4j.removeTitle("tt0076759", "Movie");
        neo4j.removeTitle("tt0080684", "Movie");
        neo4j.removeTitle("tt0086190", "Movie");
        neo4j.removeTitle("tt0121766", "Movie");

        StatementResult result = neo4j.getSession().run("MATCH p=(n:List)-[r:CONTAINS]-(m) " +
                "WHERE m.name='tt0120915' RETURN n.name AS name");

        while (result.hasNext()) {
            Record record = result.next();

            assertNotEquals("Star Wars Saga", record.get("name").asString());
        }
    }

    /**
     * Test rename
     */
    @Test
    public void renameTest() {

        /* Star Wars Movies */
        neo4j.addList("Star Wars Saga", new OmdbMovie(Omdb.getTitle("tt0120915")),
                new OmdbMovie(Omdb.getTitle("tt0121765")), new OmdbMovie(Omdb.getTitle("tt2488496")),
                new OmdbMovie(Omdb.getTitle("tt0076759")), new OmdbMovie(Omdb.getTitle("tt0080684")),
                new OmdbMovie(Omdb.getTitle("tt0086190")), new OmdbMovie(Omdb.getTitle("tt0121766")));

        /* Fixes duplicate because of different name */
        neo4j.renameNode("Twentieth Century Fox", "20th Century Fox", "Producer");
    }
}