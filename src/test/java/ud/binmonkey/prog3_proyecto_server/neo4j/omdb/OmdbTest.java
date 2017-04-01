package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class OmdbTest {

    /**
     * Test if the response for the search Trainspotting contains 1996 Trainspotting
     */
    @org.junit.Test
    public void testSearch() {

        HashMap search = Omdb.search("Trainspotting", MediaType.all);
        HashMap movie = (HashMap) search.get("tt0117951");

        assertEquals(true, movie.get("Type").equals("movie"));
        assertEquals(true, movie.get("Year").equals("1996"));
        assertEquals(true, movie.get("Title").equals("Trainspotting"));
    }

    /**
     * Test if the response for the search Trainspotting's id contains 1996 Trainspotting
     */
    @org.junit.Test
    public void testTitle() {

        Map movie = Omdb.getTitle("tt0117951");

        /* Checks that the movie is the same as the one searched for */
        assertEquals(true, movie.get("Type").equals("movie"));
        assertEquals(true, movie.get("Title").equals("Trainspotting"));
        assertEquals(true, movie.get("Year").equals("1996"));

        /* Checks info about the movie */
        ArrayList actors = (ArrayList) movie.get("Actors");
        assertEquals(true, actors.contains("Ewan McGregor"));
        assertEquals(true, movie.get("Country").equals("UK"));
    }
}