package ud.binmonkey.prog3_proyecto_server.neo4j;

import org.neo4j.driver.v1.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ud.binmonkey.prog3_proyecto_server.common.DocumentReader;
import ud.binmonkey.prog3_proyecto_server.mysql.MySQL;
import ud.binmonkey.prog3_proyecto_server.neo4j.omdb.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
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

    private MySQL mySQL;
    private Statement statement;


    /**
     * Constructor for the class Neoj
     */
    public Neo4j() {
        readConfig();
        while (!startSession()) {
            logger.log(Level.INFO, " Retrying Connection in 5s");
            try {
                Thread.sleep(5000);                 //1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        startDWH();
    }

    /* Server Utility Methods */
    private void readConfig() {

        NodeList nList = DocumentReader.getDoc("conf/Neo4jServer.xml").getElementsByTagName("neo4j-server");
        Node nNode = nList.item(0);
        Element eElement = (Element) nNode;


        username = eElement.getElementsByTagName("username").item(0).getTextContent();
        password = eElement.getElementsByTagName("password").item(0).getTextContent();
        server_address = eElement.getElementsByTagName("server_address").item(0).getTextContent();
    }

    public Session getSession() {
        return session;
    }

    public boolean startSession() {

        try {
            driver = GraphDatabase.driver(server_address, AuthTokens.basic(username, password));
            session = driver.session();
            logger.log(Level.INFO, "Connection to Neo4j server started");
            return true;
        } catch (org.neo4j.driver.v1.exceptions.ServiceUnavailableException e) {
            logger.log(Level.SEVERE, "Unable to connect to server," +
                    " ensure the database is running and that there is a working network connection to it.");
            return false;
        } catch (org.neo4j.driver.v1.exceptions.AuthenticationException e) {
            logger.log(Level.SEVERE, ": The client is unauthorized due to authentication failure.");
            System.exit(0);
        }

        return false;
    }

    public void closeSession() {
        session.close();
        driver.close();
        mySQL.closeSession();

        logger.log(Level.INFO, "Connection to Neo4j server ended");
    }
    /* END Server Utility Methods */

    /* DB utility Methods */

    /**
     * Deletes all nodes and relationships from the DB
     */
    public void clearDB() {
        session.run("MATCH (n) DETACH DELETE n;");
        logger.log(Level.INFO, "Cleared DB");
        dwhLog("CLEAR", "ALL", MediaType.ALL);
    }

    /**
     * Deletes all nodes without relationships from the DB
     */
    public void cleanDB() {
        session.run("MATCH (n) WHERE size((n)--())=0 DELETE (n)");
        logger.log(Level.INFO, "Cleaned DB");
    }

    /**
     * Check if a Node exists in the DB
     *
     * @param name - Identifier of the Node
     * @return true if the Node exists
     */
    private boolean checkNode(String name, String type) {

        boolean existance = false;
        StatementResult result = session.run("MATCH (a:" + type + ") WHERE a.name={name} RETURN a.name",
                parameters("name", name));

        while (result.hasNext()) {
            Record record = result.next();

            if (record.get("a.name").asString().equals(name)) {
                existance = true;
            }
        }
        return existance;
    }

    /**
     * Check if a Relation exists in the DB
     */
    private boolean checkRelation(String node, String node_type, String title, String relation_type) {

        boolean existance = false;

        StatementResult result = session.run("MATCH (a:" + node_type + ")-[:" + relation_type + "]->(b) " +
                "WHERE a.name={node} AND b.name={title} RETURN a.name", parameters("node", node, "title", title));

        while (result.hasNext()) {
            Record record = result.next();

            if (record.get("a.name").asString().equals(node)) {
                existance = true;
            }
        }
        return existance;
    }
    /* END DB utility Methods */

    /* DWH Methods */
    private void startDWH() {
        mySQL = new MySQL();
        statement = mySQL.getStatement();
    }

    /**
     * Logs the creation of a Title in the DWH
     *
     * @param id   - imdbID of the Title
     * @param type - MediaType of the Title
     */
    private void dwhLog(String operation, String id, MediaType type) {
        try {

            statement.executeUpdate("INSERT INTO neo4j_log VALUES (default, '" + operation + "'," +
                    " '" + id + "'," + " '" + type.toString() + "', CURRENT_TIMESTAMP);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* END DWH Methods */

    /* Add Methods */

    /**
     * Adds an IMDB title to the DB, Automatically getting info from OMDB
     *
     * @param id - IMDB id of the title
     */
    public void addTitle(String id) {

        MediaType mediaType = Omdb.getType(id);
        if (mediaType != null) {
            switch (mediaType) {
                case MOVIE:
                    addMovie(new OmdbMovie(Omdb.getTitle(id)));
                    break;
                case SERIES:
                    addSeries(new OmdbSeries(Omdb.getTitle(id)));
                    break;
                case EPISODE:
                    addEpisode(new OmdbEpisode(Omdb.getTitle(id)));
                    break;
            }
        } else {
            logger.log(Level.SEVERE, "Title " + id + " not found on IMDB");
        }
    }

    /**
     * Adds an IMDB movie to the DB
     *
     * @param movie - OmdbMovie to add to the DB
     */
    private void addMovie(OmdbMovie movie) {
        if (!checkNode(movie.getImdbID(), "Movie")) {
            session.run(
                    "CREATE (a:Movie {title: {title}, name: {name}, year: {year}, released: {released}, dvd: {dvd}," +
                            " plot: {plot}, awards: {awards}, boxOffice: {boxOffice}," +
                            " metascore: {metascore}, imdbRating: {imdbRating}, imdbVotes: {imdbVotes}," +
                            " runtime: {runtime}, website: {website}, poster: {poster}})",
                    (Value) movie.toParameters());

            logger.log(Level.INFO, "Added Movie: " + movie.getImdbID());
            dwhLog("ADD", movie.getImdbID(), MediaType.MOVIE);

            addNode(movie.getAgeRating(), "Rating", movie.getImdbID(), "RATED");
            addNodeList(movie.getLanguage(), "Language", movie.getImdbID(), "SPOKEN_LANGUAGE");
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
        } else {
            logger.log(Level.WARNING, movie.getImdbID() + " already exists");
        }
    }

    /**
     * Adds an IMDB series to the DB
     *
     * @param series - OmdbSeries to add to the DB
     */
    private void addSeries(OmdbSeries series) {
        if (!checkNode(series.getImdbID(), "Series")) {

            session.run(
                    "CREATE (a:Series {title: {title}, name: {name}, year: {year}, seasons: {seasons}," +
                            " released: {released}, plot: {plot}, awards: {awards}," +
                            " metascore: {metascore}, imdbRating: {imdbRating}, imdbVotes: {imdbVotes}," +
                            " runtime: {runtime}, poster: {poster}})",
                    (Value) series.toParameters());

            logger.log(Level.INFO, "Added Series: " + series.getImdbID());
            dwhLog("ADD", series.getImdbID(), MediaType.SERIES);

            /* Score Outles*/
            addRating(series, "Internet Movie Database", series.getImdbRating());
            if (series.getMetascore() != 0)
                addRating(series, "Metacritic", series.getMetascore());
            /* END Score Outlets */

            addNode(series.getAgeRating(), "Rating", series.getImdbID(), "RATED");
            addNodeList(series.getLanguage(), "Language", series.getImdbID(), "SPOKEN_LANGUAGE");
            addNodeList(series.getGenre(), "Genre", series.getImdbID(), "GENRE");
            addNodeList(series.getProducers(), "Producer", series.getImdbID(), "PRODUCED");
            addNodeList(series.getCountry(), "Country", series.getImdbID(), "COUNTRY");
        } else {
            logger.log(Level.WARNING, series.getImdbID() + " already exists");
        }
    }

    /**
     * Adds an IMDB episode to the DB
     *
     * @param episode - OmdbEpisode to add to the DB
     */
    private void addEpisode(OmdbEpisode episode) {
        if (!checkNode(episode.getImdbID(), "Episode")) {


            session.run(
                    "CREATE (a:Episode {title: {title}, name: {name}, year: {year}, released: {released}," +
                            " plot: {plot}, awards: {awards}, metascore: {metascore}," +
                            " imdbRating: {imdbRating}, imdbVotes: {imdbVotes}, runtime: {runtime}, poster: {poster}})",
                    (Value) episode.toParameters());

            logger.log(Level.INFO, "Added Episode: " + episode.getImdbID());
            dwhLog("ADD", episode.getImdbID(), MediaType.EPISODE);

            /* Score Outles*/
            addRating(episode, "Internet Movie Database", episode.getImdbRating());
            if (episode.getMetascore() != 0)
                addRating(episode, "Metacritic", episode.getMetascore());
            /* END Score Outlets */

            addNodeList(episode.getWriter(), "Person", episode.getImdbID(), "WROTE");
            addNodeList(episode.getDirector(), "Person", episode.getImdbID(), "DIRECTED");
            addNodeList(episode.getActors(), "Person", episode.getImdbID(), "ACTED_IN");

            if (!checkNode(episode.getSeriesID(), "Series")) {
                addSeries(new OmdbSeries(Omdb.getTitle(episode.getSeriesID())));
            } else {
                logger.log(Level.WARNING, episode.getImdbID() + " already exists");
            }

            session.run("MATCH (a: Episode { name: {name}}), (b:Series { name: {title}}) " +
                            "CREATE (a)-[:BELONGS_TO { season: {season}, episode: {episode}}]->(b)",
                    parameters("name", episode.getImdbID(), "title", episode.getSeriesID(), "season",
                            episode.getSeason(), "episode", episode.getEpisode()));
        } else {
            logger.log(Level.WARNING, episode.getImdbID() + " already exists");
        }
    }

    /**
     * Adds a rating to the DB creating the outlet if necessary
     *
     * @param title  - Title
     * @param outlet - Score Outlet
     * @param score  - Score
     */
    private void addRating(OmdbTitle title, String outlet, int score) {

        String id = title.getImdbID();


        if (!checkNode(outlet, "ScoreOutlet")) {
            session.run("CREATE (a:ScoreOutlet {name: {name}})",
                    parameters("name", outlet));

            logger.log(Level.INFO, "Added ScoreOutlet: " + outlet);
        }

        if (outlet.equals("Internet Movie Database")) { /* IMDB Rating also contain number of votes */

            int votes = title.getImdbVotes();

            session.run("MATCH (a:ScoreOutlet { name: {name}}), (b { name: {id}}) " +
                            "CREATE (a)-[:SCORED {score: {score}, votes: {votes}}]->(b)"
                    , parameters("name", outlet, "id", id, "score", score,
                            "votes", votes));

            logger.log(Level.INFO, "Added SCORED: " + outlet + " -(" + score + ", "
                    + votes + ")-> " + id);

        } else {

            session.run("MATCH (a:ScoreOutlet { name: {name}}), (b { name: {id}}) " +
                            "CREATE (a)-[:SCORED {score: {score}}]->(b)"
                    , parameters("name", outlet, "score", score, "id", id));

            logger.log(Level.INFO, "Added SCORED: " + outlet + " -(" + score + ")-> " + id);
        }
    }

    /**
     * Adds a Node to the DB creating a relation with another node
     *
     * @param node_type     - Type of the node to create
     * @param title         - Title the relation is assigned to
     * @param relation_type - Type of the relation between the node and the title
     */
    private void addNode(String node, String node_type, String title, String relation_type) {

        if (!checkNode(node, node_type)) {
            session.run(
                    "CREATE(p:" + node_type + " {name: {name}})",
                    parameters("name", node));

            logger.log(Level.INFO, "Added " + node_type + ": " + node);
        } else {
            logger.log(Level.WARNING, node + " already exists");
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
    private void addRelation(String node, String node_type, String title, String relation_type) {
        if (!checkRelation(node, node_type, title, relation_type)) {
            session.run("MATCH (a:" + node_type + " { name: {name}}), (b { name: {title}}) " +
                    "CREATE (a)-[:" + relation_type + "]->(b)", parameters("name", node, "title", title));

            logger.log(Level.INFO, "Added " + relation_type + ": " + node + " -> " + title);

            if (node_type.equals("Genre")) {
                try {
                    mySQL.updateCounter("neo4j_genres", node);
                } catch (SQLException ignored) {
                }
            }

        } else {
            logger.log(Level.WARNING, relation_type + ": " + node + " -> " + title + " already exists");
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
    private void addNodeList(ArrayList list, String node_type, String title, String relation_type) {
        for (Object o : list) {
            String node = o.toString();
            addNode(node, node_type, title, relation_type);
        }
    }

    public void addList(String name, String... ids) {
        for (String id : ids) {
            addTitle(id);
            addNode(name, "List", id, "CONTAINS");
        }
    }
    /* END Add Methods */

    /* Modify Methods */

    /**
     * Deletes a node and all its relations
     *
     * @param node      - Node to delete
     * @param node_type - Type of node to delete
     */
    public void deleteNode(String node, String node_type) {
        session.run(
                "MATCH (n:" + node_type + "{name: {node}})" +
                        "OPTIONAL MATCH (n)-[r]-()" +
                        "DELETE n, r",
                parameters("node", node));

        cleanDB();

        logger.log(Level.INFO, node + " deleted");

        switch (node_type) {
            case "Movie":
                dwhLog("DELETE", node, MediaType.MOVIE);
                break;
            case "Series":
                dwhLog("DELETE", node, MediaType.SERIES);
                break;
            case "Episode":
                dwhLog("DELETE", node, MediaType.EPISODE);
        }
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
                session.run(
                        "MATCH (n:" + node_type + ")" +
                                " WHERE n.name={name}" +
                                " SET n.name={new_name}",
                        parameters("name", node, "new_name", new_name));
                logger.log(Level.INFO, "Renamed " + node_type + ": " + node + " to " + new_name);
            } else {
                logger.log(Level.WARNING, node + " already exists starting to move relations");

                StatementResult result = session.run(
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
            logger.log(Level.WARNING, node + " does not exist");
        }

    }
    /* END Modify Methods */
}

