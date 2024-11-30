package com.g2.musiq;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

public class MusicLibrary {
    protected static MusicLibrary _instance;
    DatabaseInterface dbi;
    SQLiteDatabase db;
    Context ctx;
    public static MusicLibrary getInstance() {
        return _instance;
    }

    public static MusicLibrary createInstance(Context ctx) {
        _instance = new MusicLibrary(ctx);
        return _instance;
    }

    protected MusicLibrary(Context ctx) {
         dbi = new DatabaseInterface(ctx);
         db = dbi.getWritableDatabase();
         this.ctx = ctx.getApplicationContext();
    }

    private String loadSQLFromResource(int resourceId) {
        try {
            // Load the SQL template from the raw resource file
            InputStream is = ctx.getResources().openRawResource(resourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sqlBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sqlBuilder.append(line).append("\n");
            }
            reader.close();

            return sqlBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String escapeString(String input) {
        if (input==null) {
            return null;
        }
        return input.replace("'","''");
    }
    private static String byteArrayToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder("X'");
        for (byte b: bytes) {
            builder.append(String.format("%02X", b));
        }
        builder.append("'");
        return builder.toString();
    }

    private String generateInsertTrackSQL(TrackInfo trackInfo) {
        String template = loadSQLFromResource(R.raw.insert_track);

        String sql = template
                .replace("{{song_id}}", String.valueOf(trackInfo.requestId))
                .replace("{{media_locator}}", escapeString(trackInfo.mediaLocator))
                .replace("{{media_type}}", escapeString(trackInfo.mediaSourceType))
                .replace("{{name}}", escapeString(trackInfo.trackName))
                .replace("{{length_seconds}}", String.valueOf(trackInfo.trackLengthSeconds))
                .replace("{{artist_id}}", trackInfo.artist != null ? String.valueOf(trackInfo.artist.id) : "NULL")
                .replace("{{album_id}}", trackInfo.album != null ? String.valueOf(trackInfo.album.id) : "NULL")
                .replace("{{genre_id}}", String.valueOf(trackInfo.genreId))
                .replace("{{embedding}}", byteArrayToHex(convertFloatArrayToBlob(trackInfo.embedding)))
                .replace("{{track_number}}", String.valueOf(trackInfo.trackNumber))
                .replace("{{lyrics}}", escapeString(trackInfo.lyrics))
                .replace("{{user_rating}}", String.valueOf(trackInfo.userRating));


        return sql;
    }

    private TrackInfo readTrackInfoFromCursor(Cursor cursor) {
        TrackInfo track = new TrackInfo();

        track.mediaId = cursor.getInt(cursor.getColumnIndexOrThrow("mediaId"));
        track.mediaLocator = cursor.getString(cursor.getColumnIndexOrThrow("mediaLocator"));
        track.mediaSourceType = cursor.getString(cursor.getColumnIndexOrThrow("mediaSourceType"));
        track.trackName = cursor.getString(cursor.getColumnIndexOrThrow("trackName"));
        track.trackLengthSeconds = cursor.getFloat(cursor.getColumnIndexOrThrow("trackLengthSeconds"));
        track.trackNumber = cursor.getInt(cursor.getColumnIndexOrThrow("trackNumber"));
        track.lyrics = cursor.getString(cursor.getColumnIndexOrThrow("lyrics"));

        // Optional: Convert embedding blob to float array
        byte[] embeddingBlob = cursor.getBlob(cursor.getColumnIndexOrThrow("embedding"));
        if (embeddingBlob != null) {
            track.embedding = convertBlobToFloatArray(embeddingBlob);
        }

        // Populate nested objects
        track.artist = new Artist();
        track.artist.name = cursor.getString(cursor.getColumnIndexOrThrow("artistName"));

        track.album = new Album();
        track.album.name = cursor.getString(cursor.getColumnIndexOrThrow("albumName"));
        track.album.imageLocator = cursor.getString(cursor.getColumnIndexOrThrow("albumImage"));

        track.genreId = cursor.getInt(cursor.getColumnIndexOrThrow("genreId"));

        return track;
    }

    private String generateInsertAlbumSQL(Album album) {
        String template = loadSQLFromResource(R.raw.insert_album);

        String sql = template
                .replace("{{album_id}}", String.valueOf(album.id))
                .replace("{{artist_id}}", String.valueOf(album.artist.id))
                .replace("{{description}}", escapeString(album.description))
                .replace("{{img_locator}}", escapeString(album.imageLocator))
                .replace("{{name}}", escapeString(album.name))
                .replace("{{relase_date}}", album.releaseDate.toString());


        return sql;
    }

    private String generateInsertArtistSQL(Artist artist) {
        String template = loadSQLFromResource(R.raw.insert_artlst);

        String sql = template
                .replace("{{artist_id}}", String.valueOf(artist.id))
                .replace("{{description}}", escapeString(artist.description))
                .replace("{{img_locator}}", escapeString(artist.imageLocator))
                .replace("{{name}}", escapeString(artist.name))
                .replace("{{release_date}}", escapeString(artist.startDate.toString())) // Ensure date is formatted
                .replace("{{primary_genre}}", String.valueOf(artist.primaryGenreId));


        return sql;
    }

    private float[] convertBlobToFloatArray(byte[] blob) {
        // Check if the blob is null or empty
        if (blob == null || blob.length == 0) {
            return new float[0];
        }

        // Each float is 4 bytes
        int numFloats = blob.length / 4;
        float[] floatArray = new float[numFloats];

        // Use ByteBuffer to convert byte array to float array
        ByteBuffer buffer = ByteBuffer.wrap(blob);
        for (int i = 0; i < numFloats; i++) {
            floatArray[i] = buffer.getFloat();
        }

        return floatArray;
    }

    private byte[] convertFloatArrayToBlob(float[] arr) {
        if (arr == null || arr.length == 0) {
            return new byte[0];
        }

        int numBytes = arr.length * 4;
        ByteBuffer buffer = ByteBuffer.allocate(numBytes);

        for(float val : arr) {
            buffer.putFloat(val);
        }
        return buffer.array();

    }

    public void addGenre(Genre genre) {
        ContentValues values = new ContentValues();
        values.put("name", genre.name);
        values.put("description", genre.description);
        values.put("embedding", convertFloatArrayToBlob(genre.embedding));

        long newId = db.insert("genres", null, values);
        Log.d("Musiq", "Genre added " + newId);

    }

    public QueryResults textQuery(String search, int maxCount) {
        QueryResults results = new QueryResults();

        return results;
    }

    public QueryResults embeddingQuery(float[] embedding, int maxCount) {
        QueryResults results = new QueryResults();

        return results;
    }

    public QueryResults getAlbumTracks(Album album) {
        // TODO
        return null;
    }

    public QueryResults getArtistTracks(Artist artist) {
        // TODO
        return null;
    }

    public Genre[] getGenres() {
        // TODO
        return null;
    }

    public Artist[] getArtists() {
        // TODO
        return null;
    }

    public Album[] getAlbums() {
        // TODO
        return null;
    }

    public Playlist[] getPlaylists() {
        // TODO
        return null;
    }

    public Playlist getPlaylistById(int id) {
        return null;
    }

    public void addTrack(TrackInfo track) {
        // TODO
    }
    public void removeTrack(TrackInfo track) {
        // TODO
    }

    public void addPlaylist(Playlist playlist) {
        // TODO
    }

    public void updatePlaylist(Playlist playlist) {
        // TODO
    }




}
