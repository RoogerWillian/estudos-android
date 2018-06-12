package com.example.roger.progressbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ProgressBar progressBarCarregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.progressBar = findViewById(R.id.progressBar);
        this.progressBarCarregando = findViewById(R.id.progressBarCarregando);
        this.progressBarCarregando.setVisibility(View.GONE);
        this.progressBar.setProgress(0);
    }

    public void carregarProgressBar(View view) {

        this.progressBarCarregando.setVisibility(View.VISIBLE);
//        this.progresso += 10;
//        this.progressBar.setProgress(progresso);


        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    final int progresso = i;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progresso);
                            if (progresso == 100) {
                                progressBarCarregando.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "Acaboooouuu !!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }
}
