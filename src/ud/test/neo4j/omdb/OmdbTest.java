package ud.test.neo4j.omdb;

import ud.main.neo4j.omdb.MediaType;
import ud.main.neo4j.omdb.Omdb;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class OmdbTest {

    /**
     * Test if the response for the search Trainspotting contains 1996 Trainspotting's id
     */
    @org.junit.Test
    public void testSearch() {

        HashMap search = Omdb.search("Trainspotting", MediaType.all);
        HashMap movie = (HashMap) search.get("tt0117951");

        assertEquals(true, movie.get("Type").equals("movie"));
        assertEquals(true, movie.get("Year").equals("1996"));
        assertEquals(true, movie.get("Title").equals("Trainspotting"));
    }
}