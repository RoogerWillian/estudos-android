package instagram.com.rogerwillian.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import instagram.com.rogerwillian.R;
import instagram.com.rogerwillian.activity.EditarPerfilActivity;
import instagram.com.rogerwillian.activity.PerfilAmigoActivity;
import instagram.com.rogerwillian.adapter.AdapterGrid;
import instagram.com.rogerwillian.helper.FirebaseConfig;
import instagram.com.rogerwillian.helper.UsuarioFirebase;
import instagram.com.rogerwillian.model.Postagem;
import instagram.com.rogerwillian.model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private ProgressBar progressBar;
    private CircleImageView imagePerfil;
    private GridView gridViewPerfil;
    private TextView textPublicacoes, textSeguidores, textSeguindo;
    private Button buttonAcaoPerfil;
    private Usuario usuarioLogado;
    private DatabaseReference firebaseRef;
    private DatabaseReference perfilRef;
    private String identificadorUsuarioLogado;
    private DatabaseReference usuarioLogadoRef;
    private DatabaseReference usuariosRef;
    private ValueEventListener valueEventListenerPerfil;
    private DatabaseReference postagensUsuarioRef;
    private AdapterGrid adapterGrid;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStop() {
        super.onStop();
        usuarioLogadoRef.removeEventListener(valueEventListenerPerfil);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Configurações Iniciais
        this.inicializarComponentes(view);

        //Abre edição de perfil
        this.buttonAcaoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditarPerfilActivity.class));
            }
        });

        // Foto
        String foto = usuarioLogado.getFoto();
        if (foto != null && !foto.isEmpty()) {
            Uri urlFotoAmigo = Uri.parse(foto);
            Glide.with(getActivity()).load(urlFotoAmigo).into(imagePerfil);
        }

        // Inicializar Imagem Loader
        this.inicializarImageLoader();

        // Carregando fotos postagem
        this.carregarFotosPostagem();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.recuperarDadosUsuarioLogado();
    }

    private void carregarFotosPostagem() {

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
                    if (postagem != null)
                        urlsFotos.add(postagem.getCaminhoFoto());
                }

                // Configurar adapter gridview
                adapterGrid = new AdapterGrid(getActivity(), R.layout.grid_postagem, urlsFotos);
                gridViewPerfil.setAdapter(adapterGrid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void inicializarImageLoader() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getActivity())
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();

        ImageLoader.getInstance().init(config);

    }

    private void recuperarDadosUsuarioLogado() {
        usuarioLogadoRef = usuariosRef.child(usuarioLogado.getId());
        valueEventListenerPerfil = usuarioLogadoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuarioLogado = dataSnapshot.getValue(Usuario.class);
                if (usuarioLogado != null) {
                    textPublicacoes.setText(String.valueOf(usuarioLogado.getPublicacoes()));
                    textSeguidores.setText(String.valueOf(usuarioLogado.getSeguidores()));
                    textSeguindo.setText(String.valueOf(usuarioLogado.getSeguindo()));
                    String foto = usuarioLogado.getFoto();
                    if (foto != null && !foto.isEmpty()) {
                        Uri urlFotoAmigo = Uri.parse(foto);
                        Glide.with(getActivity()).load(urlFotoAmigo).into(imagePerfil);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void inicializarComponentes(View view) {
        this.progressBar = view.findViewById(R.id.progressBarPerfil);
        this.imagePerfil = view.findViewById(R.id.imagePerfil);
        this.gridViewPerfil = view.findViewById(R.id.gridViewPerfil);
        this.textPublicacoes = view.findViewById(R.id.textPublicacoes);
        this.textSeguidores = view.findViewById(R.id.textSeguidores);
        this.textSeguindo = view.findViewById(R.id.textSeguindo);
        this.buttonAcaoPerfil = view.findViewById(R.id.buttonAcaoPerfil);
        this.usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        this.identificadorUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        this.firebaseRef = FirebaseConfig.getFirebaseDatabase();
        this.perfilRef = firebaseRef.child("usuarios").child(identificadorUsuarioLogado);
        this.usuariosRef = firebaseRef.child("usuarios");
        postagensUsuarioRef = FirebaseConfig.getFirebaseDatabase().child(Postagem.POSTAGENS_REF).child(usuarioLogado.getId());
    }
}
