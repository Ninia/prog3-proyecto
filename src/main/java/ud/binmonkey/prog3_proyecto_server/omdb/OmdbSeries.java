package ud.binmonkey.prog3_proyecto_server.omdb;

import org.json.JSONObject;

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
     * Constructor for the class OmdbEpisode that extends from OmdbTitle
     *
     * @param seriesJSON - JSON with the info of the Episode
     */
    public OmdbSeries(JSONObject seriesJSON) {

        super(seriesJSON);

        Map series = seriesJSON.toMap();

        this.seasons = JSONFormatter.intergerConversor(series.get("totalSeasons"));
        this.language = JSONFormatter.listFormatter(series.get("Language"));
        this.genre = JSONFormatter.listFormatter(series.get("Genre"));
        this.producers.add("placeholder"); /* TODO this is a placeholder */
        this.country = JSONFormatter.listFormatter(series.get("Country"));
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

    /**
     * @return Return information in JSON format
     */
    public JSONObject toJSON() {

        JSONObject episodeJSON = super.toJSON();

        episodeJSON.put("totalSeasons", seasons);
        episodeJSON.put("Language", language);
        episodeJSON.put("Genre", genre);
        episodeJSON.put("Production", producers);
        episodeJSON.put("Country", country);

        return episodeJSON;
    }
    /* END Format Conversion Methods */

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

    public int getSeasons() {
        return seasons;
    }

    /* END Getters */
}
