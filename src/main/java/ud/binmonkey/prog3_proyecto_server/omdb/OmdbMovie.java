package ud.binmonkey.prog3_proyecto_server.omdb;

import org.json.JSONObject;
import ud.binmonkey.prog3_proyecto_server.common.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;

public class OmdbMovie extends OmdbTitle {

    private Date dvd;
    private double boxOffice; /* in Dollars */
    private String website;

    private HashMap ratings = new HashMap<String, Integer>();
    private String language;
    private ArrayList genre;
    private ArrayList writer;
    private ArrayList director;
    private ArrayList actors;
    private ArrayList producers;
    private ArrayList country;

    /**
     * Constructor for the class OmdbMovie that extends from OmdbTitle
     *
     * @param movieJSON - JSON with the info of the Episode
     */
    public OmdbMovie(JSONObject movieJSON) {
        super(movieJSON);

        Map movie = movieJSON.toMap();


        this.dvd = JSONFormatter.dateFormatter(movie.get("DVD"));
        this.boxOffice = JSONFormatter.doubleConversor(movie.get("BoxOffice"));
        this.website = (String) movie.get("Website");
        this.language = (String) movie.get("Language"); /* Defaults to OV  */
        this.genre = JSONFormatter.listFormatter(movie.get("Genre"));
        this.writer = JSONFormatter.listFormatter(movie.get("Writer"));
        this.director = JSONFormatter.listFormatter(movie.get("Director"));
        this.actors = JSONFormatter.listFormatter(movie.get("Actors"));
        this.producers = JSONFormatter.listFormatter(movie.get("Production"));
        this.country = JSONFormatter.listFormatter(movie.get("Country"));

        try {
            this.ratings = (HashMap) movie.get("Ratings");
        } catch (java.lang.ClassCastException e) { /* If it is not formatted */
            this.ratings = JSONFormatter.scoreFormatter((ArrayList) movie.get("Ratings"));
        }
    }

    /* Format Conversion Methods */

    public Object toParameters() {
        return parameters(
                "title", title,
                "name", imdbID,
                "year", year,
                "released", released.toString(),
                "dvd", dvd.toString(),
                "plot", plot,
                "awards", awards,
                "boxOffice", boxOffice,
                "metascore", metascore,
                "imdbRating", imdbRating,
                "imdbVotes", imdbVotes,
                "runtime", runtime,
                "website", website,
                "poster", poster);
    }

    /**
     * @return Return information in JSON format
     */
    public JSONObject toJSON() {

        JSONObject episodeJSON = super.toJSON();

        episodeJSON.put("DVD", DateUtils.dateFormatter(dvd));
        episodeJSON.put("BoxOffice", boxOffice);
        episodeJSON.put("Website", website);
        episodeJSON.put("Ratings", ratings);
        episodeJSON.put("Language", language);
        episodeJSON.put("Genre", genre);
        episodeJSON.put("Writer", writer);
        episodeJSON.put("Director", director);
        episodeJSON.put("Actors", actors);
        episodeJSON.put("Production", producers);
        episodeJSON.put("Country", country);

        return episodeJSON;
    }
    /* END Format Conversion Methods */

    /* Getters & Setters */

    public Enum getType() {
        return MediaType.MOVIE;
    }

    public Date getDvd() {
        return dvd;
    }

    public void setDvd(Date dvd) {
        this.dvd = dvd;
    }

    public double getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(double boxOffice) {
        this.boxOffice = boxOffice;
    }

    public String getWebsite() {
        return website;
    }

    public HashMap getRatings() {
        return ratings;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ArrayList getGenre() {
        return genre;
    }

    public void setGenre(ArrayList genre) {
        this.genre = genre;
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

    public ArrayList getActors() {
        return actors;
    }

    public void setActors(ArrayList actors) {
        this.actors = actors;
    }

    public ArrayList getProducers() {
        return producers;
    }

    public void setProducers(ArrayList producers) {
        this.producers = producers;
    }

    public ArrayList getCountry() {
        return country;
    }

    public void setCountry(ArrayList country) {
        this.country = country;
    }

/* END Getters & Setters */
}
