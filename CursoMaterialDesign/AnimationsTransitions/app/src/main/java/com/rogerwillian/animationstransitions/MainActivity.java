package com.rogerwillian.animationstransitions;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.transition.Explode;
import android.support.transition.Scene;
import android.support.transition.Slide;
import android.support.transition.Transition;
import android.support.transition.TransitionInflater;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button button;
    private Scene scene1;
    private Scene scene2;
    private boolean start = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewGroup root = findViewById(R.id.root);

        scene1 = Scene.getSceneForLayout(root, R.layout.activity_main, this);
        scene2 = Scene.getSceneForLayout(root, R.layout.activity_main_2, this);

        imageView = findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent, bundle);
            }
        });

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TransitionManager.beginDelayedTransition(root, new Slide());

                imageView.setVisibility(imageView.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    public void changeScene(View view) {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.custom);
        if (start) {
            TransitionManager.go(scene2, transition);
            start = false;
        } else {
            TransitionManager.go(scene1, transition);
            start = true;
        }
    }
}
