package whatsapp.com.rogerwillian.whatsapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import whatsapp.com.rogerwillian.whatsapp.R;
import whatsapp.com.rogerwillian.whatsapp.config.FirebaseConfig;
import whatsapp.com.rogerwillian.whatsapp.helper.Permissao;
import whatsapp.com.rogerwillian.whatsapp.helper.UsuarioFirebase;
import whatsapp.com.rogerwillian.whatsapp.model.Usuario;

public class ConfiguracoesActivity extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private ImageButton imageButtonCamera, imageButtonGaleria;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private CircleImageView circleImageViewFotoPerfil;
    private StorageReference storageReference;
    private String identificadorUsuario;
    private Usuario usuarioLogado;
    private ProgressBar progressBarUpload;
    private EditText editNome;
    private ImageView imageAtualizarFotoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        imageButtonCamera = findViewById(R.id.imageButtonCamera);
        imageButtonGaleria = findViewById(R.id.imageButtonGaleria);
        circleImageViewFotoPerfil = findViewById(R.id.circleImageViewFotoPerfil);
        editNome = findViewById(R.id.editPerfilNome);
        progressBarUpload = findViewById(R.id.progressBarUpload);
        imageAtualizarFotoUsuario = findViewById(R.id.imageAtualizarFotoUsuario);

        // Configuraçãoes Iniciais
        storageReference = FirebaseConfig.getFirebaseStorage();
        identificadorUsuario = UsuarioFirebase.getIdentificador();
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        // Validar permissoes
        Permissao.validarPermissoes(this, 1, permissoesNecessarias);

        // Configurando toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);

        // Botao voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Recuperando nome e foto do usuario
        this.carregarDadosUsuario();

        // Clique na camera
        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Veficando se é possivel abrir a camera
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, SELECAO_CAMERA);
                }
            }
        });

        // Clique na galeria
        imageButtonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, SELECAO_GALERIA);
                }
            }
        });

        // Clique atualizar foto usuario
        imageAtualizarFotoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = editNome.getText().toString();
                boolean retorno = UsuarioFirebase.atualizarNomeUsuario(nome);
                if (retorno) {
                    usuarioLogado.setNome(nome);
                    usuarioLogado.atualizar();
                    Toast.makeText(ConfiguracoesActivity.this, "Nome alterado com sucesso", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Chamando no retorno de uma intencao
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }

                if (imagem != null) {
                    this.uploadFotoPerfil(imagem);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadFotoPerfil(Bitmap imagem) {
        circleImageViewFotoPerfil.setImageBitmap(imagem);
        progressBarUpload.setVisibility(View.VISIBLE);

        //Recuperar os dados da imagem para o Firebase
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] dadosImagem = baos.toByteArray();

        // Salvar no firebase
        StorageReference imagemRef = storageReference
                .child("imagens")
                .child("perfil")
                .child(identificadorUsuario + ".jpeg");

        UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBarUpload.setVisibility(View.GONE);
                Toast.makeText(ConfiguracoesActivity.this, "Erro ao atualizar foto de perfil", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ConfiguracoesActivity.this, "Foto de perfil atualizada com sucesso!", Toast.LENGTH_SHORT).show();

                Uri url = taskSnapshot.getDownloadUrl();
                atualizarFotoUsuario(url);
                progressBarUpload.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBarUpload.setVisibility(View.VISIBLE);
    }

    public void carregarDadosUsuario() {
        this.progressBarUpload.setVisibility(View.VISIBLE);
        // Recuperando foto
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        Uri uri = usuario.getPhotoUrl();
        if (uri != null) {
            Glide.with(ConfiguracoesActivity.this)
                    .load(uri)
                    .into(circleImageViewFotoPerfil);

            progressBarUpload.setVisibility(View.GONE);
        } else {
            circleImageViewFotoPerfil.setImageResource(R.drawable.padrao);
        }
        // Recuperando nome
        editNome.setText(usuario.getDisplayName());
        editNome.requestFocus();
    }

    public void atualizarFotoUsuario(Uri url) {
        boolean retorno = UsuarioFirebase.atualizarFotoUsuario(url);
        if (retorno) {
            usuarioLogado.setFoto(url.toString());
            usuarioLogado.atualizar();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas =(");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
