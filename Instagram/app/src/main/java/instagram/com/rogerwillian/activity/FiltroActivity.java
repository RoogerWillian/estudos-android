package instagram.com.rogerwillian.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import instagram.com.rogerwillian.R;
import instagram.com.rogerwillian.adapter.AdapterMiniaturas;
import instagram.com.rogerwillian.helper.FirebaseConfig;
import instagram.com.rogerwillian.helper.RecyclerItemClickListener;
import instagram.com.rogerwillian.helper.UsuarioFirebase;
import instagram.com.rogerwillian.model.Postagem;
import instagram.com.rogerwillian.model.Usuario;

public class FiltroActivity extends AppCompatActivity {
    private ImageView imageFotoEscolhida;
    private Bitmap imagemFiltro;
    private Bitmap imagem;
    private TextInputEditText textDescricaoFiltro;
    private List<ThumbnailItem> listaFiltros;
    private RecyclerView recyclerFiltros;
    private AdapterMiniaturas adapterMiniaturas;
    private String idUsuarioLogado;
    private Postagem postagem;
    private AlertDialog alertDialogLoad;
    private AlertDialog alertDialogCarregarDadosUsuario;
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioLogadoRef;
    private Usuario usuarioLogado;
    private boolean estaCarregando;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        this.iniciarComponentes();
        this.recuperarDadosUsuarioLogado();
        this.configurarToolbar();
        this.recuperImagemEnviada();
        this.recuperarFiltros();
        this.onItemTouchListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ic_salvar_postagem:
                publicarPostagem();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_filtro, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void publicarPostagem() {

        if (estaCarregando) {
            Toast.makeText(this, "Carregando dados, aguarde...", Toast.LENGTH_SHORT).show();
        } else {
            alertDialogLoad.show();
            postagem = new Postagem();
            postagem.setIdUsuario(idUsuarioLogado);
            postagem.setDescricao(textDescricaoFiltro.getText().toString());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imagemFiltro.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] dadosImagem = baos.toByteArray();

            // Salvando imagem no storage
            StorageReference storageRef = FirebaseConfig.getFirebaseStorage();
            StorageReference imagemRef = storageRef.child("imagens").child("postagens").child(postagem.getId() + ".jpeg");
            UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(FiltroActivity.this, "Erro ao salvar a imagem, tente novamente", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Recupear local da foto
                    Uri url = taskSnapshot.getDownloadUrl();
                    postagem.setCaminhoFoto(url.toString());
                    if (postagem.salvar()) {
                        // Atualizar quantidade de postagem
                        int qtdPostagem = usuarioLogado.getPublicacoes() + 1;
                        usuarioLogado.setPublicacoes(qtdPostagem);
                        usuarioLogado.atualizarQtdPostagem();

                        Toast.makeText(FiltroActivity.this, "Postagem publicada com sucesso =)", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    alertDialogLoad.dismiss();
                }
            });

        }
    }

    private void recuperarFiltros() {
        listaFiltros.clear();
        ThumbnailsManager.clearThumbs();

        ThumbnailItem item = new ThumbnailItem();
        item.image = imagem;
        item.filterName = "Normal";
        ThumbnailsManager.addThumb(item);

        // Listando filtros disponiveis
        List<Filter> filters = FilterPack.getFilterPack(getApplicationContext());
        for (Filter filter : filters) {
            ThumbnailItem itemFiltro = new ThumbnailItem();
            itemFiltro.image = imagem;
            itemFiltro.filter = filter;
            itemFiltro.filterName = filter.getName();
            ThumbnailsManager.addThumb(itemFiltro);
        }

        listaFiltros.addAll(ThumbnailsManager.processThumbs(getApplicationContext()));
        adapterMiniaturas.notifyDataSetChanged();
    }

    private void recuperImagemEnviada() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            byte[] dadosImagem = bundle.getByteArray("fotoEscolhida");
            imagem = BitmapFactory.decodeByteArray(dadosImagem, 0, dadosImagem.length);
            imageFotoEscolhida.setImageBitmap(imagem);

            imagemFiltro = imagem.copy(imagem.getConfig(), true);
        }
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Filtros");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
    }

    private void iniciarComponentes() {
        imageFotoEscolhida = findViewById(R.id.imageFotoEscolhida);
        recyclerFiltros = findViewById(R.id.recyclerFiltros);
        textDescricaoFiltro = findViewById(R.id.textDescricaoFiltro);
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        usuariosRef = FirebaseConfig.getFirebaseDatabase().child("usuarios");
        listaFiltros = new ArrayList<>();

        // Configurando adapter e recyclerView
        adapterMiniaturas = new AdapterMiniaturas(getApplicationContext(), listaFiltros);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerFiltros.setLayoutManager(layoutManager);
        recyclerFiltros.setHasFixedSize(true);
        recyclerFiltros.setAdapter(adapterMiniaturas);

        // Configurando AlertDialog
        alertDialogLoad = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.salvando_postagem)
                .setCancelable(false)
                .build();

        alertDialogCarregarDadosUsuario = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Aguarde! Carregando dados...")
                .setCancelable(false)
                .build();
    }

    private void onItemTouchListener() {
        recyclerFiltros.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerFiltros, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                imagemFiltro = imagem.copy(imagem.getConfig(), true);
                Filter filter = listaFiltros.get(position).filter;
                imageFotoEscolhida.setImageBitmap(filter.processFilter(imagemFiltro));
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
    }

    private void carregando(boolean estado) {
        if (estado) {
            estaCarregando = true;
            alertDialogCarregarDadosUsuario.show();
        } else {
            estaCarregando = false;
            alertDialogCarregarDadosUsuario.dismiss();
        }
    }

    private void recuperarDadosUsuarioLogado() {
        this.carregando(true);
        usuarioLogadoRef = usuariosRef.child(idUsuarioLogado);
        usuarioLogadoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarioLogado = dataSnapshot.getValue(Usuario.class);
                carregando(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
