package ud.binmonkey.prog3_proyecto_server.omdb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ud.binmonkey.prog3_proyecto_server.common.DocumentReader;
import ud.binmonkey.prog3_proyecto_server.common.time.DateUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;

/**
 * Java Implementation of the OMDB api
 * <p>
 * http://omdbapi.com/
 */
public class Omdb {

    /* Logger for Omdb */
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Omdb.class.getName());
    private static final String keyFile = "conf/keys.xml";
    /* END Logger for Omdb */
    private static final String KEY = DocumentReader.getAttr(DocumentReader.getDoc(keyFile),
            "omdb").getTextContent().replaceAll("\n", "").replaceAll(" ", "");
    private static final String apikey = "&apikey=" + KEY;
    private static String url = "";

    static {
        try {
            LOG.addHandler(new FileHandler(
                    "logs/" + Omdb.class.getName() + "." +
                            DateUtils.currentFormattedDate() + ".log.xml", true));
        } catch (SecurityException | IOException e) {
            LOG.log(Level.SEVERE, "Unable to create log file.");
        }
    }

    /**
     * Searches a title in IMDB, can differentiate between media types.
     *
     * @param title - Title to search for
     * @param type  - Type of media to search for
     * @return a JSON where the key is the IMDBi and the value is another JSON with
     * basic information about the title
     */
    public static JSONObject search(String title, MediaType type) {
        try {

            JSONObject search_results = new JSONObject();

            if (!type.equals(MediaType.ALL))
                url = "http://www.omdbapi.com/?s=" + title.replace(" ", "%20") +
                        "&type=" + type.name() + apikey;
            else
                url = "http://www.omdbapi.com/?s=" + title.replace(" ", "%20") + apikey;

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

                    if (!entry_info.get("Type").equals("game")) { /* Recent update of OMDB also supports games */
                        search_results.put(entry.get("imdbID").toString(), entry_info);
                    }
                }
            }

            LOG.log(Level.INFO, "Searched for " + title);

            return search_results;

        } catch (JSONException e) {
            LOG.log(Level.SEVERE, "Malformed or empty JSON");
        } catch (MalformedURLException e) {
            LOG.log(Level.SEVERE, "Malformed URL - " + url);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "IOException - " + e.getMessage());
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

            url = "http://www.omdbapi.com/?i=" + id + "&plot=full" + apikey;

            URL query = new URL(url);

            Scanner s = new Scanner(query.openStream());
            JSONObject title = new JSONObject(s.nextLine());

            LOG.log(Level.INFO, "Searched info for " + id);
            return title.toMap();

        } catch (MalformedURLException e) {
            LOG.log(Level.SEVERE, "Malformed URL - " + url);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "IOException - " + e.getMessage());
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

            url = "http://www.omdbapi.com/?i=" + id + apikey;

            URL query = new URL(url);

            Scanner s = new Scanner(query.openStream());
            JSONObject title = new JSONObject(s.nextLine());

            String type = (String) title.get("Type");
            LOG.log(Level.INFO, "Searched type of " + id);

            if (MediaType.MOVIE.equalsName(type)) {
                return MediaType.MOVIE;
            } else if (MediaType.SERIES.equalsName(type)) {
                return MediaType.SERIES;
            } else if (MediaType.EPISODE.equalsName(type)) {
                return MediaType.EPISODE;
            }

        } catch (MalformedURLException e) {
            LOG.log(Level.SEVERE, "Malformed URL - " + url);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "IOException - " + e.getMessage());
        }

        return null;
    }
}