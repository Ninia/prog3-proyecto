package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

import java.util.ArrayList;
import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;

public class OmdbSeries extends OmdbTitle {

    private int seasons;

    private ArrayList language;
    private ArrayList genre;
    private ArrayList producers = new ArrayList();
    private ArrayList country;

    /**
     * Constructor for the class OMDBMovie that extends from OMDBTitle
     *
     * @param id - IMDB id of the Movie
     */
    public OmdbSeries(String id) {

        super(Omdb.getTitle(id));
        Map series = Omdb.getTitle(id);

        this.seasons = JSONFormatter.intergerConversor(series.get("totalSeasons"));
        this.language = JSONFormatter.listFormatter(series.get("Language"));
        this.genre = JSONFormatter.listFormatter(series.get("Genre"));
        this.producers.add("Placeholder"); /* TODO Placeholder */
        this.country = JSONFormatter.listFormatter(series.get("Country"));
    }

    /**
     * @return Return information in org.neo4j.driver.v1.Values.parameters format
     */
    public Object toParameters() {
        return parameters(
                "title", title,
                "name", imdbID,
                "year", year,
                "seasons", seasons,
                "released", released.toString(),
                "plot", plot,
                "awards", awards,
                "metascore", metascore,
                "imdbRating", imdbRating,
                "imdbVotes", imdbVotes,
                "runtime", runtime,
                "poster", poster);
    }

    /* Getters */

    public Enum getType() {
        return MediaType.SERIES;
    }

    public ArrayList getLanguage() {
        return language;
    }

    public ArrayList getGenre() {
        return genre;
    }

    public ArrayList getProducers() {
        return producers;
    }

    public ArrayList getCountry() {
        return country;
    }
    /* END Getters */
}