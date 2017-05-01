package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Java Implementation of the OMDB api
 * <p>
 * http://omdbapi.com/
 */
public class Omdb {

    private static String url = "";

    /**
     * Searches a title in IMDB, can differentiate between media types.
     *
     * @param title - Title to search for
     * @param type  - Type of media to search for
     * @return a HashMap where the key is the IMDBid
     * and the value is another HashMap with basic information about the title
     */
    public static HashMap search(String title, MediaType type) {
        try {

            HashMap search_results = new HashMap<String, HashMap>();

            if (!type.equals(MediaType.ALL))
                url = "http://www.omdbapi.com/?s=" + title.replace(" ", "%20") + "&type=" + type.name();
            else
                url = "http://www.omdbapi.com/?s=" + title.replace(" ", "%20");

            URL query = new URL(url);

            Scanner s = new Scanner(query.openStream());
            JSONObject response = new JSONObject(s.nextLine());

            JSONArray results = response.getJSONArray("Search");

            for (Object result : results) {
                if (!result.equals("{}")) {

                    JSONObject entry = new JSONObject(result.toString());

                    HashMap entry_info = new HashMap<String, String>();
                    entry_info.put("Year", entry.get("Year"));
                    entry_info.put("Title", entry.get("Title"));
                    entry_info.put("Type", entry.get("Type"));

                    search_results.put(entry.get("imdbID"), entry_info);
                }
            }

            return search_results;

        } catch (MalformedURLException e) {
            System.err.println("Malformed URL: " + url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets info from a IMDB Title and puts it into a Map
     *
     * @param id - IMDB Title to search for
     * @return a Map where the keys are the names of the values
     */
    public static Map getTitle(String id) {

        try {

            url = "http://www.omdbapi.com/?i=" + id + "&plot=full";

            URL query = new URL(url);

            Scanner s = new Scanner(query.openStream());
            JSONObject title = new JSONObject(s.nextLine());

            return title.toMap();

        } catch (MalformedURLException e) {
            System.err.println("Malformed URL: " + url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets the type from a IMDB Title
     *
     * @param id - IMDB Title to search for
     * @return - Type
     */
    public static MediaType getType(String id) {

        try {

            url = "http://www.omdbapi.com/?i=" + id;

            URL query = new URL(url);

            Scanner s = new Scanner(query.openStream());
            JSONObject title = new JSONObject(s.nextLine());

            String type = (String) title.get("Type");

            if (MediaType.MOVIE.equalsName(type)) {
                return MediaType.MOVIE;
            } else if (MediaType.SERIES.equalsName(type)) {
                return MediaType.SERIES;
            } else if (MediaType.EPISODE.equalsName(type)) {
                return MediaType.EPISODE;
            }
        } catch (MalformedURLException e) {
            System.err.println("Malformed URL: " + url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

