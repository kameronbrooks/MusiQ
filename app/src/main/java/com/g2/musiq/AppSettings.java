package com.g2.musiq;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AppSettings {
    public String user;
    public int id;
    public int indexingInterval;

    public static AppSettings loadAppSettings(Context ctx) {
        String filename = "appsettings.json";

        File file = new File(ctx.getFilesDir(), filename);
        AppSettings output = null;

        // If the file does not exit then the app has not been setup
        if (!file.exists()) {
            return null;
        }

        try (
                FileInputStream stream = ctx.openFileInput(filename);
                InputStreamReader reader = new InputStreamReader(stream)) {
            Gson gson = new Gson();
            output = gson.fromJson(reader, AppSettings.class);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

    public static void SaveAppSettings(Context ctx, AppSettings settings) {
        String filename = "appsettings.json";

        File file = new File(ctx.getFilesDir(), filename);
        AppSettings output = null;


        Log.d("FilePath", "File path: " + file.getAbsolutePath());


        try (
                FileOutputStream stream = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
                OutputStreamWriter writer = new OutputStreamWriter(stream)) {
            Gson gson = new Gson();
            String json = gson.toJson(settings);

            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
