package ud.binmonkey.prog3_proyecto_server.neo4j;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;

public class Neo4jIntegrationTest {

    private static void generateDB(Neo4j neo4j) {
        /* Adding Movies */
        neo4j.addTitle("tt0117951"); /* Trainspotting */
        neo4j.addTitle("tt0289043"); /* 28 Days Later... */
        neo4j.addTitle("tt0470752"); /* Ex Machina */
        neo4j.addTitle("tt0137523"); /* Fight Club */
        neo4j.addTitle("tt0301357"); /* Goodbye Lenin */

        /* LOTR Movies */
        neo4j.addList("Lord of The Rings Saga", "tt0120737", "tt0167261", "tt0167260");

        /* Star Wars Movies */
        neo4j.addList("Star Wars Saga", "tt0120915", "tt0121765", "tt2488496", "tt0076759", "tt0080684",
                "tt0086190", "tt0121766");

        /* Adding Series */
        neo4j.addTitle("tt2802850"); /* Fargo */

        /* Adding Episodes */
        neo4j.addTitle("tt3097534"); /* Fargo S01E01 */
        neo4j.addTitle("tt3296848"); /* Fargo S01E02 */
        neo4j.addTitle("tt3519062"); /* Fargo S01E03 */
        neo4j.addTitle("tt3578722"); /* Fargo S01E04 */
        neo4j.addTitle("tt3514096"); /* Fargo S01E05 */

        /* Fixes duplicate because of different name */
        neo4j.renameNode("Twentieth Century Fox", "20th Century Fox", "Producer");
    }

    public static void main(String[] args) {

        Neo4j neo4j = new Neo4j();
        Record record;

        neo4j.clearDB();
        generateDB(neo4j);

        System.out.println("\nList titles where a certain person acted, ordering them by rating:");
        StatementResult result = neo4j.getSession().run("MATCH p = (n:Person)-[r:ACTED_IN]->(m) " +
                " WHERE n.name = 'Ewan McGregor'" +
                " RETURN m.title as title, m.imdbRating as rating" +
                " ORDER BY rating DESCENDING");

        System.out.println("Sc | Title");
        System.out.println("------------------------------------");
        while (result.hasNext()) {
            record = result.next();
            System.out.println(record.get("rating") + " | " + record.get("title").toString());
        }

        System.out.println("\nList 5 best Movies in the DB:");

        result = neo4j.getSession().

                run("MATCH(m:Movie)" +
                        " RETURN m.title as title, m.imdbRating as rating" +
                        " ORDER BY rating DESCENDING" +
                        " LIMIT 5");

        System.out.println("Sc | Title");
        System.out.println("------------------------------------");
        while (result.hasNext()) {
            record = result.next();
            System.out.println(record.get("rating") + " | " + record.get("title").toString());
        }

        System.out.println("\nList Episodes of a Series, ordering by season and episode:");
        result = neo4j.getSession().

                run("MATCH p = (n:Episode)-[r:" +
                        " BELONGS_TO]->(m:Series)" +
                        " WHERE m.name = 'tt2802850'" +
                        " RETURN n.title as title, r.season as season, r.episode as episode" +
                        " ORDER BY season, episode");

        System.out.println("S | E | Title");
        System.out.println("------------------------------------");
        while (result.hasNext()) {
            record = result.next();
            System.out.println(record.get("season") + " | " + record.get("episode") + " | " + record.get("title").toString());
        }

        System.out.println("\nShow titles by age rating:");
        result = neo4j.getSession().

                run("MATCH p=(n)-[r:RATED]->(m:Movie)" +
                        " RETURN n.name as ageRating, m.title as title" +
                        " ORDER BY ageRating, m.year");

        System.out.println("AgeR\t| Title");
        System.out.println("------------------------------------");
        while (result.hasNext()) {
            record = result.next();

            if (record.get("ageRating").toString().length() < 4) {
                System.out.println(record.get("ageRating") + "\t\t| " + record.get("title").toString());
            } else {
                System.out.println(record.get("ageRating") + "\t| " + record.get("title").toString());
            }

        }

        /* Closes all connections */
        System.out.println();
        neo4j.closeSession();
    }
}

