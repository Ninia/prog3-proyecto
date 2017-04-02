package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

            if (!type.equals(MediaType.all))
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
     * Gets info from a IMDB Title and puts it into a Map making minor modifications
     *
     * @param id - IMDB Title to search for
     * @return a HashMap where the key are the names of the values
     */
    public static Map getTitle(String id) {

        try {

            url = "http://www.omdbapi.com/?i=" + id + "&plot=full";

            URL query = new URL(url);

            Scanner s = new Scanner(query.openStream());
            JSONObject title = new JSONObject(s.nextLine());
            Map<String, Object> title_info = title.toMap();

            /* Movie specific modifications */
            if (MediaType.movie.equalsName((String) title_info.get("Type"))) {
                /* Ratings */
                HashMap ratings = new HashMap<String, String>();
                for (Object rating : (ArrayList) title_info.get("Ratings")) {
                    HashMap a = (HashMap) rating;
                    ratings.put(a.get("Source"), a.get("Value"));
                }
                title_info.replace("Ratings", ratings);

                /* DVD Date */
                title_info.replace("DVD", dateFormatter(title_info.get("DVD")));
            }

            /* TV series specific modifications */
            if (MediaType.series.equalsName((String) title_info.get("Type"))) {
                /* Year */
                title_info.replace("Year", yearFormatter(title_info.get("Year")));
            }

            /* Release Date */
            title_info.replace("Released", dateFormatter(title_info.get("Released")));
            /* Language */
            title_info.replace("Language", listFormatter(title_info.get("Language")));
            /* Genres */
            title_info.replace("Genre", listFormatter(title_info.get("Genre")));
            /* Writers */
            title_info.replace("Writer", listFormatter(title_info.get("Writer")));
            /* Director - seems unnecessary but there are some movies with more than 1 directors e.g. Matrix */
            title_info.replace("Director", listFormatter(title_info.get("Director")));
            /* Actors */
            title_info.replace("Actors", listFormatter(title_info.get("Actors")));

            return title_info;

        } catch (MalformedURLException e) {
            System.err.println("Malformed URL: " + url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static ArrayList listFormatter(Object list) {
        ArrayList formattedList = new ArrayList<String>();
        for (String entry : list.toString().split(",")) {

            entry = entry.replaceAll("\\(.*?\\)", ""); /* removes characters between brackets */
            entry = entry.replaceAll("\\s+$", ""); /* removes whitespace at the beginning of the string */
            entry = entry.replaceAll("^\\s+", ""); /* removes whitespace at the end of the string */
            if (!formattedList.contains(entry))
                formattedList.add(entry);
        }

        return formattedList;
    }

    private static Object dateFormatter(Object date) {


        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat("dd MMM yy");
            return formatter.parse(date.toString());
        } catch (ParseException e) {
            System.err.println("Error - Invalid date: " + date);
        }

        return date;
    }


    private static String yearFormatter(Object year) {

        return year.toString().replace("â\u0080\u0093", "-"); /* Fixes encoding problem */
    }
    
}

