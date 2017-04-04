package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;

public class OmdbMovie extends OmdbTitle {

    private Date dvd;
    private double boxOffice; // in Dollars
    private String website;

    private HashMap ratings = new HashMap<String, String>();
    private ArrayList language;
    private ArrayList genre;
    private ArrayList writer;
    private ArrayList director;
    private ArrayList actors;
    private ArrayList producers;

    /**
     * Constructor for the class OMDBMovie that extends from OMDBTitle
     *
     * @param id - IMDB id of the Movie
     */
    public OmdbMovie(String id) {

        super(Omdb.getTitle(id));
        Map movie = Omdb.getTitle(id);

        this.dvd = Omdb.dateFormatter(movie.get("DVD"));
        this.boxOffice = Omdb.doubleConversor(movie.get("BoxOffice"));
        this.website = (String) movie.get("Website");

        for (Object rating : (ArrayList) movie.get("Ratings")) {
            HashMap a = (HashMap) rating;
            ratings.put(a.get("Source"), a.get("Value"));
        }

        this.language = Omdb.listFormatter(movie.get("Language"));
        this.genre = Omdb.listFormatter(movie.get("Genre"));
        this.writer = Omdb.listFormatter(movie.get("Writer"));
        this.director = Omdb.listFormatter(movie.get("Director"));
        this.actors = Omdb.listFormatter(movie.get("Actors"));
        this.producers = Omdb.listFormatter(movie.get("Production"));
    }

    /**
     * @return Return information in org.neo4j.driver.v1.Values.parameters format
     */
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

    ;

    /* Overridden Methods */
    @Override
    public String toString() {
        return "OmdbMovie:\n" +
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
                "\tPoster=" + poster + "\n" +
                "\tRatings=" + ratings + "\n" +
                "\tLanguage=" + language + "\n" +
                "\tGenre=" + genre + "\n" +
                "\tWriter=" + writer + "\n" +
                "\tDirector=" + director + "\n" +
                "\tActors=" + actors + "\n";
    }
}
