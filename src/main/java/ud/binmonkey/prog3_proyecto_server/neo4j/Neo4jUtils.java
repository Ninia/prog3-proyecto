package ud.binmonkey.prog3_proyecto_server.neo4j;

import org.apache.commons.lang3.StringUtils;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import ud.binmonkey.prog3_proyecto_server.common.DocumentReader;
import ud.binmonkey.prog3_proyecto_server.common.time.DateUtils;
import ud.binmonkey.prog3_proyecto_server.mysql.MySQLUtils;
import ud.binmonkey.prog3_proyecto_server.omdb.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import static org.neo4j.driver.v1.Values.parameters;

public class Neo4jUtils extends Neo4j {

    /* Logger for Neo4jUtils */
    private static final String ftpd = DocumentReader.getAttr(DocumentReader.getDoc("conf/properties.xml"),
            "network", "ftp-server", "ftpd").getTextContent();
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Neo4jUtils.class.getName());

    static {
        try {
            LOG.addHandler(new FileHandler(
                    "logs/" + Neo4jUtils.class.getName() + "." +
                            DateUtils.currentFormattedDate() + ".log.xml", true));
        } catch (SecurityException | IOException e) {
            LOG.log(Level.SEVERE, "Unable to create log file.");
        }
    }
    /* END Logger for Neo4j */

    private MySQLUtils mySQL;

    public Neo4jUtils() {
        super();

        mySQL = new MySQLUtils();
    }

    /* Add Methods */

    /**
     * Adds an IMDB title to the DB
     *
     * @param title - OmdbTitle
     */
    public void addTitle(OmdbTitle title) {

        if (title instanceof OmdbMovie) {
            addMovie((OmdbMovie) title);
        } else if (title instanceof OmdbSeries) {
            addSeries((OmdbSeries) title);
        } else if (title instanceof OmdbEpisode) {
            addEpisode((OmdbEpisode) title);
        }

    }

    /**
     * Adds an IMDB movie to the DB
     *
     * @param movie - OmdbMovie to add to the DB
     */
    public void addMovie(OmdbMovie movie) {
        if (!checkNode(movie.getImdbID(), "Movie")) {
            getSession().run(
                    "CREATE (a:Movie {title: {title}, name: {name}, year: {year}, released: {released}, dvd: {dvd}," +
                            " plot: {plot}, awards: {awards}, boxOffice: {boxOffice}," +
                            " metascore: {metascore}, imdbRating: {imdbRating}, imdbVotes: {imdbVotes}," +
                            " runtime: {runtime}, website: {website}, poster: {poster}})",
                    (Value) movie.toParameters());

            LOG.log(Level.INFO, "Added Movie: " + movie.getImdbID());
            mySQL.dwhLog("ADD", movie.getImdbID(), MediaType.MOVIE);

            addLanguage(movie.getLanguage(), movie.getImdbID(), movie.getFilename());

            addNode(movie.getAgeRating(), "Rating", movie.getImdbID(), "RATED");
            addNodeList(movie.getGenre(), "Genre", movie.getImdbID(), "GENRE");
            addNodeList(movie.getWriter(), "Person", movie.getImdbID(), "WROTE");
            addNodeList(movie.getDirector(), "Person", movie.getImdbID(), "DIRECTED");
            addNodeList(movie.getActors(), "Person", movie.getImdbID(), "ACTED_IN");
            addNodeList(movie.getProducers(), "Producer", movie.getImdbID(), "PRODUCED");
            addNodeList(movie.getCountry(), "Country", movie.getImdbID(), "COUNTRY");


            /* ScoreOutles */
            for (Object outlet : movie.getRatings().keySet()) {
                addRating(movie, (String) outlet, (Integer) movie.getRatings().get(outlet));
            }
            /* END Score Outlets */

            /* Download image */
            String fileName = ftpd + "/common/data/images/" +
                    (movie.getTitle()) + "(" + movie.getYear() + ").jpg";


            java.io.File file = new File(fileName);
            if (!file.exists()) {
                try {
                    InputStream in = new BufferedInputStream(new URL(movie.getPoster()).openStream());

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int n;

                    while ((n = in.read(buf)) != -1) {
                        out.write(buf, 0, n);
                    }

                    out.close();
                    in.close();
                    byte[] response = out.toByteArray();

                    FileOutputStream fos = new FileOutputStream(file.getPath());
                    fos.write(response);
                    fos.close();

                } catch (IOException e) {
                    return;
                }
            }

        } else {
            LOG.log(Level.WARNING, movie.getImdbID() + " already exists");
        }
    }

    /**
     * Adds an IMDB series to the DB
     *
     * @param series - OmdbSeries to add to the DB
     */
    public void addSeries(OmdbSeries series) {
        if (!checkNode(series.getImdbID(), "Series")) {

            getSession().run(
                    "CREATE (a:Series {title: {title}, name: {name}, year: {year}, seasons: {seasons}," +
                            " released: {released}, plot: {plot}, awards: {awards}," +
                            " metascore: {metascore}, imdbRating: {imdbRating}, imdbVotes: {imdbVotes}," +
                            " runtime: {runtime}, poster: {poster}})",
                    (Value) series.toParameters());

            LOG.log(Level.INFO, "Added Series: " + series.getImdbID());
            mySQL.dwhLog("ADD", series.getImdbID(), MediaType.SERIES);

            /* Score Outles*/
            addRating(series, "Internet Movie Database", series.getImdbRating());
            if (series.getMetascore() != 0)
                addRating(series, "Metacritic", series.getMetascore());
            /* END Score Outlets */

            addNode(series.getAgeRating(), "Rating", series.getImdbID(), "RATED");
            addNodeList(series.getGenre(), "Genre", series.getImdbID(), "GENRE");
            addNodeList(series.getProducers(), "Producer", series.getImdbID(), "PRODUCED");
            addNodeList(series.getCountry(), "Country", series.getImdbID(), "COUNTRY");

        } else {
            LOG.log(Level.WARNING, series.getImdbID() + " already exists");
        }
    }

    /**
     * Adds an IMDB episode to the DB
     *
     * @param episode - OmdbEpisode to add to the DB
     */
    public void addEpisode(OmdbEpisode episode) {
        if (!checkNode(episode.getImdbID(), "Episode")) {


            getSession().run(
                    "CREATE (a:Episode {title: {title}, name: {name}, year: {year}, released: {released}," +
                            " plot: {plot}, awards: {awards}, metascore: {metascore}," +
                            " imdbRating: {imdbRating}, imdbVotes: {imdbVotes}, runtime: {runtime}, poster: {poster}})",
                    (Value) episode.toParameters());

            LOG.log(Level.INFO, "Added Episode: " + episode.getImdbID());
            mySQL.dwhLog("ADD", episode.getImdbID(), MediaType.EPISODE);

            /* Score Outles*/
            addRating(episode, "Internet Movie Database", episode.getImdbRating());
            if (episode.getMetascore() != 0)
                addRating(episode, "Metacritic", episode.getMetascore());
            /* END Score Outlets */
            addNodeList(episode.getWriter(), "Person", episode.getImdbID(), "WROTE");
            addNodeList(episode.getDirector(), "Person", episode.getImdbID(), "DIRECTED");
            addNodeList(episode.getActors(), "Person", episode.getImdbID(), "ACTED_IN");
            addLanguage(episode.getLanguage(), episode.getImdbID(), episode.getFilename());

            if (!checkNode(episode.getSeriesID(), "Series")) {
                addSeries(new OmdbSeries(Omdb.getTitle(episode.getSeriesID())));
            } else {
                LOG.log(Level.WARNING, episode.getImdbID() + " already exists");
            }

            getSession().run("MATCH (a: Episode { name: {name}}), (b:Series { name: {title}}) " +
                            "CREATE (a)-[:BELONGS_TO { season: {season}, episode: {episode}}]->(b)",
                    parameters("name", episode.getImdbID(), "title", episode.getSeriesID(), "season",
                            episode.getSeason(), "episode", episode.getEpisode()));
        } else {
            LOG.log(Level.WARNING, episode.getImdbID() + " already exists");
        }
    }

    /**
     * Adds a rating to the DB creating the outlet if necessary
     *
     * @param title  - Title
     * @param outlet - Score Outlet
     * @param score  - Score
     */
    public void addRating(OmdbTitle title, String outlet, int score) {

        String id = title.getImdbID();


        if (!checkNode(outlet, "ScoreOutlet")) {
            getSession().run("CREATE (a:ScoreOutlet {name: {name}})",
                    parameters("name", outlet));

            LOG.log(Level.INFO, "Added ScoreOutlet: " + outlet);
        }

        if (outlet.equals("Internet Movie Database")) { /* IMDB Rating also contain number of votes */

            int votes = title.getImdbVotes();

            getSession().run("MATCH (a:ScoreOutlet { name: {name}}), (b { name: {id}}) " +
                            "CREATE (a)-[:SCORED {score: {score}, votes: {votes}}]->(b)"
                    , parameters("name", outlet, "id", id, "score", score,
                            "votes", votes));

            LOG.log(Level.INFO, "Added SCORED: " + outlet + " -(" + score + ", "
                    + votes + ")-> " + id);

        } else {

            getSession().run("MATCH (a:ScoreOutlet { name: {name}}), (b { name: {id}}) " +
                            "CREATE (a)-[:SCORED {score: {score}}]->(b)"
                    , parameters("name", outlet, "score", score, "id", id));

            LOG.log(Level.INFO, "Added SCORED: " + outlet + " -(" + score + ")-> " + id);
        }
    }

    private void addLanguage(String language, String title, String filename) {

        if (!checkNode(language, "Language")) {
            getSession().run(
                    "CREATE(p:Language {name: {name}})",
                    parameters("name", language));

            LOG.log(Level.INFO, "Added Language: " + language);
        } else {
            LOG.log(Level.WARNING, language + " already exists");
        }

        if (!checkRelation(language, "Language", title, "SPOKEN_LANGUAGE")) {
            getSession().run("MATCH (a:Language { name: {name}}), (b { name: {title}}) " +
                            "CREATE (a)-[:SPOKEN_LANGUAGE {filename: {filename}}]->(b)",
                    parameters("name", language, "title", title, "filename", filename));

            LOG.log(Level.INFO, "Added SPOKEN_LANGUAGE: " + filename + "(" + language + ") -> " + title);

        } else {
            LOG.log(Level.WARNING, "SPOKEN_LANGUAGE: " + language + " -> " + title + " already exists");
        }
    }

    /**
     * Adds a Node to the DB creating a relation with another node
     *
     * @param node_type     - Type of the node to create
     * @param title         - Title the relation is assigned to
     * @param relation_type - Type of the relation between the node and the title
     */
    public void addNode(String node, String node_type, String title, String relation_type) {

        if (!checkNode(node, node_type)) {
            getSession().run(
                    "CREATE(p:" + node_type + " {name: {name}})",
                    parameters("name", node));

            LOG.log(Level.INFO, "Added " + node_type + ": " + node);
        } else {
            LOG.log(Level.WARNING, node + " already exists");
        }

        addRelation(node, node_type, title, relation_type);
    }

    /**
     * Add a relation between a node and a title
     *
     * @param node          - Node to start the relation with
     * @param node_type     - Type of node to start the relation with
     * @param title         - Title to attach the relation to
     * @param relation_type - Type of relation
     */
    public void addRelation(String node, String node_type, String title, String relation_type) {
        if (!checkRelation(node, node_type, title, relation_type)) {
            getSession().run("MATCH (a:" + node_type + " { name: {name}}), (b { name: {title}}) " +
                    "CREATE (a)-[:" + relation_type + "]->(b)", parameters("name", node, "title", title));

            LOG.log(Level.INFO, "Added " + relation_type + ": " + node + " -> " + title);

        } else {
            LOG.log(Level.WARNING, relation_type + ": " + node + " -> " + title + " already exists");
        }
    }

    /**
     * Takes a ArrayList of values and turns them into Nodes
     *
     * @param list          - List of values to turn into Nodes
     * @param node_type     - Type of the nodes to create
     * @param title         - Title the relation is assigned to
     * @param relation_type - Type of the relation between the node and the title
     */
    public void addNodeList(ArrayList list, String node_type, String title, String relation_type) {
        for (Object o : list) {
            String node = o.toString();
            addNode(node, node_type, title, relation_type);
        }
    }

    public void addList(String name, OmdbTitle... titles) {
        for (OmdbTitle title : titles) {
            addTitle(title);
            addNode(name, "List", title.getImdbID(), "CONTAINS");
        }
    }
    /* END Add Methods */

    /* Modify Methods */

    /**
     * Removes a single title from the DB
     *
     * @param title - Title to remove
     * @param type  - Omdb type of title
     */
    public void removeTitle(String title, MediaType type) {
        getSession().run(
                "MATCH (n:" + StringUtils.capitalize(type.toString()) + "{name: {title}})" +
                        "OPTIONAL MATCH (n)-[r]-()" +
                        "DELETE n, r",
                parameters("title", title));

        cleanDB();

        LOG.log(Level.INFO, title + " deleted");

        mySQL.dwhLog("DELETE", title, type);
    }

    /**
     * Deletes a node and all its relations
     *
     * @param node      - Node to delete
     * @param node_type - Type of node to delete
     */
    public void deleteNode(String node, String node_type) {
        getSession().run(
                "MATCH (n:" + node_type + "{name: {node}})" +
                        "OPTIONAL MATCH (n)-[r]-()" +
                        "DELETE n, r",
                parameters("node", node));

        cleanDB();

        LOG.log(Level.INFO, node + " deleted");
    }

    /**
     * Renames a node
     *
     * @param node      - Node to rename
     * @param new_name  - New name for the node
     * @param node_type - Type of node to rename
     */
    public void renameNode(String node, String new_name, String node_type) {

        Record record;

        if (checkNode(node, node_type)) {
            if (!checkNode(new_name, node_type)) {
                getSession().run(
                        "MATCH (n:" + node_type + ")" +
                                " WHERE n.name={name}" +
                                " SET n.name={new_name}",
                        parameters("name", node, "new_name", new_name));
                LOG.log(Level.INFO, "Renamed " + node_type + ": " + node + " to " + new_name);
            } else {
                LOG.log(Level.WARNING, node + " already exists starting to move relations");

                StatementResult result = getSession().run(
                        "MATCH (a:" + node_type + " )-[r]-(b) " +
                                " WHERE a.name={node}" +
                                " RETURN b.name as title, TYPE(r) as relation_type",
                        parameters("node", node));

                while (result.hasNext()) {
                    record = result.next();

                    addRelation(new_name, node_type, record.get("title").asString(), record.get("relation_type").asString());
                }

                deleteNode(node, node_type);
            }
        } else {
            LOG.log(Level.WARNING, node + " does not exist");
        }

    }
    /* END Modify Methods */

    /* Overriden Methods */
    @Override
    public void closeSession() {
        super.closeSession();

        mySQL.closeSession();
        LOG.log(Level.INFO, "Connection to MySQL server ended");
    }

    /**
     * Deletes all nodes and relationships from the DB
     */
    @Override
    public void clearDB() {
        super.clearDB();

        mySQL.dwhLog("CLEAR", "ALL", MediaType.ALL);
        LOG.log(Level.INFO, "Cleared MySQL DB");
    }
    /**/
}
