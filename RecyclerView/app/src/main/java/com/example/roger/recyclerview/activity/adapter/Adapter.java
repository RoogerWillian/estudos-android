package com.example.roger.recyclerview.activity.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roger.recyclerview.R;
import com.example.roger.recyclerview.activity.model.Filme;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Filme> listaFilmes;

    public Adapter(List<Filme> lista) {
        this.listaFilmes = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_lista, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Filme filme = listaFilmes.get(position);

        holder.titulo.setText(filme.getTitulo());
        holder.genero.setText(filme.getGenero());
        holder.ano.setText(filme.getAno());
    }

    @Override
    public int getItemCount() {
        return listaFilmes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo;
        TextView ano;
        TextView genero;

        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textTitulo);
            ano = itemView.findViewById(R.id.textAno);
            genero = itemView.findViewById(R.id.textGenero);
        }
    }

}
