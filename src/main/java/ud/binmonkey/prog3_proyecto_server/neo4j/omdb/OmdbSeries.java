package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

import java.util.ArrayList;
import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;

public class OmdbSeries extends OmdbTitle {

    private int seasons;

    private ArrayList language;
    private ArrayList genre;
    private ArrayList producers;
    private ArrayList country;

    /**
     * Constructor for the class OMDBMovie that extends from OMDBTitle
     *
     * @param id - IMDB id of the Movie
     */
    public OmdbSeries(String id) {

        super(Omdb.getTitle(id));
        Map series = Omdb.getTitle(id);

        this.seasons = Omdb.intergerConversor(series.get("totalSeasons"));
        this.language = Omdb.listFormatter(series.get("Language"));
        this.genre = Omdb.listFormatter(series.get("Genre"));
        this.producers = new ArrayList();
        producers.add("Placeholder");
        this.country = Omdb.listFormatter(series.get("Country"));
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
                "rated", rated,
                "awards", awards,
                "metascore", metascore,
                "imdbRating", imdbRating,
                "imdbVotes", imdbVotes,
                "runtime", runtime,
                "poster", poster);
    }

    /* Getters */

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

    /* Overridden Methods */
    @Override
    public String toString() {
        return "OmdbMovie:\n" +
                "\tTitle=" + title + "\n" +
                "\tIMDB ID=" + imdbID + "\n" +
                "\tYear=" + year + "\n" +
                "\tReleased=" + released + "\n" +
                "\tPlot=" + plot + "\n" +
                "\tRated=" + rated + "\n" +
                "\tAward=" + awards + "\n" +
                "\tMetascore=" + metascore + "\n" +
                "\tIMDB Rating=" + imdbRating + "\n" +
                "\tIMDB Votes=" + imdbVotes + "\n" +
                "\tRuntime=" + runtime + "\n" +
                "\tPoster=" + poster + "\n" +
                "\tLanguage=" + language + "\n" +
                "\tGenre=" + genre + "\n";
    }
}