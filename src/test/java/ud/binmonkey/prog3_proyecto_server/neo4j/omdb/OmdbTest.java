package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class OmdbTest {

    /**
     * Test if the response for the search Trainspotting contains 1996 Trainspotting
     */
    @org.junit.Test
    public void testSearch() {

        HashMap search = Omdb.search("Trainspotting", MediaType.ALL);
        HashMap movie = (HashMap) search.get("tt0117951");

        assertEquals("movie", movie.get("Type"));
        assertEquals("1996", movie.get("Year"));
        assertEquals("Trainspotting", movie.get("Title"));
    }

    /**
     * Test if the response for the search Trainspotting's id contains 1996 Trainspotting
     */
    @org.junit.Test
    public void testTitle() {

        Map movie = Omdb.getTitle("tt0117951");

        /* Checks that the movie is the same as the one searched for */
        assertEquals("movie", movie.get("Type"));
        assertEquals("1996", movie.get("Year"));
        assertEquals("Trainspotting", movie.get("Title"));

        /* Checks info about the movie */
        String actors = (String) movie.get("Actors");
        assertEquals(true, actors.contains("Ewan McGregor"));
        assertEquals("UK", movie.get("Country"));
    }
}