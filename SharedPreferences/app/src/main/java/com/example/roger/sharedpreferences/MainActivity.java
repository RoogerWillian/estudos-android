package com.example.roger.sharedpreferences;

import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button buttonSalvar;
    private TextInputEditText edtNome;
    private TextView textResultado;
    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSalvar = findViewById(R.id.buttonSalvar);
        edtNome = findViewById(R.id.edtNome);
        textResultado = findViewById(R.id.textResultado);

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
                SharedPreferences.Editor editor = preferences.edit();

                //Validar nome
                if (edtNome.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Por favor, digite o nome", Toast.LENGTH_SHORT).show();
                } else {
                    String nome = edtNome.getText().toString();
                    editor.putString("nome", nome);
                    editor.commit();
                    textResultado.setText("Olá, " + nome);
                }
            }
        });

        //Recuperar SharedPreferences
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

        //Validar se temos a chave
        if (preferences.contains("nome")) {
            String nome = preferences.getString("nome", "usuário não definido");
            textResultado.setText("Olá, " + nome);
        } else {
            textResultado.setText("Olá, usuário não definido");
        }
    }
}
