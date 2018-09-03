package whatsapp.com.rogerwillian.whatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import whatsapp.com.rogerwillian.whatsapp.R;
import whatsapp.com.rogerwillian.whatsapp.model.Usuario;

public class GrupoSelecionadoAdapter extends RecyclerView.Adapter<GrupoSelecionadoAdapter.MyViewHolder> {

    private List<Usuario> contatosSelecionados;
    private Context context;

    public GrupoSelecionadoAdapter(List<Usuario> listaContatos, Context context) {
        this.contatosSelecionados = listaContatos;
        this.context = context;
    }

    @NonNull
    @Override
    public GrupoSelecionadoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_grupo_selecionado, parent, false);
        return new GrupoSelecionadoAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull GrupoSelecionadoAdapter.MyViewHolder holder, int position) {
        Usuario usuario = contatosSelecionados.get(position);

        holder.nome.setText(usuario.getNome());
        String fotoContato = usuario.getFoto();

        if (fotoContato != null) {
            Uri uri = Uri.parse(fotoContato);
            Glide.with(context)
                    .load(uri).into(holder.foto);
        } else {
            holder.foto.setImageResource(R.drawable.padrao);
        }
    }

    @Override
    public int getItemCount() {
        return contatosSelecionados.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView foto;
        TextView nome;

        public MyViewHolder(View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewFotoMembroSelecionado);
            nome = itemView.findViewById(R.id.textNomeMembroSelecionado);

            foto.setImageResource(R.drawable.padrao);
        }
    }

}
