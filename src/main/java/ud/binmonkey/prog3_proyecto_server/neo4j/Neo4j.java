package ud.binmonkey.prog3_proyecto_server.neo4j;

import org.neo4j.driver.v1.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ud.binmonkey.prog3_proyecto_server.common.DocumentReader;
import ud.binmonkey.prog3_proyecto_server.neo4j.omdb.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import static org.neo4j.driver.v1.Values.parameters;

public class Neo4j {

    /* Logger for Neo4j */
    private static final boolean ADD_TO_FIC_LOG = false; /* set false to overwrite */
    private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Neo4j.class.getName());

    static {
        try {
            logger.addHandler(new FileHandler(
                    "logs/" + Neo4j.class.getName() + ".log.xml", ADD_TO_FIC_LOG));
        } catch (SecurityException | IOException e) {
            logger.log(Level.SEVERE, "Error in log file creation");
        }
    }
    /* END Logger for Neo4j */

    private String username;
    private String password;
    private String server_address;
    private Driver driver;
    private Session session;

    /**
     * Constructor for the class Neoj
     */
    public Neo4j() {
        readConfig();
        try {
            startSession();
        } catch (org.neo4j.driver.v1.exceptions.ServiceUnavailableException e) {
            logger.log(Level.SEVERE, "Unable to connect to server," +
                    " ensure the database is running and that there is a working network connection to it.");
            System.exit(0);
        } catch (org.neo4j.driver.v1.exceptions.AuthenticationException e) {
            logger.log(Level.SEVERE, ": The client is unauthorized due to authentication failure.");
            System.exit(0);
        }
    }

    private void readConfig() {

        NodeList nList = DocumentReader.getDoc("conf/Neo4jServer.xml").getElementsByTagName("neo4j-server");
        Node nNode = nList.item(0);
        Element eElement = (Element) nNode;


        username = eElement.getElementsByTagName("username").item(0).getTextContent();
        password = eElement.getElementsByTagName("password").item(0).getTextContent();
        server_address = eElement.getElementsByTagName("server_address").item(0).getTextContent();
    }

    public void startSession() {
        driver = GraphDatabase.driver(server_address, AuthTokens.basic(username, password));
        session = driver.session();
    }

    public void closeSession() {
        session.close();
        driver.close();
    }

    /**
     * Adds an IMDB title to the DB
     *
     * @param id - IMDB id of the title
     */
    public void addTitle(String id) {
        if (MediaType.movie.equalsName((String) Omdb.getTitle(id).get("Type"))) {
            addMovie(id);
        } else if (MediaType.series.equalsName((String) Omdb.getTitle(id).get("Type"))) {
            addSeries(id);
        } else if (MediaType.episode.equalsName((String) Omdb.getTitle(id).get("Type"))) {
            addEpisode(id);
        }
    }

    /**
     * Adds an IMDB movie to the DB
     *
     * @param id - IMDB id of the movie
     */
    private void addMovie(String id) {
        if (!checkNode(id, "Movie")) {
            OmdbMovie movie = new OmdbMovie(id);

            session.run(
                    "CREATE (a:Movie {title: {title}, name: {name}, year: {year}, released: {released}, dvd: {dvd}," +
                            " plot: {plot}, rated: {rated}, awards: {awards}, boxOffice: {boxOffice}," +
                            " metascore: {metascore}, imdbRating: {imdbRating}, imdbVotes: {imdbVotes}," +
                            " runtime: {runtime}, website: {website}, poster: {poster}})",
                    (Value) movie.toParameters());

            logger.log(Level.INFO, "Added Movie: " + movie.getImdbID());

            addNodeList(movie.getLanguage(), "Language", id, "Movie", "SPOKEN_LANGUAGE");
            addNodeList(movie.getGenre(), "Genre", id, "Movie", "GENRE");
            addNodeList(movie.getWriter(), "Person", id, "Movie", "WROTE");
            addNodeList(movie.getDirector(), "Person", id, "Movie", "DIRECTED");
            addNodeList(movie.getActors(), "Person", id, "Movie", "ACTED_IN");
            addNodeList(movie.getProducers(), "Producer", id, "Movie", "PRODUCED");
            addNodeList(movie.getCountry(), "Country", id, "Movie", "COUNTRY");


            /* ScoreOutles TODO standarize scores */
            for (Object outlet : movie.getRatings().keySet()) {
                if (!checkNode((String) outlet, "ScoreOutlet")) {
                    session.run("CREATE (a:ScoreOutlet {name: {name}})",
                            parameters("name", outlet));

                    logger.log(Level.INFO, "Added ScoreOutlet: " + outlet);
                }

                String score = (String) movie.getRatings().get(outlet);

                session.run("MATCH (a:ScoreOutlet { name: {name}}), (b:Movie { name: {id}}) " +
                                "CREATE (a)-[:SCORED {score: {score}}]->(b)"
                        , parameters("name", outlet, "score", score, "id", id));

                logger.log(Level.INFO, "Added SCORED: " + outlet + " -(" + score + ")-> " + id);
            }
        } else {
            logger.log(Level.WARNING, id + " already exists");
        }
    }

    /**
     * Adds an IMDB series to the DB
     *
     * @param id - IMDB id of the series
     */
    private void addSeries(String id) {
        if (!checkNode(id, "Series")) {
            OmdbSeries series = new OmdbSeries(id);

            session.run(
                    "CREATE (a:Series {title: {title}, name: {name}, year: {year}, seasons: {seasons}," +
                            " released: {released}, plot: {plot}, rated: {rated}, awards: {awards}," +
                            " metascore: {metascore}, imdbRating: {imdbRating}, imdbVotes: {imdbVotes}," +
                            " runtime: {runtime}, poster: {poster}})",
                    (Value) series.toParameters());

            logger.log(Level.INFO, "Added Series: " + series.getImdbID());

            addNodeList(series.getLanguage(), "Language", id, "Series", "SPOKEN_LANGUAGE");
            addNodeList(series.getGenre(), "Genre", id, "Series", "GENRE");
            addNodeList(series.getProducers(), "Producer", id, "Series", "PRODUCED");
            addNodeList(series.getCountry(), "Country", id, "Series", "COUNTRY");
        } else {
            logger.log(Level.WARNING, id + " already exists");
        }
    }

    /**
     * Adds an IMDB episode to the DB
     *
     * @param id - IMDB id of the episode
     */
    private void addEpisode(String id) {
        if (!checkNode(id, "Episode")) {
            OmdbEpisode episode = new OmdbEpisode(id);

            session.run(
                    "CREATE (a:Episode {title: {title}, name: {name}, year: {year}, released: {released}," +
                            " plot: {plot}, rated: {rated}, awards: {awards}, metascore: {metascore}," +
                            " imdbRating: {imdbRating}, imdbVotes: {imdbVotes}, runtime: {runtime}, poster: {poster}})",
                    (Value) episode.toParameters());

            logger.log(Level.INFO, "Added Episode: " + episode.getImdbID());

            addNodeList(episode.getWriter(), "Person", id, "Episode", "WROTE");
            addNodeList(episode.getDirector(), "Person", id, "Episode", "DIRECTED");
            addNodeList(episode.getActors(), "Person", id, "Episode", "ACTED_IN");

            if (!checkNode(episode.getSeriesID(), "Series")) {
                addSeries(episode.getSeriesID());
            } else {
                logger.log(Level.WARNING, episode.getImdbID() + " already exists");
            }

            session.run("MATCH (a: Episode { name: {name}}), (b:Series { name: {title}}) " +
                            "CREATE (a)-[:BELONGS_TO { season: {season}, episode: {episode}}]->(b)",
                    parameters("name", id, "title", episode.getSeriesID(), "season",
                            episode.getSeason(), "episode", episode.getEpisode()));

        } else {
            logger.log(Level.WARNING, id + " already exists");
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
    private void addNodeList(ArrayList list, String node_type, String title, String node2_type, String relation_type) {
        for (Object o : list) {
            String node = o.toString();
            if (!checkNode(node, node_type)) {

                session.run(
                        "CREATE(p:" + node_type + " {name: {name}})",
                        parameters("name", node));

                logger.log(Level.INFO, "Added " + node_type + ": " + node);
            } else {
                logger.log(Level.WARNING, node + " already exists");
            }

            session.run("MATCH (a:" + node_type + " { name: {name}}), (b:" + node2_type + " { name: {title}}) " +
                    "CREATE (a)-[:" + relation_type + "]->(b)", parameters("name", node, "title", title));

            logger.log(Level.INFO, "Added " + relation_type + ": " + node + " -> " + title);
        }
    }

    /**
     * Check if a Node exists in the DB
     *
     * @param id - Identifier of the Node
     * @return true if the Node exists
     */
    private boolean checkNode(String id, String type) {

        boolean existance = false;
        StatementResult result = session.run("MATCH (a:" + type + ") RETURN a.name");

        while (result.hasNext()) {
            Record record = result.next();

            if (record.get("a.name").asString().equals(id)) {
                existance = true;
            }
        }
        return existance;
    }

    public Session getSession() {
        return session;
    }

    public void clearDB() {
        session.run("MATCH (n) DETACH DELETE n;");
        logger.log(Level.INFO, "Cleared DB");
    }
}
