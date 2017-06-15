package ud.binmonkey.prog3_proyecto_server.neo4j;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import ud.binmonkey.prog3_proyecto_server.omdb.Omdb;
import ud.binmonkey.prog3_proyecto_server.omdb.OmdbEpisode;
import ud.binmonkey.prog3_proyecto_server.omdb.OmdbMovie;

public class Neo4jIntegrationTest {

    private static void generateDB(Neo4jUtils neo4j) {

        /* Generate Titles */
        OmdbMovie trainspotting = new OmdbMovie(Omdb.getTitle("tt0117951"));
        trainspotting.setFilename(trainspotting.getTitle() + "(" + trainspotting.getYear() + ").mp4");
        trainspotting.setLanguage("EN");

        OmdbMovie daysLater = new OmdbMovie(Omdb.getTitle("tt0289043"));
        daysLater.setFilename(daysLater.getTitle() + "(" + daysLater.getYear() + ").mp4");
        daysLater.setLanguage("EN");

        OmdbMovie exMachina = new OmdbMovie(Omdb.getTitle("tt0470752"));
        exMachina.setFilename(exMachina.getTitle() + "(" + exMachina.getYear() + ").mp4");
        exMachina.setLanguage("EN");

        OmdbMovie fightClub = new OmdbMovie(Omdb.getTitle("tt0137523"));
        fightClub.setFilename(fightClub.getTitle() + "(" + fightClub.getYear() + ").mp4");
        fightClub.setLanguage("EN");

        OmdbMovie goodbyeLenin = new OmdbMovie(Omdb.getTitle("tt0301357"));
        goodbyeLenin.setFilename(goodbyeLenin.getTitle() + "(" + goodbyeLenin.getYear() + ").mp4");
        goodbyeLenin.setLanguage("EN");

        OmdbMovie lord1 = new OmdbMovie(Omdb.getTitle("tt0120737"));
        lord1.setFilename(lord1.getTitle() + "(" + lord1.getYear() + ").mp4");
        lord1.setLanguage("EN");

        OmdbMovie lord2 = new OmdbMovie(Omdb.getTitle("tt0167261"));
        lord2.setFilename(lord2.getTitle() + "(" + lord2.getYear() + ").mp4");
        lord2.setLanguage("EN");

        OmdbMovie lord3 = new OmdbMovie(Omdb.getTitle("tt0167260"));
        lord3.setFilename(lord3.getTitle() + "(" + lord3.getYear() + ").mp4");
        lord3.setLanguage("EN");

        OmdbMovie star1 = new OmdbMovie(Omdb.getTitle("tt0121765"));
        star1.setFilename(star1.getTitle() + "(" + star1.getYear() + ").mp4");
        star1.setLanguage("EN");

        OmdbMovie star2 = new OmdbMovie(Omdb.getTitle("tt2488496"));
        star2.setFilename(star2.getTitle() + "(" + star2.getYear() + ").mp4");
        star2.setLanguage("EN");

        OmdbMovie star3 = new OmdbMovie(Omdb.getTitle("tt0076759"));
        star3.setFilename(star3.getTitle() + "(" + star3.getYear() + ").mp4");
        star3.setLanguage("EN");

        OmdbMovie star4 = new OmdbMovie(Omdb.getTitle("tt0080684"));
        star4.setFilename(star4.getTitle() + "(" + star4.getYear() + ").mp4");
        star4.setLanguage("EN");

        OmdbMovie star5 = new OmdbMovie(Omdb.getTitle("tt0086190"));
        star5.setFilename(star5.getTitle() + "(" + star5.getYear() + ").mp4");
        star5.setLanguage("EN");

        OmdbMovie star6 = new OmdbMovie(Omdb.getTitle("tt0121766"));
        star6.setFilename(star6.getTitle() + "(" + star6.getYear() + ").mp4");
        star6.setLanguage("EN");

        OmdbMovie star7 = new OmdbMovie(Omdb.getTitle("tt0080684"));
        star7.setFilename(star7.getTitle() + "(" + star7.getYear() + ").mp4");
        star7.setLanguage("EN");

        OmdbEpisode fargo1 = new OmdbEpisode(Omdb.getTitle("tt3097534"));
        fargo1.setFilename(fargo1.getTitle() + "(" + fargo1.getYear() + ").mp4");
        fargo1.setLanguage("EN");

        OmdbEpisode fargo2 = new OmdbEpisode(Omdb.getTitle("tt3296848"));
        fargo2.setFilename(fargo2.getTitle() + "(" + fargo2.getYear() + ").mp4");
        fargo2.setLanguage("EN");

        OmdbEpisode fargo3 = new OmdbEpisode(Omdb.getTitle("tt3519062"));
        fargo3.setFilename(fargo3.getTitle() + "(" + fargo3.getYear() + ").mp4");
        fargo3.setLanguage("EN");

        OmdbEpisode fargo4 = new OmdbEpisode(Omdb.getTitle("tt3578722"));
        fargo4.setFilename(fargo4.getTitle() + "(" + fargo4.getYear() + ").mp4");
        fargo4.setLanguage("EN");

        OmdbEpisode fargo5 = new OmdbEpisode(Omdb.getTitle("tt3514096"));
        fargo5.setFilename(fargo5.getTitle() + "(" + fargo5.getYear() + ").mp4");
        fargo5.setLanguage("EN");

        /* Adding Movies */
        neo4j.addTitle(trainspotting); /* Trainspotting */
        neo4j.addTitle(daysLater); /* 28 Days Later... */
        neo4j.addTitle(exMachina); /* Ex Machina */
        neo4j.addTitle(fightClub); /* Fight Club */
        neo4j.addTitle(goodbyeLenin); /* Goodbye Lenin */

        /* LOTR Movies */
        neo4j.addList("Lord of The Rings Saga", lord1, lord2, lord3);

        /* Star Wars Movies */
        neo4j.addList("Star Wars Saga", star1, star2, star3, star4, star5, star6, star7);

        /* Adding Episodes */
        neo4j.addTitle(fargo1);
        neo4j.addTitle(fargo2);
        neo4j.addTitle(fargo3);
        neo4j.addTitle(fargo4);
        neo4j.addTitle(fargo5);

        /* Fixes duplicate because of different name */
        neo4j.renameNode("Twentieth Century Fox", "20th Century Fox", "Producer");
    }

    public static void main(String[] args) {

        Neo4jUtils neo4j = new Neo4jUtils();
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

