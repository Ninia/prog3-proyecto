/* List titles where a certain person acted, ordering them by rating */
MATCH p = (n:Person)-[r:ACTED_IN]->(m)
  WHERE n.name = 'Ewan McGregor'
RETURN m.title, m.imdbRating
  ORDER BY m.imdbRating DESCENDING;

/* List 5 best Movies in the DB */
MATCH (m:Movie)
RETURN m.title, m.imdbRating
  ORDER BY m.imdbRating DESCENDING
  LIMIT 5;

/* List movies produced by a Producer, ordering them by year and rating */
MATCH p = (n:Producer)-[r:PRODUCED]->(m:Movie)
  WHERE n.name = '20th Century Fox'
RETURN m.title, m.imdbRating, m.year
  ORDER BY m.year, m.imdbRating DESCENDING;

/* List languages and the number of title they are spoken in, ordering them by the number of title */
MATCH (a:Language)-[b:SPOKEN_LANGUAGE]->(c)
RETURN a.name, count(c)
  ORDER BY count(c) DESCENDING

/* List movies of a Language, ordering by year */
MATCH p = (n:Language)-[r:SPOKEN_LANGUAGE]->(m:Movie)
  WHERE n.name = 'English'
RETURN m.title, m.imdbRating, m.year
  ORDER BY m.year;