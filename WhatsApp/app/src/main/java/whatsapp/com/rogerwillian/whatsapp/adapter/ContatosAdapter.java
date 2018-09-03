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

public class ContatosAdapter extends RecyclerView.Adapter<ContatosAdapter.MyViewHolder> {

    private List<Usuario> listaContatos;
    private Context context;

    public ContatosAdapter(List<Usuario> listaContatos, Context context) {
        this.listaContatos = listaContatos;
        this.context = context;
    }

    public List<Usuario> getListaContatos() {
        return listaContatos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuario usuario = listaContatos.get(position);
        boolean cabecalho = usuario.getEmail().isEmpty();

        holder.nomeContato.setText(usuario.getNome());
        holder.emailContato.setText(usuario.getEmail());
        String fotoContato = usuario.getFoto();

        if (fotoContato != null) {
            Uri uri = Uri.parse(fotoContato);
            Glide.with(context)
                    .load(uri).into(holder.fotoUsuario);
        } else {
            if (cabecalho) {
                holder.fotoUsuario.setImageResource(R.drawable.icone_grupo);
                holder.emailContato.setVisibility(View.GONE);
            } else
                holder.fotoUsuario.setImageResource(R.drawable.padrao);
        }
    }

    @Override
    public int getItemCount() {
        return listaContatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView fotoUsuario;
        TextView nomeContato, emailContato;

        public MyViewHolder(View itemView) {
            super(itemView);

            fotoUsuario = itemView.findViewById(R.id.imageViewFotoContato);
            nomeContato = itemView.findViewById(R.id.textNomeContato);
            emailContato = itemView.findViewById(R.id.textEmailContato);

            fotoUsuario.setImageResource(R.drawable.padrao);
        }
    }


}


