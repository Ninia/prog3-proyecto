/* List titles where a certain person acted, ordering them by year */
MATCH p = (n:Person)-[r:ACTED_IN]->(m)
  WHERE n.name = 'Ewan McGregor'
RETURN m.title, m.year
  ORDER BY m.year;

/* List 5 best Movies in the DB acording to Rotten Tomatoes */
MATCH p = (n:ScoreOutlet)-[r:SCORED]->(m:Movie)
  WHERE n.name = 'Rotten Tomatoes'
RETURN m.title, r.score
  ORDER BY r.score DESCENDING
  LIMIT 5;

/* List movies produced by a Producer */
MATCH p = (n:Producer)-[r:PRODUCED]->(m:Movie)
AND a.name = 'Internet Movie Database'

RETURN m.title, m.year
  ORDER BY m.year DESCENDING;

/* List languages and the number of title they are spoken in, ordering them by the number of title */
MATCH (a:Language)-[b:SPOKEN_LANGUAGE]->(c)
RETURN a.name, count(c)
  ORDER BY count(c) DESCENDING;

/* List movies of a Language, ordering by year */
MATCH p = (n:Language)-[r:SPOKEN_LANGUAGE]->(m:Movie)
  WHERE n.name = 'English'
RETURN m.title, m.imdbRating, m.year
  ORDER BY m.year;

/* List Episodes of a Series, ordering by season and episode: */
MATCH p = (n:Episode)-[r:BELONGS_TO]->(m:Series)
  WHERE m.name = 'tt2802850'
RETURN n.title, r.season, r.episode
  ORDER BY r.season, r.episode;

/* Show Titles scored by a ScoreOutlet */
MATCH p = (n:ScoreOutlet)-[r:SCORED]->(m:Movie)
  WHERE n.name = 'Internet Movie Database'
RETURN m.title, r.score, m.year
  ORDER BY m.year;

/* Show Titles by Age Rating */
MATCH p = (n)-[r:RATED]->(m:Movie)
RETURN n.name, m.name
  ORDER BY n.name, m.year;

/* Show Titles that belong to a list */
MATCH p = (n:List)-[r:CONTAINS]-(m)
  WHERE n.name = 'Star Wars Saga'
RETURN m.title, m.year
  ORDER BY m.year;
