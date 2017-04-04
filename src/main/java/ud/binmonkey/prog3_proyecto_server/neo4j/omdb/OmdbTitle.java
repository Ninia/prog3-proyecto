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
        this.year = (String) title.get("Year");
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Date getReleased() {
        return released;
    }

    public void setReleased(Date released) {
        this.released = released;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public int getMetascore() {
        return metascore;
    }

    public void setMetascore(int metascore) {
        this.metascore = metascore;
    }

    public int getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(int imdbRating) {
        this.imdbRating = imdbRating;
    }

    public int getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(int imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
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
