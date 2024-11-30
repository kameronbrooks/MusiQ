INSERT INTO albums (
    album_id,
    artist_id,
    description,
    name,
    img_locator,
    release_date
) VALUES (
    {{album_id}},
    {{artist_id}},
    '{{description}}',
    '{{name}}',
    '{{img_locator}}',
    '{{release_date}}'
);
