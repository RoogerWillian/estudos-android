package com.example.roger.sorteio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void botaoJogarClicado(View view) {
        TextView textoNumeroSelecionado = this.findViewById(R.id.txt_numero_escolhido);

        int numero = new Random().nextInt(11);
        textoNumeroSelecionado.setText("O número selecionado é: " + numero);
    }
}
