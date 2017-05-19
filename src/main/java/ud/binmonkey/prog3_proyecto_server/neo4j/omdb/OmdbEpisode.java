package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

import org.json.JSONObject;

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
     * Constructor for the class OmdbSeries that extends from OmdbBTitle
     *
     * @param episode - Map with the info of the Episode
     */
    public OmdbEpisode(Map episode) {

        super(episode);

        this.seriesID = (String) episode.get("seriesID");
        this.season = JSONFormatter.intergerConversor(episode.get("Season"));
        this.episode = JSONFormatter.intergerConversor(episode.get("Episode"));
        this.actors = JSONFormatter.listFormatter(episode.get("Actors"));
        this.writer = JSONFormatter.listFormatter(episode.get("Writer"));
        this.director = JSONFormatter.listFormatter(episode.get("Director"));
    }

    /* Format Conversion Methods */

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

    /**
     * @return Return information in JSON format
     */
    public JSONObject toJSON() {

        JSONObject episodeJSON = super.toJSON();

        episodeJSON.put("seriesID", seriesID);
        episodeJSON.put("Season", season);
        episodeJSON.put("Episode", episode);

        episodeJSON.put("Writer", writer);
        episodeJSON.put("Director", director);
        episodeJSON.put("Actors", actors);

        return episodeJSON;
    }
    /* END Format Conversion Methods */

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
