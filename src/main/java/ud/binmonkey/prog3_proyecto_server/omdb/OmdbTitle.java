package ud.binmonkey.prog3_proyecto_server.omdb;

import org.json.JSONObject;
import ud.binmonkey.prog3_proyecto_server.common.time.DateUtils;

import java.util.Date;
import java.util.Map;

public class OmdbTitle {

    protected String title;
    protected String imdbID;
    protected String year;
    protected Date released;
    protected String plot;
    protected String ageRating;
    protected String awards;
    protected int metascore;
    protected int imdbRating;
    protected int imdbVotes;
    protected int runtime; /* Minutes */
    protected String poster;

    OmdbTitle(JSONObject titleJSON) {

        Map title = titleJSON.toMap();

        this.title = (String) title.get("Title");
        this.imdbID = (String) title.get("imdbID");
        this.year = JSONFormatter.yearFormatter(title.get("Year"));
        this.released = JSONFormatter.dateFormatter(title.get("Released"));
        this.plot = (String) title.get("Plot");
        this.ageRating = (String) title.get("Rated");
        this.awards = (String) title.get("Awards");
        this.metascore = JSONFormatter.intergerConversor(title.get("Metascore"));
        this.imdbRating = JSONFormatter.intergerConversor(title.get("imdbRating"));
        this.imdbVotes = JSONFormatter.intergerConversor(title.get("imdbVotes"));
        this.runtime = JSONFormatter.intergerConversor(title.get("Runtime"));
        this.poster = (String) title.get("Poster");
    }

    public JSONObject toJSON() {

        JSONObject episodeJSON = new JSONObject();

        episodeJSON.put("Title", title);
        episodeJSON.put("imdbID", imdbID);
        episodeJSON.put("Year", year);
        episodeJSON.put("Released", DateUtils.dateFormatter(released));
        episodeJSON.put("Plot", plot);
        episodeJSON.put("Rated", ageRating);
        episodeJSON.put("Awards", awards);
        episodeJSON.put("Metascore", metascore);
        episodeJSON.put("imdbRating", imdbRating);
        episodeJSON.put("imdbVotes", imdbVotes);
        episodeJSON.put("Runtime", runtime);
        episodeJSON.put("Poster", poster);

        return episodeJSON;
    }

    /* Getters and Setters */

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public Date getReleased() {
        return released;
    }

    public String getPlot() {
        return plot;
    }

    public String getAwards() {
        return awards;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getPoster() {
        return poster;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public int getMetascore() {
        return metascore;
    }

    public int getImdbRating() {
        return imdbRating;
    }

    public int getImdbVotes() {
        return imdbVotes;
    }

}
