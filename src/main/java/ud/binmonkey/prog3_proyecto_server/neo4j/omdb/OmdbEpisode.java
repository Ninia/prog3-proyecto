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

    public static void main(String[] args) {
        OmdbEpisode omdbEpisode = new OmdbEpisode("tt3097534");

        System.out.println(omdbEpisode.toJSON());
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

    /**
     * Converts OmdbEpisode to JSON format
     *
     * @return JSONObject
     */
    public JSONObject toJSON() {

        JSONObject episodeJSON = super.toJSON();

        episodeJSON.put("seriesID", seriesID);
        episodeJSON.put("season", season);
        episodeJSON.put("episode", episode);

        episodeJSON.put("writer", writer);
        episodeJSON.put("director", director);
        episodeJSON.put("actors", actors);

        return episodeJSON;
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

    /* Overridden Methods */
    @Override
    public String toString() {
        return "OmdbMovie:\n" +
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
                "\tPoster=" + poster + "\n" +
                "\tGenre=" + writer + "\n";
    }
}
