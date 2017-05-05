package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

import java.util.ArrayList;
import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;

public class OmdbEpisode extends OmdbTitle {

    private String seriesID;
    private int season;
    private int episode;

    private ArrayList actors;
    private ArrayList writer;
    private ArrayList director;

    /**
     * Constructor for the class OMDBMovie that extends from OMDBTitle
     *
     * @param id - IMDB id of the Movie
     */
    public OmdbEpisode(String id) {

        super(Omdb.getTitle(id));
        Map series = Omdb.getTitle(id);

        this.seriesID = (String) series.get("seriesID");
        this.season = JSONFormatter.intergerConversor(series.get("Season"));
        this.episode = JSONFormatter.intergerConversor(series.get("Episode"));

        this.actors = JSONFormatter.listFormatter(series.get("Actors"));
        this.writer = JSONFormatter.listFormatter(series.get("Writer"));
        this.director = JSONFormatter.listFormatter(series.get("Director"));
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
        return MediaType.EPISODE;
    }

    public ArrayList getActors() {
        return actors;
    }

    public ArrayList getWriter() {
        return writer;
    }

    public ArrayList getDirector() {
        return director;
    }

    public String getSeriesID() {
        return seriesID;
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }
    /* END Getters */
}
