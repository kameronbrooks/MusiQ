PRAGMA foreign_keys = ON;


CREATE TABLE genres (
  genre_id INTEGER PRIMARY KEY NOT NULL,
  description TEXT,
  name TEXT UNIQUE,
  embedding BLOB
);


CREATE TABLE artists (
  artist_id INTEGER PRIMARY KEY NOT NULL,
  description TEXT,
  img_locator TEXT,
  name TEXT,
  release_date DATE,
  primary_genre INTEGER NOT NULL,
  FOREIGN KEY (primary_genre) REFERENCES Genres(genre_id)
);


CREATE TABLE albums (
  album_id INTEGER PRIMARY KEY NOT NULL,
  artist_id INTEGER NOT NULL,
  description TEXT,
  name TEXT,
  img_locator TEXT,
  release_date DATE,
  FOREIGN KEY (artist_id) REFERENCES Artists(artist_id)
);


CREATE TABLE songs (
  song_id INTEGER PRIMARY KEY NOT NULL,
  media_type TEXT NOT NULL,
  media_locator TEXT NOT NULL,
  length_seconds REAL NOT NULL,
  name TEXT NOT NULL,
  artist_id INTEGER NOT NULL,
  album_id INTEGER NOT NULL,
  genre_id INTEGER NOT NULL,
  uid TEXT UNIQUE,
  embedding BLOB,
  track_number INTEGER,
  lyrics TEXT,
  user_rating INTEGER NOT NULL,
  FOREIGN KEY (artist_id) REFERENCES Artists(artist_id),
  FOREIGN KEY (album_id) REFERENCES Albums(album_id),
  FOREIGN KEY (genre_id) REFERENCES Genres(genre_id)
);


CREATE TABLE song_interactions (
  interaction_id INTEGER PRIMARY KEY NOT NULL,
  song_id INTEGER NOT NULL,
  event_date DATETIME NOT NULL,
  seconds_listened REAL NOT NULL,
  completion_percent REAL NOT NULL,
  FOREIGN KEY (song_id) REFERENCES Songs(song_id)
);


CREATE TABLE playlists (
  playlist_id INTEGER PRIMARY KEY NOT NULL,
  description TEXT,
  name TEXT UNIQUE NOT NULL,
  created_date DATETIME NOT NULL,
  user_rating INTEGER NOT NULL
);


CREATE TABLE playlist_songs (
  playlist_id INTEGER NOT NULL,
  song_id INTEGER NOT NULL,
  playlist_index INTEGER NOT NULL,
  PRIMARY KEY (playlist_id, song_id),
  FOREIGN KEY (playlist_id) REFERENCES Playlists(playlist_id),
  FOREIGN KEY (song_id) REFERENCES Songs(song_id)
);
