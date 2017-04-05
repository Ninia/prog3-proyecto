package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

import java.util.Date;
import java.util.Map;

public class OmdbTitle {

    protected String title;
    protected String imdbID;
    protected String year;
    protected Date released;
    protected String plot;
    protected String rated;
    protected String awards;
    protected int metascore;
    protected int imdbRating;
    protected int imdbVotes;
    protected int runtime; /* Minutes */
    protected String poster;

    OmdbTitle(Map title) {
        this.title = (String) title.get("Title");
        this.imdbID = (String) title.get("imdbID");
        this.year = Omdb.yearFormatter(title.get("Year"));
        this.released = Omdb.dateFormatter(title.get("Released"));
        this.plot = (String) title.get("Plot");
        this.rated = (String) title.get("Rated");
        this.awards = (String) title.get("Awards");
        this.metascore = Omdb.intergerConversor(title.get("Metascore"));
        this.imdbRating = Omdb.intergerConversor(title.get("imdbRating"));
        this.imdbVotes = Omdb.intergerConversor(title.get("imdbVotes"));
        this.runtime = Omdb.intergerConversor(title.get("Runtime"));
        this.poster = (String) title.get("Poster");
    }

    /* Getters and Setters */

    public String getImdbID() {
        return imdbID;
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
                "\tRated=" + rated + "\n" +
                "\tAward=" + awards + "\n" +
                "\tMetascore=" + metascore + "\n" +
                "\tIMDB Rating=" + imdbRating + "\n" +
                "\tIMDB Votes=" + imdbVotes + "\n" +
                "\tRuntime=" + runtime + "\n" +
                "\tPoster=" + poster + "\n";
    }
}
