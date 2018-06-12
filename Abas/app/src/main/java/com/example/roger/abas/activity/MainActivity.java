package com.example.roger.abas.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.roger.abas.R;
import com.example.roger.abas.fragment.EmAltaFragment;
import com.example.roger.abas.fragment.HomeFragment;
import com.example.roger.abas.fragment.InscricoesFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SmartTabLayout smartTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        smartTabLayout = findViewById(R.id.viewPagerTab);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Youtube");

        // Configurar abas
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
            getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                .add("Home", HomeFragment.class)
                .add("Inscricoes", InscricoesFragment.class)
                .add("Em alta", EmAltaFragment.class)
                .create()
        );
        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
    }
}
