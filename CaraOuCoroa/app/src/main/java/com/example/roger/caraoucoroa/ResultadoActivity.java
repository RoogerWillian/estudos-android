package com.example.roger.caraoucoroa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ResultadoActivity extends AppCompatActivity {

    private Button buttonVoltar;
    private ImageView imageResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        buttonVoltar = findViewById(R.id.buttonVoltar);
        imageResultado = findViewById(R.id.imageResultado);

        // Recuperar
        Bundle dados = getIntent().getExtras();
        int numero = dados.getInt("numero");

        if (numero == 0) {
            imageResultado.setImageResource(R.drawable.moeda_cara);
        } else {
            imageResultado.setImageResource(R.drawable.moeda_coroa);
        }

        // Evento de clique para o botao;
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
