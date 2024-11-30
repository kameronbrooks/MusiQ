INSERT INTO songs (
    song_id,
    media_locator,
    media_type,
    name,
    length_seconds,
    artist_id,
    album_id,
    genre_id,
    embedding,
    track_number,
    lyrics,
    user_rating
) VALUES (
    {{song_id}},
    '{{media_locator}}',
    '{{media_type}}',
    '{{name}}',
    {{length_seconds}},
    {{artist_id}},
    {{album_id}},
    '{{genre_id}}',
    {{embedding}},
    {{track_number}},
    '{{lyrics}}',
    {{user_rating}}
);
