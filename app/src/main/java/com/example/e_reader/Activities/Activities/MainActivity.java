package com.example.e_reader.Activities.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.e_reader.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{
    BottomNavigationView bottomNavigationView;

    SearchFragment searchFragment = new SearchFragment();
    HomeFragment homeFragment = new HomeFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

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
}