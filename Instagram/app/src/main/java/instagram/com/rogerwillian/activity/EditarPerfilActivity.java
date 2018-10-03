package instagram.com.rogerwillian.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import instagram.com.rogerwillian.R;
import instagram.com.rogerwillian.helper.FirebaseConfig;
import instagram.com.rogerwillian.helper.MessageHelper;
import instagram.com.rogerwillian.helper.Permissao;
import instagram.com.rogerwillian.helper.TextUtil;
import instagram.com.rogerwillian.helper.UsuarioFirebase;
import instagram.com.rogerwillian.model.Usuario;

public class EditarPerfilActivity extends AppCompatActivity {

    private static final int SELECAO_GALERIA = 200;
    private CircleImageView imageEditarPerfil;
    private TextInputEditText editNomePerfil, editEmailPerfil;
    private Usuario usuarioLogado;
    private StorageReference storageRef;
    private String identificadorUsuario;
    private String[] permissoesNecessarias = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        Permissao.validarPermissoes(permissoesNecessarias, this, 1);

        //Configurações Iniciais
        this.iniciarComponentes();

        //Configurando  Toolbar
        this.configurarToolbar();

        //Recuperando dados do usuario logado
        this.recuparDadosUsuario();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {
                // Selecao apenas da galeria
                switch (requestCode) {
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }

                if (imagem != null) {
                    // ConfiguraImagem na tela
                    this.imageEditarPerfil.setImageBitmap(imagem);

                    // Atualiza no firebase
                    this.iniciarAlteracaoFoto(imagem);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void iniciarAlteracaoFoto(Bitmap imagem) {
        StorageReference imagensRef = storageRef.child("imagens").child("perfil").child(identificadorUsuario + ".jpeg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] dadosImagem = baos.toByteArray();
        UploadTask uploadTask = imagensRef.putBytes(dadosImagem);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarPerfilActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //Recupear local da foto
                Uri url = taskSnapshot.getDownloadUrl();
                atualizarFotoUsuario(url);

                Toast.makeText(EditarPerfilActivity.this, "Sua foto foi atualizada =)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void atualizarFotoUsuario(Uri url) {
        //Atualizar foto no perfil
        UsuarioFirebase.atualizarFotoUsuario(url);

        //Atualizar foto no Firebase
        usuarioLogado.setFoto(url.toString());
        usuarioLogado.atualizar();
    }

    public void solicitarAlteracaoFoto(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(intent, SELECAO_GALERIA);
        else
            Toast.makeText(this, "Problema ao abrir galeria =(", Toast.LENGTH_SHORT).show();
    }

    public void salvarAlteracoes(View view) {

        String nomeAtualizado = editNomePerfil.getText().toString();
        UsuarioFirebase.atualizarNomeUsuario(nomeAtualizado);
        usuarioLogado.setNome(nomeAtualizado);
        usuarioLogado.atualizar();

        MessageHelper.exibirSnackbar(view, "Informações atualizados com sucesso", Snackbar.LENGTH_LONG);
    }

    // Ação quando botao voltar for pressionar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void recuparDadosUsuario() {
        FirebaseUser usuarioAtual = UsuarioFirebase.getUsuarioAtual();
        editNomePerfil.setText(TextUtil.capitalize(usuarioAtual.getDisplayName().toLowerCase()));
        editEmailPerfil.setText(usuarioAtual.getEmail());
        editNomePerfil.setSelection(editNomePerfil.getText().length());

        Uri url = usuarioAtual.getPhotoUrl();
        if (url != null) {
            Glide.with(EditarPerfilActivity.this)
                    .load(url)
                    .into(imageEditarPerfil);
        } else {
            imageEditarPerfil.setImageResource(R.drawable.avatar);
        }
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Editar perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
    }

    private void iniciarComponentes() {

        this.imageEditarPerfil = findViewById(R.id.imageEditarPerfil);
        this.editNomePerfil = findViewById(R.id.editNomePerfil);
        this.editEmailPerfil = findViewById(R.id.editEmailPerfil);
        this.usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        this.storageRef = FirebaseConfig.getFirebaseStorage();
        this.identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        this.editEmailPerfil.setFocusable(false);

    }
}
