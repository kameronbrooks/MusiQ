INSERT INTO artists (
    artist_id,
    description,
    img_locator,
    name,
    release_date,
    primary_genre
) VALUES (
    {{artist_id}},
    '{{description}}',
    '{{img_locator}}',
    '{{name}}',
    '{{release_date}}',
    {{primary_genre}}
);
