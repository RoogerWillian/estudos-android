package com.example.roger.recyclerview.activity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.roger.recyclerview.R;
import com.example.roger.recyclerview.activity.RecyclerItemClickListener;
import com.example.roger.recyclerview.activity.adapter.Adapter;
import com.example.roger.recyclerview.activity.model.Filme;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Filme> listaFilmes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.recyclerView = findViewById(R.id.recyclerView);

        // Listagem de listaFilmes
        this.criarFilmes();

        // Configurar Adapter
        Adapter adapter = new Adapter(this.listaFilmes);

        // Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        this.recyclerView.setAdapter(adapter);

        // Evento de click
        this.recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        this.recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Filme filme = listaFilmes.get(position);
                                Toast.makeText(getApplicationContext(), "Item pressionado: " + filme.getTitulo(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                Filme filme = listaFilmes.get(position);
                                Toast.makeText(getApplicationContext(), "Clique long: " + filme.getTitulo(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );
    }

    public void criarFilmes() {
        this.listaFilmes.add(new Filme("Black Panther", "Ficção/Ação", "2018"));
        this.listaFilmes.add(new Filme("Deadpool 2", "Ação/Comédia", "2018"));
        this.listaFilmes.add(new Filme("Avengers - Infinity War", "Ficção/Ação", "2018"));
        this.listaFilmes.add(new Filme("Homem Aranha - De volta ao lar", "Aventura", "2017"));
        this.listaFilmes.add(new Filme("Mulher Maravilha", "Fantasia", "2017"));
        this.listaFilmes.add(new Filme("Liga da Justiça", "Ficção", "2017"));
        this.listaFilmes.add(new Filme("Capitão América - Guerra Civíl", "Aventura/Ficção", "2016"));
        this.listaFilmes.add(new Filme("It: A coisa", "Drama/Terror", "2017"));
        this.listaFilmes.add(new Filme("Pica-Pau: O Filme", "Comédia/Animação", "2017"));
        this.listaFilmes.add(new Filme("A Múmia", "Terror", "2017"));
        this.listaFilmes.add(new Filme("A Bela e a Fera", "Romance", "2017"));
        this.listaFilmes.add(new Filme("Meu Malvado Favorito 3", "Comédia", "2017"));
        this.listaFilmes.add(new Filme("Carros 3", "Comédia", "2017"));
    }
}
