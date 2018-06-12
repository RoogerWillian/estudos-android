package com.example.roger.cardview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.roger.cardview.R;
import com.example.roger.cardview.adapter.PostagemAdapter;
import com.example.roger.cardview.modal.Postagem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerPostagem;
    private List<Postagem> postagens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.recyclerPostagem = findViewById(R.id.recyclerPostagem);
        this.recyclerPostagem.setHasFixedSize(true);

        // Define o layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerPostagem.setLayoutManager(layoutManager);

        // preparando minha postagem
        this.prepararPostagens();

        // Define adapter
        PostagemAdapter adapter = new PostagemAdapter(this.postagens);
        this.recyclerPostagem.setAdapter(adapter);
    }

    public void prepararPostagens() {

        this.postagens.add(new Postagem("Roger Willian N. Rocha", "#tbt viagem legal!", R.drawable.imagem1));
        this.postagens.add(new Postagem("Milton Luiz Alvarenga", "só na dieta #sovai !", R.drawable.imagem2));
        this.postagens.add(new Postagem("Alex Antonio", "vendas na cabeça #borapraaction !", R.drawable.imagem3));
        this.postagens.add(new Postagem("Marcelo Tavares", "não quero fazer nada #clientefdp !", R.drawable.imagem3));

    }
}
