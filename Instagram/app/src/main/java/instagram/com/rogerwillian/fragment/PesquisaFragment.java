package instagram.com.rogerwillian.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import instagram.com.rogerwillian.R;
import instagram.com.rogerwillian.activity.PerfilAmigoActivity;
import instagram.com.rogerwillian.adapter.AdapterPesquisa;
import instagram.com.rogerwillian.helper.FirebaseConfig;
import instagram.com.rogerwillian.helper.RecyclerItemClickListener;
import instagram.com.rogerwillian.helper.UsuarioFirebase;
import instagram.com.rogerwillian.model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisaFragment extends Fragment {

    private SearchView searchViewPesquisa;
    private RecyclerView recyclerViewPesquisa;
    private List<Usuario> listaUsuarios;
    private DatabaseReference usuariosRef;
    private AdapterPesquisa adapterPesquisa;
    private ProgressBar progressBarPesquisa;
    private String idUsuarioLogado;

    public PesquisaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);

        // Configuracoes Iniciais
        this.inicializarComponentes(view);

        // Configura searchview
        this.configuraSearchView();

        // Configura recyclerView
        configuraRecyclerPesquisa();

        return view;
    }

    private void pesquisarUsuarios(String texto) {
        listaUsuarios.clear();
        if (texto.length() >= 2) {
            Query query = usuariosRef.orderByChild("nome")
                    .startAt(texto)
                    .endAt(texto + "\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    listaUsuarios.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Usuario usuario = ds.getValue(Usuario.class);

                        if (idUsuarioLogado.equals(usuario.getId()))
                            continue;
                        listaUsuarios.add(usuario);
                    }

                    adapterPesquisa.notifyDataSetChanged();
                    progressBarPesquisa.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            progressBarPesquisa.setVisibility(View.GONE);
        }
    }

    private void inicializarComponentes(View view) {
        this.searchViewPesquisa = view.findViewById(R.id.searchViewPesquisa);
        this.recyclerViewPesquisa = view.findViewById(R.id.recyclerViewPesquisa);
        this.progressBarPesquisa = view.findViewById(R.id.progressBarPesquisa);
        this.usuariosRef = FirebaseConfig.getFirebaseDatabase().child("usuarios");
        this.idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        this.progressBarPesquisa.setVisibility(View.GONE);
        this.listaUsuarios = new ArrayList<>();
    }

    private void configuraSearchView() {
        searchViewPesquisa.setQueryHint("Buscar usuários");
        searchViewPesquisa.requestFocusFromTouch();
        searchViewPesquisa.setIconified(false);
        searchViewPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                progressBarPesquisa.setVisibility(View.VISIBLE);
                String textoDigitado = newText.toUpperCase();
                pesquisarUsuarios(textoDigitado);
                return true;
            }
        });
    }

    private void configuraRecyclerPesquisa() {
        recyclerViewPesquisa.setHasFixedSize(true);
        recyclerViewPesquisa.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterPesquisa = new AdapterPesquisa(listaUsuarios, getActivity());
        recyclerViewPesquisa.setAdapter(adapterPesquisa);
        recyclerViewPesquisa.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                recyclerViewPesquisa,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Usuario usuario = listaUsuarios.get(position);
                        Intent intent = new Intent(getActivity(), PerfilAmigoActivity.class);
                        intent.putExtra("usuarioSelecionado", usuario);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }));
    }
}
