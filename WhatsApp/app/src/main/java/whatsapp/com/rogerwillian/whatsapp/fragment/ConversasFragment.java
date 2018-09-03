package whatsapp.com.rogerwillian.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import whatsapp.com.rogerwillian.whatsapp.R;
import whatsapp.com.rogerwillian.whatsapp.activity.ChatActivity;
import whatsapp.com.rogerwillian.whatsapp.adapter.ConversasAdapter;
import whatsapp.com.rogerwillian.whatsapp.config.FirebaseConfig;
import whatsapp.com.rogerwillian.whatsapp.helper.RecyclerItemClickListener;
import whatsapp.com.rogerwillian.whatsapp.helper.UsuarioFirebase;
import whatsapp.com.rogerwillian.whatsapp.model.Conversa;
import whatsapp.com.rogerwillian.whatsapp.model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    private RecyclerView recyclerConversas;
    private ConversasAdapter adapter;
    private List<Conversa> conversas = new ArrayList<>();
    private DatabaseReference database;
    private DatabaseReference conversasRef;
    private ValueEventListener childEventListenerConversas;
    private ProgressBar progressBarConversas;

    public ConversasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        recyclerConversas = view.findViewById(R.id.recyclerConversas);
        progressBarConversas = view.findViewById(R.id.progressBarConversas);

        // Configurando RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerConversas.setLayoutManager(layoutManager);
        recyclerConversas.setHasFixedSize(true);
        adapter = new ConversasAdapter(conversas, getActivity());
        recyclerConversas.setAdapter(adapter);

        database = FirebaseConfig.getFirebaseDatabase();
        String identificador = UsuarioFirebase.getIdentificador();
        conversasRef = database.child("conversas").child(identificador);

        // RecyclerView clique
        recyclerConversas.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerConversas, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Conversa> conversasAtualizadas = adapter.getConversas();
                Conversa conversaSelecionada = conversasAtualizadas.get(position);
                Intent intent = new Intent(getActivity(), ChatActivity.class);

                if (conversaSelecionada.getIsGroup().equals("true")) {
                    intent.putExtra("chatGrupo", conversaSelecionada.getGrupo());
                    startActivity(intent);
                } else {
                    Usuario usuarioSelecionado = conversaSelecionada.getUsuarioExibicao();
                    intent.putExtra("chatContato", usuarioSelecionado);
                    startActivity(intent);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarConversas();
    }

    @Override
    public void onStop() {
        super.onStop();

        conversasRef.removeEventListener(childEventListenerConversas);
    }

    public void recuperarConversas() {
        progressBarConversas.setVisibility(View.VISIBLE);
        childEventListenerConversas = conversasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                conversas.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Conversa conversa = dados.getValue(Conversa.class);
                    conversas.add(conversa);
                }

                adapter.notifyDataSetChanged();
                progressBarConversas.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void pesquisarConversas(String texto) {

        List<Conversa> conversasBusca = new ArrayList<>();
        for (Conversa conversa : conversas) {

            if (conversa.getUsuarioExibicao() != null) {
                String nome = conversa.getUsuarioExibicao().getNome().toLowerCase();
                String ultimaMensagem = conversa.getUltimaMensagem().toLowerCase();
                if (nome.contains(texto) || ultimaMensagem.contains(texto))
                    conversasBusca.add(conversa);
            } else {
                String nome = conversa.getGrupo().getNome().toLowerCase();
                String ultimaMensagem = conversa.getUltimaMensagem().toLowerCase();
                if (nome.contains(texto) || ultimaMensagem.contains(texto))
                    conversasBusca.add(conversa);
            }
        }

        adapter = new ConversasAdapter(conversasBusca, getActivity());
        recyclerConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recarregarConversas() {
        adapter = new ConversasAdapter(conversas, getActivity());
        recyclerConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
