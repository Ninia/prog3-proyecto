// Movies
CREATE (tt0117951:Movie {
  Title:      'Trainspotting',
  imdbID:     'tt0117951',
  Year:       '1996',
  Released:   'Fri Aug 09 00:00:00 CEST 1996',
  DVD:        'Tue Mar 24 00:00:00 CET 1998',
  Plot:       'Renton, deeply immersed in the Edinburgh drug scene, tries to clean up and get out, despite the allure of the drugs and influence of friends.',
  Rated:      'R',
  Awards:     'Nominated for 1 Oscar. Another 20 wins & 26 nominations.',
  BoxOffice:  'N/A',
  Metascore:  '83',
  imdbRating: '8.2',
  imdbVotes:  '501,336',
  Runtime:    '94 min',
  Website:    'http://www.miramax.com/movie/trainspotting/',
  Poster:     'https://images-na.ssl-images-amazon.com/images/M/MV5BMzA5Zjc3ZTMtMmU5YS00YTMwLWI4MWUtYTU0YTVmNjVmODZhXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_SX300.jpg'
})

CREATE (tt0289043:Movie {
  Title:      '28 Days Later...',
  imdbID:     'tt0289043',
  Year:       '2002',
  Released:   'Fri Jun 27 00:00:00 CEST 2003',
  DVD:        'Tue Oct 21 00:00:00 CEST 2003',
  Plot:       "Animal activists invade a laboratory with the intention of releasing chimpanzees that are undergoing experimentation, infected by a virus -a virus that causes rage. The naive activists ignore the pleas of a scientist to keep the cages locked, with disastrous results. Twenty-eight days later, our protagonist, Jim, wakes up from a coma, alone, in an abandoned hospital. He begins to seek out anyone else to find London is deserted, apparently without a living soul. After finding a church, which had become inhabited by zombie like humans intent on his demise, he runs for his life. Selena and Mark rescue him from the horde and bring him up to date on the mass carnage and horror as all of London tore itself apart. This is a tale of survival and ultimately, heroics, with nice subtext about mankind's savage nature.",
  Rated:      'R',
  Awards:     '10 wins & 29 nominations.',
  BoxOffice:  '$44,900,469.00',
  Metascore:  '73',
  imdbRating: '7.6',
  imdbVotes:  '310,278',
  Runtime:    '113 min',
  Website:    'http://www.28dayslaterthemovie.co.uk/main.html',
  Poster:     'https://images-na.ssl-images-amazon.com/images/M/MV5BMTk3NjEwNDMzMF5BMl5BanBnXkFtZTgwMzgwNDgwNzE@._V1_SX300.jpg'
})

CREATE (tt:Movie {
  Title:      '',
  imdbID:     '',
  Year:       '',
  Released:   '',
  DVD:        '',
  Plot:       '',
  Language:   '',
  Rated:      '',
  Awards:     '',
  BoxOffice:  '',
  Metascore:  '',
  imdbRating: '',
  imdbVotes:  '',
  Runtime:    '',
  Website:    '',
  Poster:     ''
})

// People
CREATE (John_Hodge:Person {name: 'John Hodge'})
CREATE (Irvine_Welsh:Person {name: 'Irvine Welsh'})
CREATE (Ewan_McGregor:Person {name: 'Ewan McGregor'})
CREATE (Ewen_Bremne:Person {name: 'Ewen Bremne'})
CREATE (Jonny_Lee_Miller:Person {name: 'Jonny Lee Miller'})
CREATE (Kevin_McKidd:Person {name: 'Kevin McKidd'})
CREATE (Danny_Boyle:Person {name: 'Danny Boyle'})
CREATE (Alex_Palmer:Person {name: 'Alex Palmer'})
CREATE (Bindu_De_Stoppani:Person {name: 'Alex Bindu De Stoppani'})
CREATE (Jukka_Hiltunen:Person {name: 'Jukka Hiltunen'})
CREATE (David_Schneider:Person {name: 'David_Schneider'})
CREATE (Alex_Garland:Person {name: 'Alex Garland'})

//Producers
CREATE (Miramax_Films:Producer {name: 'Miramax Films'})
CREATE (Fox_Searchlight_Pictures:Producer {name: 'Fox Searchlight Pictures'})

// Score Outlets
CREATE (IMDB:ScoreOutlet {name: 'Internet Movie Database'})
CREATE (Rotten_Tomatoes:ScoreOutlet {name: 'Rotten Tomatoes'})
CREATE (Metacritic:ScoreOutlet {name: 'Metacritic'})

// Countries
CREATE (UK:Country {name: 'UK'})

// Genres
CREATE (Drama:Genre {name: 'Drama'})
CREATE (Horror:Genre {name: 'Horror'})
CREATE (SciFi:Genre {name: 'Sci-Fi'})

// Languages
CREATE (English:Language {name: 'English'})
CREATE (Spanish:Language {name: 'Spanish'})

// Trainspotting"s Relations
CREATE
  (John_Hodge)-[:WROTE]->(tt0117951),
  (Irvine_Welsh)-[:WROTE]->(tt0117951),
  (Ewan_McGregor)-[:ACTED_IN]->(tt0117951),
  (Ewen_Bremne)-[:ACTED_IN]->(tt0117951),
  (Jonny_Lee_Miller)-[:ACTED_IN]->(tt0117951),
  (Kevin_McKidd)-[:ACTED_IN]->(tt0117951),
  (Danny_Boyle)-[:DIRECTED]->(tt0117951),
  (Miramax_Films)-[:PRODUCED]->(tt0117951),
  (IMDB)-[:SCORED {score: 82}]->(tt0117951),
  (Rotten_Tomatoes)-[:SCORED {score: 90}]->(tt0117951),
  (Metacritic)-[:SCORED {score: 83}]->(tt0117951),
  (UK)-[:COUNTRY]->(tt0117951),
  (Drama)-[:GENRE]->(tt0117951),
  (English)-[:LANGUAGE]->(tt0117951)

// 28 Days Later"s Relations
CREATE
  (Alex_Palmer)-[:ACTED_IN]->(tt0289043),
  (Bindu_De_Stoppani)-[:ACTED_IN]->(tt0289043),
  (Jukka_Hiltunen)-[:ACTED_IN]->(tt0289043),
  (David_Schneider)-[:ACTED_IN]->(tt0289043),
  (Danny_Boyle)-[:DIRECTED]->(tt0289043),
  (Fox_Searchlight_Pictures)-[:PRODUCED]->(tt0289043),
  (IMDB)-[:SCORED {score: 76}]->(tt0289043),
  (Rotten_Tomatoes)-[:SCORED {score: 87}]->(tt0289043),
  (Metacritic)-[:SCORED {score: 73}]->(tt0289043),
  (UK)-[:COUNTRY]->(tt0289043),
  (Drama)-[:GENRE]->(tt0289043),
  (Horror)-[:GENRE]->(tt0289043),
  (SciFi)-[:GENRE]->(tt0289043),
  (English)-[:LANGUAGE]->(tt0289043),
  (Spanish)-[:LANGUAGE]->(tt0289043)

;