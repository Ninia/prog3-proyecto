package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;

public class OmdbMovie extends OmdbTitle {

    private Date dvd;
    private double boxOffice; /* in Dollars */
    private String website;

    private HashMap ratings = new HashMap<String, String>();
    private ArrayList language;
    private ArrayList genre;
    private ArrayList writer;
    private ArrayList director;
    private ArrayList actors;
    private ArrayList producers;
    private ArrayList country;

    /**
     * Constructor for the class OMDBMovie that extends from OMDBTitle
     *
     * @param id - IMDB id of the Movie
     */
    public OmdbMovie(String id) {

        super(Omdb.getTitle(id));
        Map movie = Omdb.getTitle(id);

        this.dvd = JSONFormatter.dateFormatter(movie.get("DVD"));
        this.boxOffice = JSONFormatter.doubleConversor(movie.get("BoxOffice"));
        this.website = (String) movie.get("Website");

        for (Object rating : (ArrayList) movie.get("Ratings")) {
            HashMap a = (HashMap) rating;
            ratings.put(a.get("Source"), a.get("Value"));
        }

        this.language = JSONFormatter.listFormatter(movie.get("Language"));
        this.genre = JSONFormatter.listFormatter(movie.get("Genre"));
        this.writer = JSONFormatter.listFormatter(movie.get("Writer"));
        this.director = JSONFormatter.listFormatter(movie.get("Director"));
        this.actors = JSONFormatter.listFormatter(movie.get("Actors"));
        this.producers = JSONFormatter.listFormatter(movie.get("Production"));
        this.country = JSONFormatter.listFormatter(movie.get("Country"));
    }

    public static void main(String[] args) {
        OmdbMovie omdbMovie = new OmdbMovie("tt0117951");
        System.out.println(omdbMovie.toJSON());
    }

    /* Methods */
    public JSONObject toJSON() {

        JSONObject episodeJSON = super.toJSON();

        episodeJSON.put("dvd", dvd.toString());
        episodeJSON.put("boxOffice", boxOffice);
        episodeJSON.put("website", website);

        episodeJSON.put("ratings", ratings);
        episodeJSON.put("language", language);
        episodeJSON.put("genre", genre);
        episodeJSON.put("writer", writer);
        episodeJSON.put("director", director);
        episodeJSON.put("actors", actors);
        episodeJSON.put("producers", producers);
        episodeJSON.put("country", country);

        return episodeJSON;
    }

    public Object toParameters() {
        return parameters(
                "title", title,
                "name", imdbID,
                "year", year,
                "released", released.toString(),
                "dvd", dvd.toString(),
                "plot", plot,
                "rated", rated,
                "awards", awards,
                "boxOffice", boxOffice,
                "metascore", metascore,
                "imdbRating", imdbRating,
                "imdbVotes", imdbVotes,
                "runtime", runtime,
                "website", website,
                "poster", poster);
    }
    /* END Methods */

    /* Getters */
    public HashMap getRatings() {
        return ratings;
    }

    public ArrayList getLanguage() {
        return language;
    }

    public ArrayList getGenre() {
        return genre;
    }

    public ArrayList getWriter() {
        return writer;
    }

    public ArrayList getDirector() {
        return director;
    }

    public ArrayList getActors() {
        return actors;
    }

    public ArrayList getProducers() {
        return producers;
    }

    public ArrayList getCountry() {
        return country;
    }
    /* END Getters*/

    /* Overridden Methods */
    @Override
    /* TODO cleanup */
    public String toString() {
        System.out.println("OmdbMovie:\n" +
                "\tTitle=" + title + "\n" +
                "\tIMDB ID=" + imdbID + "\n" +
                "\tYear=" + year + "\n" +
                "\tReleased=" + released + "\n" +
                "\tDVD=" + dvd + "\n" +
                "\tBoxOffice=" + boxOffice + "\n" +
                "\tPlot=" + plot + "\n" +
                "\tRated=" + rated + "\n" +
                "\tAward=" + awards + "\n" +
                "\tMetascore=" + metascore + "\n" +
                "\tIMDB Rating=" + imdbRating + "\n" +
                "\tIMDB Votes=" + imdbVotes + "\n" +
                "\tRuntime=" + runtime + "\n" +
                "\tWebsite=" + website + "\n" +
                "\tPoster=" + poster);
                /* Movie Specific Values */
        System.out.println("\tRatings:");
        for (Object outlet : ratings.keySet()) {
            System.out.println("\t\t" + outlet + " " + ratings.get(outlet));
        }

        System.out.println("\tLanguages:");
        for (Object entry : language) {
            System.out.println("\t\t" + entry);
        }

        System.out.println("\tGenres:");
        for (Object entry : genre) {
            System.out.println("\t\t" + entry);
        }

        System.out.println("\tWriters:");
        for (Object entry : writer) {
            System.out.println("\t\t" + entry);
        }

        System.out.println("\tDirectors:");
        for (Object entry : director) {
            System.out.println("\t\t" + entry);
        }

        System.out.println("\tActors:");
        for (Object entry : actors) {
            System.out.println("\t\t" + entry);
        }

        return "";
    }
    /* END Overridden Methods */
}
