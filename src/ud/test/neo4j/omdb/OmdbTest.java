package ud.test.neo4j.omdb;

import ud.main.neo4j.omdb.MediaType;
import ud.main.neo4j.omdb.Omdb;

import static org.junit.Assert.assertEquals;

public class OmdbTest {

    /**
     * Test if the response for the search Trainspotting contains Trainspotting's id
     */
    @org.junit.Test
    public void testSearch() {
        assertEquals(true ,Omdb.search("Trainspotting", MediaType.movie).contains("tt0117951"));
    }
}