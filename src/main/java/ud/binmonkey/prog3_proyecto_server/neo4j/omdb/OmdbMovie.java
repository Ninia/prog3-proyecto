package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;

public class OmdbMovie extends OmdbTitle {

    private Date dvd;
    private double boxOffice; /* in Dollars */
    private String website;

    private HashMap ratings = new HashMap<String, Integer>();
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

        this.ratings = JSONFormatter.scoreFormatter((ArrayList) movie.get("Ratings"));

        this.language = JSONFormatter.listFormatter(movie.get("Language"));
        this.genre = JSONFormatter.listFormatter(movie.get("Genre"));
        this.writer = JSONFormatter.listFormatter(movie.get("Writer"));
        this.director = JSONFormatter.listFormatter(movie.get("Director"));
        this.actors = JSONFormatter.listFormatter(movie.get("Actors"));
        this.producers = JSONFormatter.listFormatter(movie.get("Production"));
        this.country = JSONFormatter.listFormatter(movie.get("Country"));
    }

    /* Methods */
    public Object toParameters() {
        return parameters(
                "title", title,
                "name", imdbID,
                "year", year,
                "released", released.toString(),
                "dvd", dvd.toString(),
                "plot", plot,
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

    public Enum getType() {
        return MediaType.MOVIE;
    }

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
}
