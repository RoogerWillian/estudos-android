package com.example.roger.caraoucoroa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button buttonJogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.buttonJogar = findViewById(R.id.buttonJogar);
        this.buttonJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResultadoActivity.class);

                //Passar dados para a proxima tela
                int numero = new Random().nextInt(2);
                intent.putExtra("numero", numero);

                startActivity(intent);
            }
        });
    }
}
