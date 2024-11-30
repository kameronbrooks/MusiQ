package com.g2.musiq;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseInterface extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "musiq_lib.db";
    private static final int DATABASE_VERSION = 3;

    private Context context;

    public DatabaseInterface(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        loadSQLFile(db, R.raw.create_db);
    }

    private void loadSQLFile(SQLiteDatabase db, int resourceId) {
        try {
            // Open the resource file
            InputStream is = context.getResources().openRawResource(resourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            StringBuilder sql = new StringBuilder();
            String line;

            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                sql.append(line);
                if (line.trim().endsWith(";")) { // Execute each statement when a semicolon is encountered
                    Log.d("Musiq", "Executing SQL: "+sql.toString());
                    db.execSQL(sql.toString());
                    sql.setLength(0); // Clear the StringBuilder
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Genre genre = new Genre();
        //genre.name = "Unknown";
        //genre.description = "The default Genre if it is unknown";
        //genre.embedding = new float[512];
        //lib.addGenre(genre);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade logic
        db.execSQL("DROP TABLE IF EXISTS genres");
        db.execSQL("DROP TABLE IF EXISTS artists");
        db.execSQL("DROP TABLE IF EXISTS albums");
        db.execSQL("DROP TABLE IF EXISTS songs");
        db.execSQL("DROP TABLE IF EXISTS song_interactions");
        db.execSQL("DROP TABLE IF EXISTS playlists");
        db.execSQL("DROP TABLE IF EXISTS playlist_songs");
        onCreate(db);
    }
}
