package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

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

    /* Getters and Setters */

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
