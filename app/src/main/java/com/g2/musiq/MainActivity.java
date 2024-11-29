package com.g2.musiq;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Debug;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.g2.musiq.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    public AppSettings settings;

    protected void onInitializeApp() {
        settings = AppSettings.loadAppSettings(this);

        if(settings == null) {
            settings = new AppSettings();
            settings.id = (int)(Math.random()*Integer.MAX_VALUE);
            settings.user = "User";
            settings.indexingInterval = 30000;

            AppSettings.SaveAppSettings(this, settings);
        }


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup the app if needed
        onInitializeApp();

    }

}