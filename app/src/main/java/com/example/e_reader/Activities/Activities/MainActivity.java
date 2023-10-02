package com.example.e_reader.Activities.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.e_reader.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{
    private BottomNavigationView bottomNavigationView;
    private SearchFragment searchFragment = new SearchFragment();
    private HomeFragment homeFragment = new HomeFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();
    private final int STORAGE_PERMISSION_CODE = 101;
    public static boolean hasPermissions = false; // I am not actually sure if this is the most optimal

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Change the way the app theme is set, because this is just being lazy
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_main);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        this.bottomNavigationView = findViewById(R.id.bottomNavigationView);

        this.bottomNavigationView
                .setOnItemSelectedListener(this);
        this.bottomNavigationView.setSelectedItemId(R.id.item_2);

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, this.searchFragment)
                        .commit();
                return true;

            case R.id.item_2:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, this.homeFragment)
                        .commit();
                return true;

            case R.id.item_3:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, this.settingsFragment)
                        .commit();
                return true;
        }
        return false;
    }

    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
            hasPermissions = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == this.STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasPermissions = true;
            }
            else {
                hasPermissions = false;
                Toast.makeText(MainActivity.this, "Please enable storage permissions in the settings to continue", Toast.LENGTH_SHORT).show();
            }
        }
    }
}