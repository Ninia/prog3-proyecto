package ud.binmonkey.prog3_proyecto_server.omdb;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OmdbTitleUnitTest {


    /**
     * Tests creating OmdbTitle from the API
     */
    @org.junit.Test
    public void testFromAPI() {

        OmdbMovie movie = new OmdbMovie(Omdb.getTitle("tt0117951")); /* Trainspotting */
        OmdbSeries series = new OmdbSeries(Omdb.getTitle("tt2802850")); /* Fargo */
        OmdbEpisode episode = new OmdbEpisode(Omdb.getTitle("tt2169080")); /* Rick and Morty S01E01 */

        assertEquals("tt0117951", movie.getImdbID());
        assertTrue(movie.getActors().contains("Ewan McGregor"));

        assertEquals("tt2802850", series.getImdbID());
        assertTrue(series.getGenre().contains("Drama"));

        assertEquals("tt2169080", episode.getImdbID());
        assertTrue(episode.getWriter().contains("Justin Roiland"));
    }

    /**
     * Tests creating OmdbTitle from JSON
     */
    @org.junit.Test
    public void testFromJSON() {
        try {

            JSONObject jsonMovie = new JSONObject(new Scanner(new File(
                    "src/test/resources/neo4j/omdb/movie.json")).useDelimiter("\\Z").next());
            JSONObject jsonSeries = new JSONObject(new Scanner(new File(
                    "src/test/resources/neo4j/omdb/series.json")).useDelimiter("\\Z").next());
            JSONObject jsonEpisode = new JSONObject(new Scanner(new File(
                    "src/test/resources/neo4j/omdb/episode.json")).useDelimiter("\\Z").next());


            OmdbMovie movie = new OmdbMovie(jsonMovie.toMap());
            OmdbSeries series = new OmdbSeries(jsonSeries.toMap());
            OmdbEpisode episode = new OmdbEpisode(jsonEpisode.toMap());

            assertEquals("tt0117951", movie.getImdbID());
            assertTrue(movie.getActors().contains("Ewan McGregor"));

            assertEquals("tt2802850", series.getImdbID());
            assertTrue(series.getGenre().contains("Drama"));

            assertEquals("tt2169080", episode.getImdbID());
            assertTrue(episode.getWriter().contains("Justin Roiland"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
