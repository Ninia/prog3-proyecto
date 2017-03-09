package ud.main.neo4j.omdb;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Omdb {

    private static String url = "";

    public static String search(String film, MediaType type) {
        try {
            url = "http://www.omdbapi.com/?s=" + film.replace(" ", "%20") + "&type=" + type.name();
            URL query = new URL(url);

            Scanner s = new Scanner(query.openStream());

            return s.nextLine();

        } catch (MalformedURLException e) {
            System.err.println("Malformed URL: " + url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(Omdb.search("Trainspotting", MediaType.movie));
    }
}
