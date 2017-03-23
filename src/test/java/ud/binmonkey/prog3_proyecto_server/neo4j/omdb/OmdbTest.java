package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

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