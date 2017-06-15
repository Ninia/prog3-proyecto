package ud.binmonkey.prog3_proyecto_server.omdb;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;

public class OmdbEpisode extends OmdbTitle {

    private String seriesID;
    private int season;
    private int episode;
    private String language;

    private ArrayList actors;
    private ArrayList writer;
    private ArrayList director;

    /**
     * Constructor for the class OmdbSeries that extends from OmdbBTitle
     *
     * @param episodeJSON - JSON with the info of the Episode
     */
    public OmdbEpisode(JSONObject episodeJSON) {

        super(episodeJSON);

        Map episode = episodeJSON.toMap();

        this.language = (String) episode.get("Language"); /* Defaults to OV  */
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
        episodeJSON.put("Language", language);
        episodeJSON.put("Writer", writer);
        episodeJSON.put("Director", director);
        episodeJSON.put("Actors", actors);

        return episodeJSON;
    }
    /* END Format Conversion Methods */

    /* Getters & Setters */

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Enum getType() {
        return MediaType.EPISODE;
    }

    public String getSeriesID() {
        return seriesID;
    }

    public void setSeriesID(String seriesID) {
        this.seriesID = seriesID;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public ArrayList getActors() {
        return actors;
    }

    public void setActors(ArrayList actors) {
        this.actors = actors;
    }

    public ArrayList getWriter() {
        return writer;
    }

    public void setWriter(ArrayList writer) {
        this.writer = writer;
    }

    public ArrayList getDirector() {
        return director;
    }

    public void setDirector(ArrayList director) {
        this.director = director;
    }

/* END Getters & Setters */
}
