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
import java.util.ArrayList;
import java.util.List;

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

    public long insertTrack(SQLiteDatabase db, TrackInfo trackInfo) {


        // Create a ContentValues object to hold the column values
        ContentValues values = new ContentValues();
        values.put("song_id", trackInfo.mediaId); // song_id
        values.put("media_locator", trackInfo.mediaLocator); // media_locator
        values.put("media_type", trackInfo.mediaSourceType); // media_type
        values.put("name", trackInfo.trackName); // name
        values.put("length_seconds", trackInfo.trackLengthSeconds); // length_seconds

        // Nested objects
        values.put("artist_id", trackInfo.artist != null ? trackInfo.artist.id : null); // artist_id
        values.put("album_id", trackInfo.album != null ? trackInfo.album.id : null); // album_id

        // Genre and other fields
        values.put("genre_id", trackInfo.genreId); // genre_id
        values.put("embedding", convertFloatArrayToBlob(trackInfo.embedding)); // embedding
        values.put("track_number", trackInfo.trackNumber); // track_number
        values.put("lyrics", trackInfo.lyrics); // lyrics
        values.put("user_rating", trackInfo.userRating); // user_rating
        values.put("uid", trackInfo.userRating);

        // Insert the values into the "songs" table
        //long newRowId = db.insert("songs", null, values);
        long newRowId = db.insertWithOnConflict("songs", null, values, SQLiteDatabase.CONFLICT_IGNORE);

        // Return the row ID of the new record, or -1 if there was an error
        return newRowId;
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

    public long insertAlbum(SQLiteDatabase db, Album album) {
        // Create a ContentValues object to hold the column values
        ContentValues values = new ContentValues();
        values.put("album_id", album.id); // album_id
        values.put("artist_id", album.artist.id); // artist_id
        values.put("description", album.description); // description
        values.put("img_locator", album.imageLocator); // img_locator
        values.put("name", album.name); // name
        values.put("release_date", album.releaseDate.toString()); // release_date (ensure correct format)

        // Insert the values into the "albums" table
        long newRowId = db.insert("albums", null, values);

        // Return the row ID of the new record, or -1 if there was an error
        return newRowId;
    }

    public long insertArtist(SQLiteDatabase db, Artist artist) {
        // Create a ContentValues object to hold the column values
        ContentValues values = new ContentValues();
        values.put("artist_id", artist.id); // artist_id
        values.put("description", artist.description); // description
        values.put("img_locator", artist.imageLocator); // img_locator
        values.put("name", artist.name); // name
        values.put("release_date", artist.startDate.toString()); // release_date (ensure correct format)
        values.put("primary_genre", artist.primaryGenreId); // primary_genre

        // Insert the values into the "artists" table
        long newRowId = db.insert("artists", null, values);

        // Return the row ID of the new record, or -1 if there was an error
        return newRowId;
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

    public TrackInfo[] getTracks() {
        String template = loadSQLFromResource(R.raw.select_tracks);
        String sql = template
                .replace("{{where_clause}}", "")
                .replace("{{limit}}", "10000")
                .replace("{{order}}", "songs.name ASC");

        // Execute the query
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor == null) {
            return new TrackInfo[0];
        }

        List<TrackInfo> list = new ArrayList<TrackInfo>();
        while(cursor.moveToNext()) {
            TrackInfo track = readTrackInfoFromCursor(cursor);
            list.add(track);
        }

        return list.toArray(new TrackInfo[0]);
    }

    public Playlist[] getPlaylists() {
        // TODO
        return null;
    }

    public Playlist getPlaylistById(int id) {
        return null;
    }

    public void addTrack(TrackInfo track) {
        insertTrack(db, track);
    }
    public void removeTrack(TrackInfo track) {
        db.delete("songs", "song_id=?", new String[]{String.valueOf(track.mediaId)} );
    }

    public void addPlaylist(Playlist playlist) {
        ContentValues values = new ContentValues();
        values.put("name", playlist.name);
        values.put("description", playlist.description);
        values.put("created_date", playlist.createdDate.toString());
        values.put("user_rating", playlist.userRating);

        long playlistId = db.insert("playlists", null, values);

        if (playlist.tracks.isEmpty()) {
            return;
        }

        // Save the songs
        for (int i = 0; i < playlist.tracks.size(); i++) {
            ContentValues values2 = new ContentValues();
            values2.put("playlist_id", playlistId);
            values2.put("song_id", playlist.tracks.get(i).mediaId);
            values2.put("playlist_index", i);

            db.insertWithOnConflict("playlist_songs", null, values2, SQLiteDatabase.CONFLICT_IGNORE);
        }

    }

    public void updatePlaylist(Playlist playlist) {
        // Delete the existing mappings
        db.delete("playlist_songs", "playlist_id=?", new String[]{String.valueOf(playlist.id)});

        // Save the new ones
        for (int i = 0; i < playlist.tracks.size(); i++) {
            ContentValues values2 = new ContentValues();
            values2.put("playlist_id", playlist.id);
            values2.put("song_id", playlist.tracks.get(i).mediaId);
            values2.put("playlist_index", i);

            db.insertWithOnConflict("playlist_songs", null, values2, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }




}
