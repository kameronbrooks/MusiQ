package com.g2.musiq;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.g2.musiq.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    public AppSettings settings;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    protected void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults );

    }

    protected void onInitializeApp() {
        settings = AppSettings.loadAppSettings(this);

        requestPermissions();

        if(settings == null) {
            settings = new AppSettings();
            settings.id = (int)(Math.random()*Integer.MAX_VALUE);
            settings.user = "User";
            settings.indexingInterval = 30000;

            AppSettings.SaveAppSettings(this, settings);
        }

        TrackIndexer indexer = new TrackIndexer();
        indexer.indexFileSystem(this);

        Player.createInstance();



    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup the app if needed
        onInitializeApp();

    }

}