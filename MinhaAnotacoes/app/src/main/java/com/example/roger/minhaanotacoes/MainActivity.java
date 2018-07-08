package com.example.roger.minhaanotacoes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private AnotacoesPreferences preferences;
    private EditText editAnotacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.editAnotacao = findViewById(R.id.editAnotacao);
        this.preferences = new AnotacoesPreferences(getApplicationContext());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoRecuperacao = editAnotacao.getText().toString();

                if (textoRecuperacao.equals("")) {
                    Snackbar.make(view, "Por favor, preencha a anotação!", Snackbar.LENGTH_LONG).show();
                } else {
                    preferences.salvarAnotacao(textoRecuperacao);
                    Snackbar.make(view, "Anotação salva com sucesso!", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        // Recuperando uma anotacao
        String anotacao = preferences.recuperarAnotacao();
        if (!anotacao.equals(""))
            editAnotacao.setText(anotacao);
    }
}
