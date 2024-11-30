SELECT
    songs.song_id AS mediaId,
    songs.media_locator AS mediaLocator,
    songs.media_type AS mediaSourceType,
    songs.name AS trackName,
    songs.length_seconds AS trackLengthSeconds,
    songs.track_number AS trackNumber,
    songs.lyrics AS lyrics,
    songs.embedding AS embedding,
    songs.artist_id as artistId,
    artists.name AS artistName,
    albums.name AS albumName,
    albums.img_locator AS albumImage,
    songs.album_id AS albumId,
    genres.name AS genreName
    songs.genre_id AS genreId

FROM songs
LEFT JOIN artists ON songs.artist_id = artists.artist_id
LEFT JOIN albums ON songs.album_id = albums.album_id
LEFT JOIN genres ON songs.genre_id = genres.genre_id
{{where_clause}} ORDER BY {{order}} LIMIT {{limit}};