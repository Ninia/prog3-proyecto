package ud.binmonkey.prog3_proyecto_server.neo4j;

import org.neo4j.driver.v1.*;

import static org.neo4j.driver.v1.Values.parameters;

public class Neo4j {

    private String username;
    private String password;
    private Driver driver;
    private Session session;


    public Neo4j() {
        readConfig();
        startSession();
    }

    private void readConfig() {
        username = "test";
        password = "test";
    }

    public void startSession() {
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic(username, password));
        session = driver.session();
    }

    public void closeSession() {
        session.close();
        driver.close();
    }

    public Session getSession() {
        return session;
    }

    public void clearDB() {
        session.run("MATCH (n) DETACH DELETE n;");
    }

    public static void main(String[] args) {
        Neo4j neo4j = new Neo4j();

        Session session = neo4j.getSession();
        session.run("CREATE (a:Person {name: {name}, title: {title}})", parameters("name", "Arthur", "title", "King"));

        StatementResult result = session.run("MATCH (a:Person) WHERE a.name = {name} " +
                        "RETURN a.name AS name, a.title AS title",
                parameters("name", "Arthur"));
        while (result.hasNext()) {
            Record record = result.next();
            System.out.println(record.get("title").asString() + " " + record.get("name").asString());
        }

        neo4j.clearDB();
        neo4j.closeSession();


    }
}
