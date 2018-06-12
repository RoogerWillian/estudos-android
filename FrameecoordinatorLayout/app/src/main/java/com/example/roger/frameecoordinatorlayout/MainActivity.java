package com.example.roger.frameecoordinatorlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.frameCarregando);
        /*    frameLayout.setVisibility(View.GONE);*/
    }

    /*    public void abrir(View view) {
        frameLayout.setVisibility(View.VISIBLE);
    }*/
}
