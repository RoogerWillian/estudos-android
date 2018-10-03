package instagram.com.rogerwillian.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

import instagram.com.rogerwillian.R;

public class AdapterMiniaturas extends RecyclerView.Adapter<AdapterMiniaturas.MyViewHolder> {

    private Context context;
    private List<ThumbnailItem> listaFiltros;

    public AdapterMiniaturas(Context context, List<ThumbnailItem> listaFiltros) {
        this.context = context;
        this.listaFiltros = listaFiltros;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filtros, parent, false);
        return new AdapterMiniaturas.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ThumbnailItem item = listaFiltros.get(position);
        holder.foto.setImageBitmap(item.image);
        holder.nomeFiltro.setText(item.filterName);
    }

    @Override
    public int getItemCount() {
        return listaFiltros.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView foto;
        TextView nomeFiltro;

        public MyViewHolder(View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageFotoFiltro);
            nomeFiltro = itemView.findViewById(R.id.textNomeFiltro);
        }
    }

}
