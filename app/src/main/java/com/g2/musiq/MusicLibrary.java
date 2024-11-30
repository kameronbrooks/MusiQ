package com.g2.musiq;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

public class MusicLibrary {
    protected static MusicLibrary _instance;
    DatabaseInterface dbi;
    public static MusicLibrary getInstance() {
        return _instance;
    }

    public static MusicLibrary createInstance(Context ctx) {
        _instance = new MusicLibrary(ctx);
        return _instance;
    }

    protected MusicLibrary(Context ctx) {
         dbi = new DatabaseInterface(ctx);
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

    public String[] getGenres() {
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
