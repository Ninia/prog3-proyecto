package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

import org.json.JSONObject;

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

    OmdbTitle(Map title) {
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

    /**/
    public JSONObject toJSON() {

        JSONObject episodeJSON = new JSONObject();

        episodeJSON.put("title", title);
        episodeJSON.put("name", imdbID);
        episodeJSON.put("year", year);
        episodeJSON.put("released", released.toString());
        episodeJSON.put("plot", plot);
        episodeJSON.put("rated", ageRating);
        episodeJSON.put("awards", awards);
        episodeJSON.put("metascore", metascore);
        episodeJSON.put("imdbRating", imdbRating);
        episodeJSON.put("imdbVotes", imdbVotes);
        episodeJSON.put("runtime", runtime);
        episodeJSON.put("poster", poster);

        return episodeJSON;
    }

    /**/
    /* Getters and Setters */
    public String getImdbID() {
        return imdbID;
    }

    public String getAgeRating() {
        return ageRating;
    }

    /* Overridden Methods*/
    @Override
    public String toString() {
        return "OmdbTitle:\n" +
                "\tTitle=" + title + "\n" +
                "\tIMDB ID=" + imdbID + "\n" +
                "\tYear=" + year + "\n" +
                "\tReleased=" + released + "\n" +
                "\tPlot=" + plot + "\n" +
                "\tRated=" + ageRating + "\n" +
                "\tAward=" + awards + "\n" +
                "\tMetascore=" + metascore + "\n" +
                "\tIMDB Rating=" + imdbRating + "\n" +
                "\tIMDB Votes=" + imdbVotes + "\n" +
                "\tRuntime=" + runtime + "\n" +
                "\tPoster=" + poster + "\n";
    }
}
