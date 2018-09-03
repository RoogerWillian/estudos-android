package whatsapp.com.rogerwillian.whatsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import whatsapp.com.rogerwillian.whatsapp.R;
import whatsapp.com.rogerwillian.whatsapp.adapter.ContatosAdapter;
import whatsapp.com.rogerwillian.whatsapp.adapter.GrupoSelecionadoAdapter;
import whatsapp.com.rogerwillian.whatsapp.config.FirebaseConfig;
import whatsapp.com.rogerwillian.whatsapp.helper.RecyclerItemClickListener;
import whatsapp.com.rogerwillian.whatsapp.helper.UsuarioFirebase;
import whatsapp.com.rogerwillian.whatsapp.model.Usuario;

public class GrupoActivity extends AppCompatActivity {

    private RecyclerView recyclerMembrosSelecionados, recyclerMembros;
    private ContatosAdapter membrosAdapter;
    private GrupoSelecionadoAdapter membrosSelecionadosAdapter;
    private List<Usuario> listaMembros = new ArrayList<>();
    private List<Usuario> listaMembrosSelecionados = new ArrayList<>();
    private ValueEventListener valueEventListenerMembros;
    private DatabaseReference usuariosRef;
    private FirebaseUser usuarioAtual;
    private Toolbar toolbar;

    public void atualizarMembroToolbar() {
        int totalMembrosSelecionados = listaMembrosSelecionados.size();
        int totalMembros = listaMembros.size() + listaMembrosSelecionados.size();
        toolbar.setSubtitle(totalMembrosSelecionados + " de " + totalMembros + " selecionado(s)");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo grupo");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GrupoActivity.this, CadastroGrupoActivity.class);
                intent.putExtra("membros", (Serializable) listaMembrosSelecionados);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configurações Iniciais
        recyclerMembrosSelecionados = findViewById(R.id.recyclearMembrosSelecinados);
        recyclerMembros = findViewById(R.id.recyclerMembros);
        usuariosRef = FirebaseConfig.getFirebaseDatabase();
        usuarioAtual = UsuarioFirebase.getUsuarioAtual();

        // Configura Adapter
        membrosAdapter = new ContatosAdapter(listaMembros, getApplicationContext());

        // Configura RecyclerView para os membros
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMembros.setLayoutManager(layoutManager);
        recyclerMembros.setHasFixedSize(true);
        recyclerMembros.setAdapter(membrosAdapter);

        // Clique RecyclerView Membros
        recyclerMembros.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerMembros, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Usuario usuarioSelecionado = listaMembros.get(position);

                // Remover usuario selecionado
                listaMembros.remove(usuarioSelecionado);
                membrosAdapter.notifyDataSetChanged();

                listaMembrosSelecionados.add(usuarioSelecionado);
                membrosSelecionadosAdapter.notifyDataSetChanged();

                atualizarMembroToolbar();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));

        // Configura o recyclerview os membros selecionados.
        membrosSelecionadosAdapter = new GrupoSelecionadoAdapter(listaMembrosSelecionados, getApplicationContext());
        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMembrosSelecionados.setLayoutManager(layoutManagerHorizontal);
        recyclerMembrosSelecionados.setHasFixedSize(true);
        recyclerMembrosSelecionados.setAdapter(membrosSelecionadosAdapter);

        // Clique RecyclerView Membros Selecionados
        recyclerMembrosSelecionados.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerMembrosSelecionados, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Usuario usuarioSelecionados = listaMembrosSelecionados.get(position);
                //Remover da listagem de membros selecionado
                listaMembrosSelecionados.remove(usuarioSelecionados);
                membrosSelecionadosAdapter.notifyDataSetChanged();

                listaMembros.add(usuarioSelecionados);
                membrosAdapter.notifyDataSetChanged();
                atualizarMembroToolbar();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarUsuarios();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerMembros);
    }

    public void recuperarUsuarios() {
        listaMembros.clear();

        valueEventListenerMembros = usuariosRef.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);
                    String emailUsuarioAtual = usuarioAtual.getEmail();

                    if (!emailUsuarioAtual.equals(usuario.getEmail()))
                        listaMembros.add(usuario);
                }
                membrosAdapter.notifyDataSetChanged();
                atualizarMembroToolbar();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
