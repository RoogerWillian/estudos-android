package instagram.com.rogerwillian.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import instagram.com.rogerwillian.R;
import instagram.com.rogerwillian.adapter.AdapterGrid;
import instagram.com.rogerwillian.helper.FirebaseConfig;
import instagram.com.rogerwillian.helper.TextUtil;
import instagram.com.rogerwillian.helper.UsuarioFirebase;
import instagram.com.rogerwillian.model.Postagem;
import instagram.com.rogerwillian.model.Usuario;

public class PerfilAmigoActivity extends AppCompatActivity {

    private CircleImageView imagePerfil;
    private GridView gridViewPerfil;
    private TextView textPublicacoes, textSeguidores, textSeguindo;
    private Usuario usuarioSelecionado;
    private Usuario usuarioLogado;
    private Button buttonAcaoPerfil;
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioAmigoRef;
    private DatabaseReference firebaseRef;
    private DatabaseReference seguidoresRef;
    private DatabaseReference usuarioLogadoRef;
    private ValueEventListener valueEventListenerPerfilAmigo;
    private String idUsuarioLogado;
    private DatabaseReference postagensUsuarioRef;
    private AdapterGrid adapterGrid;
    private List<Postagem> postagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigo);

        // Configuracoes Iniciais
        this.inicializarComponentes();

        // Recuperando usuario selecionado
        this.recuperarUsuarioSelecionado();

        // Configurar toolbar
        this.configurarToolbar();

        // Inicializando imagem loader
        this.inicializarImageLoader();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.recuperarInformacoesAmigo();
        this.recuperarDadosUsuarioLogado();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioAmigoRef.removeEventListener(valueEventListenerPerfilAmigo);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void recuperarInformacoesAmigo() {
        usuarioAmigoRef = usuariosRef.child(usuarioSelecionado.getId());
        valueEventListenerPerfilAmigo = usuarioAmigoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuarioAmigo = dataSnapshot.getValue(Usuario.class);
                if (usuarioAmigo != null) {
                    textPublicacoes.setText(String.valueOf(usuarioAmigo.getPublicacoes()));
                    textSeguidores.setText(String.valueOf(usuarioAmigo.getSeguidores()));
                    textSeguindo.setText(String.valueOf(usuarioAmigo.getSeguindo()));
                    String foto = usuarioAmigo.getFoto();
                    if (foto != null && !foto.isEmpty()) {
                        Uri urlFotoAmigo = Uri.parse(foto);
                        Glide.with(PerfilAmigoActivity.this).load(urlFotoAmigo).into(imagePerfil);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle(TextUtil.capitalize(usuarioSelecionado.getNome().toLowerCase()));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
    }

    private void inicializarComponentes() {
        this.buttonAcaoPerfil = findViewById(R.id.buttonAcaoPerfil);
        this.imagePerfil = findViewById(R.id.imagePerfil);
        this.gridViewPerfil = findViewById(R.id.gridViewPerfil);
        this.gridViewPerfil.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        this.textPublicacoes = findViewById(R.id.textPublicacoes);
        this.textSeguidores = findViewById(R.id.textSeguidores);
        this.textSeguindo = findViewById(R.id.textSeguindo);
        this.idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        this.firebaseRef = FirebaseConfig.getFirebaseDatabase();
        this.usuariosRef = firebaseRef.child("usuarios");
        this.buttonAcaoPerfil.setText(R.string.text_carregando);
        this.seguidoresRef = firebaseRef.child("seguidores");

        // Clique nas fotos no gridview
        this.gridViewPerfil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Postagem postagem = postagens.get(i);
                Intent intent = new Intent(getApplicationContext(), VisualizarPostagemActivity.class);
                intent.putExtra("postagem", postagem);
                intent.putExtra("usuario", usuarioSelecionado);
                startActivity(intent);
            }
        });
    }

    private void recuperarUsuarioSelecionado() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");
            postagensUsuarioRef = FirebaseConfig.getFirebaseDatabase().child(Postagem.POSTAGENS_REF).child(usuarioSelecionado.getId());

            this.carregarFotosPostagem();
        }
    }

    public void inicializarImageLoader() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();

        ImageLoader.getInstance().init(config);

    }

    private void carregarFotosPostagem() {
        postagens = new ArrayList<>();
        // Recupera fotos do usuario
        postagensUsuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Configurar o tamanho do grid
                int tamanhoGrid = getResources().getDisplayMetrics().widthPixels;
                int tamanhoImagem = tamanhoGrid / 3;
                gridViewPerfil.setColumnWidth(tamanhoImagem);

                List<String> urlsFotos = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Postagem postagem = ds.getValue(Postagem.class);
                    if (postagem != null) {
                        urlsFotos.add(postagem.getCaminhoFoto());
                        postagens.add(postagem);
                    }
                }

                // Configurar adapter gridview
                adapterGrid = new AdapterGrid(getApplicationContext(), R.layout.grid_postagem, urlsFotos);
                gridViewPerfil.setAdapter(adapterGrid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void recuperarDadosUsuarioLogado() {
        usuarioLogadoRef = usuariosRef.child(idUsuarioLogado);
        usuarioLogadoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarioLogado = dataSnapshot.getValue(Usuario.class);
                // Verificando se o usuario logado segue o usuario selecionado
                verificaSegueUsuarioAmigo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void verificaSegueUsuarioAmigo() {
        DatabaseReference seguidorRef = seguidoresRef.child(idUsuarioLogado).child(usuarioSelecionado.getId());
        seguidorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    acaoBotaoSeguir(true);
                else
                    acaoBotaoSeguir(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void acaoBotaoSeguir(boolean isSegueAmigo) {
        this.buttonAcaoPerfil.setOnClickListener(null);
        if (isSegueAmigo) {
            this.buttonAcaoPerfil.setText(R.string.text_seguindo);
            this.buttonAcaoPerfil.setOnClickListener(null);
        } else {
            this.buttonAcaoPerfil.setText(R.string.text_seguir);
            this.buttonAcaoPerfil.setTextColor(getResources().getColor(android.R.color.black));
            this.buttonAcaoPerfil.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background_botao_perfil));
            this.buttonAcaoPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    salvarSeguidor(usuarioLogado, usuarioSelecionado);
                }
            });
        }
    }

    private void salvarSeguidor(Usuario uLogado, Usuario uAmigo) {
        HashMap<String, Object> dadosAmigo = new HashMap<>();
        dadosAmigo.put("nome", uAmigo.getNome());
        dadosAmigo.put("foto", uAmigo.getFoto());
        DatabaseReference seguidorRef = this.seguidoresRef.child(uLogado.getId()).child(uAmigo.getId());
        seguidorRef.setValue(dadosAmigo);

        // Incrementar seguindo do usuario logado
        uLogado.setSeguindo(uLogado.getSeguindo() + 1);
        uLogado.atualizar();

        // Incrementar seguidores do amigo
        uAmigo.setSeguidores(uAmigo.getSeguidores() + 1);
        uAmigo.atualizar();

        this.verificaSegueUsuarioAmigo();
    }


}
