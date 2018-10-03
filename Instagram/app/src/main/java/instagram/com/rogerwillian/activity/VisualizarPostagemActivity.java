package instagram.com.rogerwillian.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import instagram.com.rogerwillian.R;
import instagram.com.rogerwillian.helper.TextUtil;
import instagram.com.rogerwillian.model.Postagem;
import instagram.com.rogerwillian.model.Usuario;

public class VisualizarPostagemActivity extends AppCompatActivity {

    private CircleImageView imagemPefilPostagem;
    private TextView textPerfilPostagem, textQtdCurtidasPostagem, textDescricaoPostagem, textVisualizarComentariosPostagem;
    private ImageView imagePostagemSelecionada;
    private Usuario usuarioSelecionado;
    private Postagem postagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_postagem);

        this.initializeComponents();
        this.toRecoverIntentData();
        this.configureToolbar();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void toRecoverIntentData() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            postagem = (Postagem) bundle.getSerializable("postagem");
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuario");

            if (usuarioSelecionado != null && postagem != null)
                exibirDadosUsuarioEPostagem(usuarioSelecionado, postagem);
        }

    }

    private void exibirDadosUsuarioEPostagem(Usuario usuario, Postagem postagem) {

        String fotoUsuario = usuario.getFoto();
        if (fotoUsuario != null) {
            Glide.with(VisualizarPostagemActivity.this).load(Uri.parse(fotoUsuario)).into(imagemPefilPostagem);
        }
        textPerfilPostagem.setText(usuario.getNome());

        String fotoPostagem = postagem.getCaminhoFoto();
        if (fotoPostagem != null)
            Glide.with(VisualizarPostagemActivity.this).load(Uri.parse(fotoPostagem)).into(imagePostagemSelecionada);

        textDescricaoPostagem.setText(postagem.getDescricao());

    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle(R.string.text_view_post);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
    }

    private void initializeComponents() {
        this.imagemPefilPostagem = findViewById(R.id.imagePerfilPostagemSelecionada);
        this.textPerfilPostagem = findViewById(R.id.textPerfilPostagemSelecionada);
        this.textQtdCurtidasPostagem = findViewById(R.id.textQtdCurtidasPostagemSelecionada);
        this.textDescricaoPostagem = findViewById(R.id.textDescricaoPostagemSelecionada);
        this.textVisualizarComentariosPostagem = findViewById(R.id.textVisualizarComentarioPostagemSelecionada);
        this.imagePostagemSelecionada = findViewById(R.id.imagePostagemSelecionada);
    }
}
