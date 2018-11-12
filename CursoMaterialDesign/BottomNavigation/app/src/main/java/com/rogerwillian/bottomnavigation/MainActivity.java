package com.rogerwillian.bottomnavigation;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_fav:
                Toast.makeText(this, "Favoritos", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_loc:
                Toast.makeText(this, "Localização", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_track:
                Toast.makeText(this, "Caminho", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }
}
