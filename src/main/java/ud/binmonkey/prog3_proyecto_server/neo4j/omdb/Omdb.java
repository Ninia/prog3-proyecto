package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @SuppressWarnings({"SameParameterValue", "unchecked"})
    static HashMap search(String title, MediaType type) {
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
     * Formats lists in String received from OMDB to a usable ArrayList
     *
     * @param list - string received from OMDB
     * @return formatted Arraylist
     */
    @SuppressWarnings("unchecked")
    static ArrayList listFormatter(Object list) {
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

    /**
     * Formats date in String received from OMDB to a usable Date
     *
     * @param date - String received from OMDB
     * @return formatted Date
     */
    static Date dateFormatter(Object date) {
        if (!(date == null)) {
            {
                try {
                    DateFormat formatter;
                    formatter = new SimpleDateFormat("dd MMM yy");
                    return formatter.parse(date.toString());
                } catch (ParseException e) {
                    System.err.println("Error - Invalid date: " + date);
                }
            }
        }
        return null;
    }

    /**
     * Fixes encoding problem with some years received from OMDB
     *
     * @param year - year received from OMDB
     * @return formatted year
     */

    static String yearFormatter(Object year) {
        return year.toString().replaceAll("â\u0080\u0093", "-"); /* Fixes encoding problem */
    }

    /**
     * Formats integer in String received from OMDB to a usable int
     *
     * @param string - String received from OMDB
     * @return formatted int
     */
    static int intergerConversor(Object string) {

        if (!string.equals("N/A")) {
            String str_int = string.toString();
            str_int = str_int.replaceAll("[^0-9]", ""); /* Removes non integers*/
            return Integer.parseInt(str_int);
        }

        System.err.println("Error - Missing Info"); //TODO Ask for input
        return 0;

    }

    /**
     * Formats double in String received from OMDB to a usable double
     *
     * @param string - String received from OMDB
     * @return formatted int
     */
    static double doubleConversor(Object string) {

        if (!string.equals("N/A")) {
            String str_double = string.toString();
            str_double = str_double.replaceAll("\\$", "");
            str_double = str_double.replaceAll(",", "");
            return Double.parseDouble(str_double);
        }

        System.err.println("Error - Missing Info"); //TODO Ask for input
        return 0;
    }
}

