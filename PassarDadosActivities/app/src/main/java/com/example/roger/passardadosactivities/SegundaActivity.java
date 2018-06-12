package com.example.roger.passardadosactivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SegundaActivity extends AppCompatActivity {

    private TextView textNome, textIdade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        this.textNome = findViewById(R.id.textNome);
        this.textIdade = findViewById(R.id.textIdade);

        // Recuperando os dados enviados
        Bundle dados = this.getIntent().getExtras();
        String nome = dados.getString("nome");
        int idade = dados.getInt("idade");
        Usuario usuario = (Usuario) dados.getSerializable("objeto");

        // Configurar valor recuperados.
        this.textNome.setText(usuario.getEmail());
        this.textIdade.setText(String.valueOf(idade));
    }
}
