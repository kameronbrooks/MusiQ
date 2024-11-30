package com.g2.musiq;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TrackIndexer {
    HashMap<String, Artist> _artistMap;
    HashMap<String, Album> _albumMap;

    public TrackIndexer() {
        _artistMap = new HashMap<String,Artist>();
        _albumMap = new HashMap<String, Album>();
    }

    public static File getMusicDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
    }

    private static boolean isMusicFile(File file) {
        String[] musicExtensions = {".mp3", ".wav", ".flac", ".m4a", ".aac", ".ogg"};
        String fileName = file.getName().toLowerCase();
        for (String extension : musicExtensions) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
    protected int searchDirectory(File directory, List<String> list) {
        Log.d("TrackIndexer", directory.getAbsolutePath());
        if (directory == null || !directory.isDirectory()) {
            return 0;
        }

        File[] files = directory.listFiles();
        Log.d("Track Indexer", directory + " -> Files found " + files.length);
        if (files == null) {
            return 0;
        }

        int filesFound = 0;
        for(File f : files) {
            Log.d("Track Indexer", "File " + f.getAbsolutePath());
            if (f.isDirectory()) {
                filesFound += searchDirectory(f, list);
            }
            else {

                if(isMusicFile(f)) {
                    filesFound += 1;
                    list.add(f.getAbsolutePath());
                }
            }
        }
        return filesFound;
    }

    public TrackIndexingResult indexFileSystem(AppCompatActivity activity) {
        TrackIndexingResult indexingResult = new TrackIndexingResult();

        ContentResolver resolver = activity.getContentResolver();
        Uri audioURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.RELATIVE_PATH,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TRACK
        };

        Cursor cursor = resolver.query(audioURI, projection, null, null, null);

        if (cursor == null) {
            return null;
        }
        while(cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            String relativePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.RELATIVE_PATH));
            String albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String artistName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String trackNumber = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));

            TrackInfo info = new TrackInfo();
            info.trackName = title;
            info.mediaLocator = relativePath;
            info.mediaSourceType = "local";
            // Handle track number
            try {
                info.trackNumber = Integer.parseInt(trackNumber);
            }
            catch (Exception e) {
                info.trackNumber = -1;
            }
            // Find the artist if already encountered, or create a new one
            Artist artist = null;
            String artistNameLower = artistName.toLowerCase();

            if(_artistMap.containsKey(artistNameLower)) {
                artist = _artistMap.get(artistNameLower);
            }
            else {
                artist = new Artist();
                artist.name = artistName;
                _artistMap.put(artistNameLower, artist);

            }

            // Find the album if already encountered, or create a new one
            Album album = null;
            String albumNameLower = artistNameLower + " => " + albumName.toLowerCase();

            if(_albumMap.containsKey(albumNameLower)) {
                album = _albumMap.get(albumNameLower);
            }
            else {
                album = new Album();
                album.name = albumName;
                _albumMap.put(albumNameLower, album);

            }

            info.album = album;
            info.artist = artist;

            indexingResult.AddItem(info);

        }

        return indexingResult;
    }

    public TrackIndexingResult indexStreamingServices() {
        // TODO
        return null;
    }

    public TrackIndexingResult indexStreamingService() {
        // TODO
        return null;
    }
}
