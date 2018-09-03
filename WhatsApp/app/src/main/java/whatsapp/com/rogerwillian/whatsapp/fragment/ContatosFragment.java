package whatsapp.com.rogerwillian.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import whatsapp.com.rogerwillian.whatsapp.R;
import whatsapp.com.rogerwillian.whatsapp.activity.ChatActivity;
import whatsapp.com.rogerwillian.whatsapp.activity.GrupoActivity;
import whatsapp.com.rogerwillian.whatsapp.adapter.ContatosAdapter;
import whatsapp.com.rogerwillian.whatsapp.adapter.ConversasAdapter;
import whatsapp.com.rogerwillian.whatsapp.config.FirebaseConfig;
import whatsapp.com.rogerwillian.whatsapp.helper.RecyclerItemClickListener;
import whatsapp.com.rogerwillian.whatsapp.helper.UsuarioFirebase;
import whatsapp.com.rogerwillian.whatsapp.model.Conversa;
import whatsapp.com.rogerwillian.whatsapp.model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ContatosAdapter adapter;
    private RecyclerView recyclerViewListaContatos;
    private ArrayList<Usuario> listaContatos = new ArrayList<>();
    private DatabaseReference usuariosRef;
    private ValueEventListener valueEventListenerContatos;
    private FirebaseUser usuarioAtual;
    private ProgressBar progressBarContatos;

    public ContatosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        listaContatos.clear();
        recuperarUsuarios();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerContatos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        //Configuracoes Iniciais
        recyclerViewListaContatos = view.findViewById(R.id.recyclerViewListaContatos);
        progressBarContatos = view.findViewById(R.id.progressBarContatos);
//        progressBarContatos.setVisibility(View.VISIBLE);
        usuariosRef = FirebaseConfig.getFirebaseDatabase();
        usuarioAtual = UsuarioFirebase.getUsuarioAtual();

        //configurar adapter
        adapter = new ContatosAdapter(listaContatos, getActivity());

        //Configura recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListaContatos.setLayoutManager(layoutManager);
        recyclerViewListaContatos.setHasFixedSize(true);
        recyclerViewListaContatos.setAdapter(adapter);

        // Configurar evento de clique
        recyclerViewListaContatos.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                recyclerViewListaContatos,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        List<Usuario> listaContatosAtualizada = adapter.getListaContatos();

                        Usuario usuarioSelecionado = listaContatosAtualizada.get(position);
                        boolean cabecalho = usuarioSelecionado.getEmail().isEmpty();

                        if (cabecalho) {
                            Intent intent = new Intent(getActivity(), GrupoActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(), ChatActivity.class);
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
                }
        ));

        return view;
    }

    public void recuperarUsuarios() {

        Usuario itemGrupo = new Usuario();
        itemGrupo.setNome("Novo grupo");
        itemGrupo.setEmail("");
        listaContatos.add(itemGrupo);

        valueEventListenerContatos = usuariosRef.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBarContatos.setVisibility(View.VISIBLE);
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);
                    String emailUsuarioAtual = usuarioAtual.getEmail();

                    if (!emailUsuarioAtual.equals(usuario.getEmail()))
                        listaContatos.add(usuario);
                }
                adapter.notifyDataSetChanged();
                progressBarContatos.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void pesquisarContatos(String texto) {

        List<Usuario> contatosBusca = new ArrayList<>();
        for (Usuario usuario : listaContatos) {

            String nome = usuario.getNome().toLowerCase();
            if (nome.contains(texto))
                contatosBusca.add(usuario);
        }

        adapter = new ContatosAdapter(contatosBusca, getActivity());
        recyclerViewListaContatos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recarregarContatos() {
        adapter = new ContatosAdapter(listaContatos, getActivity());
        recyclerViewListaContatos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
