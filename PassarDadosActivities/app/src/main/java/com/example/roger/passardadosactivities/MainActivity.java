package com.example.roger.passardadosactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.buttonEnviar = findViewById(R.id.buttonEnviar);
        this.buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SegundaActivity.class);

                Usuario usuario = new Usuario("Milton Luiz", "gordo@gmail.com");

                //Passar dados
                intent.putExtra("nome", "Roger Willian");
                intent.putExtra("idade", 23);
                intent.putExtra("objeto", usuario);

                startActivity(intent);
            }
        });
    }
}
