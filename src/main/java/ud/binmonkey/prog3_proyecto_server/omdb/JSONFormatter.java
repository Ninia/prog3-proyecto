package ud.binmonkey.prog3_proyecto_server.omdb;

import ud.binmonkey.prog3_proyecto_server.common.time.DateUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;

public class JSONFormatter {

    /* Logger for JSONFormatter */
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(JSONFormatter.class.getName());

    static {
        try {
            LOG.addHandler(new FileHandler(
                    "logs/" + JSONFormatter.class.getName() + "." +
                            DateUtils.currentFormattedDate() + ".log.xml", true));
        } catch (SecurityException | IOException e) {
            LOG.log(Level.SEVERE, "Unable to create log file.");
        }
    }
    /* END Logger for JSONFormatter */

    /**
     * Formats lists in String received from OMDB to a usable ArrayList
     *
     * @param list - string received from OMDB
     * @return formatted Arraylist
     */
    protected static ArrayList listFormatter(Object list) {
        ArrayList formattedList = new ArrayList<String>();
        for (String entry : list.toString().split(",")) {

            entry = entry.replaceAll("\\[", "");
            entry = entry.replaceAll("]", "");
            entry = entry.replaceAll("\\(.*?\\)", ""); /* removes characters between brackets */
            entry = entry.replaceAll("\\s+$", ""); /* removes whitespace at the beginning of the string */
            entry = entry.replaceAll("^\\s+", ""); /* removes whitespace at the end of the string */
            if (!formattedList.contains(entry))
                formattedList.add(entry);
        }

        return formattedList;
    }

    /**
     * Formats Scores in Arraylist received from OMDB to a Hashmap where the key is the outlet, standardizing the scores
     *
     * @param ratings - Arraylist of Ratings
     * @return formatted Hashmap
     */
    protected static HashMap scoreFormatter(ArrayList ratings) {

        HashMap formatted_ratings = new HashMap<String, Integer>();
        int formatted_value;

        for (Object rating : ratings) {
            HashMap a = (HashMap) rating;

            String source = (String) a.get("Source");
            String value = (String) a.get("Value");

            switch (source) {
                case "Internet Movie Database":
                    value = value.replace("/10", "");
                    value = value.replace(".", "");
                    formatted_value = Integer.parseInt(value);
                    break;
                case "Metacritic":
                    value = value.replace("/100", "");
                    formatted_value = Integer.parseInt(value);
                    break;
                default:  /* Rotten Tomatoes */
                    value = value.replace("%", "");
                    formatted_value = Integer.parseInt(value);
                    break;
            }

            formatted_ratings.put(source, formatted_value);
        }

        return formatted_ratings;
    }

    /**
     * Formats date in String received from OMDB to a usable Date
     *
     * @param date - String received from OMDB
     * @return formatted Date
     */
    protected static Date dateFormatter(Object date) {
        if (!(date == null)) {
            {
                try {
                    DateFormat formatter;
                    formatter = new SimpleDateFormat("dd MMM yy");
                    return formatter.parse(date.toString());
                } catch (ParseException e) {
                    LOG.log(Level.SEVERE, "Invalid date: " + date);
                    /* TODO */
                }
            }
        }
        return new Date(); /* TODO */
    }

    /**
     * Fixes encoding problem with some years received from OMDB
     *
     * @param year - year received from OMDB
     * @return formatted year
     */

    protected static String yearFormatter(Object year) {
        return year.toString().replaceAll("â\u0080\u0093", "-"); /* Fixes encoding problem */
    }

    /**
     * Formats integer in String received from OMDB to a usable int
     *
     * @param string - String received from OMDB
     * @return formatted int
     */
    protected static int intergerConversor(Object string) {

        if (!string.equals("N/A")) {
            String str_int = string.toString();
            str_int = str_int.replaceAll("[^0-9]", ""); /* Removes non integers*/
            return Integer.parseInt(str_int);
        }

        LOG.log(Level.SEVERE, "Missing Info"); //TODO Ask for input
        return 0;

    }

    /**
     * Formats double in String received from OMDB to a usable double
     *
     * @param string - String received from OMDB
     * @return formatted int
     */
    protected static double doubleConversor(Object string) {

        if (!string.equals("N/A")) {
            String str_double = string.toString();
            str_double = str_double.replaceAll("\\$", "");
            str_double = str_double.replaceAll(",", "");
            return Double.parseDouble(str_double);
        }

        LOG.log(Level.SEVERE, "Missing Info"); //TODO Ask for input
        return 0;
    }
    /* END Formatter Methods */
}
