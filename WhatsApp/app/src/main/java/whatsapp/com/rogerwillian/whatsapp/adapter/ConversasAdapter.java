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
import whatsapp.com.rogerwillian.whatsapp.model.Conversa;
import whatsapp.com.rogerwillian.whatsapp.model.Grupo;
import whatsapp.com.rogerwillian.whatsapp.model.Usuario;

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MyViewHolder> {

    private List<Conversa> conversas;
    private Context context;

    public ConversasAdapter(List<Conversa> conversas, Context context) {
        this.conversas = conversas;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemConversa = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_conversas, parent, false);

        return new MyViewHolder(itemConversa);
    }

    public List<Conversa> getConversas() {
        return conversas;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Conversa conversa = this.conversas.get(position);
        holder.ultimaMensagemConversa.setText(conversa.getUltimaMensagem());

        if (conversa.getIsGroup().equals("true")) {

            Grupo grupo = conversa.getGrupo();
            holder.nomeUsuarioConversa.setText(grupo.getNome());

            if (grupo.getFoto() != null && !grupo.getFoto().isEmpty()) {
                Glide.with(context)
                        .load(Uri.parse(grupo.getFoto()))
                        .into(holder.imageViewFotoConversa);
            } else {
                holder.imageViewFotoConversa.setImageResource(R.drawable.padrao);
            }
        } else {
            Usuario usuarioExibicao = conversa.getUsuarioExibicao();
            if (usuarioExibicao != null) {
                holder.nomeUsuarioConversa.setText(usuarioExibicao.getNome());
                String fotoUsuarioConversa = usuarioExibicao.getFoto();
                if (fotoUsuarioConversa != null && !fotoUsuarioConversa.isEmpty()) {
                    Glide.with(context)
                            .load(Uri.parse(fotoUsuarioConversa))
                            .into(holder.imageViewFotoConversa);
                } else {
                    holder.imageViewFotoConversa.setImageResource(R.drawable.padrao);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imageViewFotoConversa;
        private TextView nomeUsuarioConversa;
        private TextView ultimaMensagemConversa;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageViewFotoConversa = itemView.findViewById(R.id.imageViewFotoConversa);
            nomeUsuarioConversa = itemView.findViewById(R.id.nomeUsuarioConversa);
            ultimaMensagemConversa = itemView.findViewById(R.id.ultimaMensagemConversa);
        }
    }
}
