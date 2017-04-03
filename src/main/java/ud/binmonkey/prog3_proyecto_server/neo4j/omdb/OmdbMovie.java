package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

import java.util.Map;

public class OmdbMovie extends OmdbTitle {


    public OmdbMovie(String id) {

        super(Omdb.getTitle(id));
        Map movie = Omdb.getTitle(id);
    }

    public static void main(String[] args) {

        OmdbMovie movie = new OmdbMovie("tt0117951");

        System.out.println(movie);
    }

    @Override
    public String toString() {
        return "OmdbMovie:\n" +
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
