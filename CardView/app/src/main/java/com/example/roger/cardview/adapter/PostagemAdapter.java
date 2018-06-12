package com.example.roger.cardview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roger.cardview.R;
import com.example.roger.cardview.modal.Postagem;

import java.util.List;

public class PostagemAdapter extends RecyclerView.Adapter<PostagemAdapter.MyViewHolder> {

    private List<Postagem> postagens;

    public PostagemAdapter(List<Postagem> postagens) {
        this.postagens = postagens;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.postagem_detalhe, parent, false
        );

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Postagem postagem = this.postagens.get(position);

        holder.textNome.setText(postagem.getUsuario());
        holder.textPostagem.setText(postagem.getDescricao());
        holder.imagePostagem.setImageResource(postagem.getImagem());

    }

    @Override
    public int getItemCount() {
        return this.postagens.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textNome;
        private TextView textPostagem;
        private ImageView imagePostagem;

        public MyViewHolder(View itemView) {
            super(itemView);

            textNome = itemView.findViewById(R.id.textNome);
            textPostagem = itemView.findViewById(R.id.textPostagem);
            imagePostagem = itemView.findViewById(R.id.imagePostagem);
        }
    }

}
