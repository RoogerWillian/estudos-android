package whatsapp.com.rogerwillian.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import whatsapp.com.rogerwillian.whatsapp.R;
import whatsapp.com.rogerwillian.whatsapp.adapter.GrupoSelecionadoAdapter;
import whatsapp.com.rogerwillian.whatsapp.config.FirebaseConfig;
import whatsapp.com.rogerwillian.whatsapp.helper.UsuarioFirebase;
import whatsapp.com.rogerwillian.whatsapp.model.Grupo;
import whatsapp.com.rogerwillian.whatsapp.model.Usuario;

public class CadastroGrupoActivity extends AppCompatActivity {

    private List<Usuario> membrosSelecionados = new ArrayList<>();
    private TextView textTotalParcipantes;
    private GrupoSelecionadoAdapter membrosSelecionadosAdapter;
    private RecyclerView recyclerMembrosSelecionados;
    private CircleImageView imageGrupo;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private Grupo grupo;
    private EditText editNomeGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_grupo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Grupo");
        toolbar.setSubtitle("Adicione um nome");
        setSupportActionBar(toolbar);

        //Configuracoes Iniciais
        textTotalParcipantes = findViewById(R.id.textTotalParcipantes);
        recyclerMembrosSelecionados = findViewById(R.id.recyclerMembrosGrupo);
        imageGrupo = findViewById(R.id.imageGrupo);
        storageReference = FirebaseConfig.getFirebaseStorage();
        editNomeGrupo = findViewById(R.id.editNomeGrupo);
        grupo = new Grupo();

        // Recuperando os membros selecionados
        if (getIntent().getExtras() != null) {
            membrosSelecionados = (List<Usuario>) getIntent().getExtras().getSerializable("membros");
            textTotalParcipantes.setText("Participantes: " + membrosSelecionados.size());
        }

        // Configura o recyclerview os membros selecionados.
        membrosSelecionadosAdapter = new GrupoSelecionadoAdapter(membrosSelecionados, getApplicationContext());
        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMembrosSelecionados.setLayoutManager(layoutManagerHorizontal);
        recyclerMembrosSelecionados.setHasFixedSize(true);
        recyclerMembrosSelecionados.setAdapter(membrosSelecionadosAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, SELECAO_GALERIA);
                }
            }
        });
    }

    public void salvarGrupo(View view) {
        String nomeGrupo = editNomeGrupo.getText().toString();
        membrosSelecionados.add(UsuarioFirebase.getDadosUsuarioLogado());
        grupo.setMembros(membrosSelecionados);
        grupo.setNome(nomeGrupo);
        grupo.salvar();

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chatGrupo", grupo);
        startActivity(intent);

        Toast.makeText(this, "Grupo salvo com sucesso", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {

                Uri localImagemSelecionada = data.getData();
                imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);

                if (imagem != null) {
                    imageGrupo.setImageBitmap(imagem);

                    //Recuperar os dados da imagem para o Firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    // Salvar no firebase
                    StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("grupos")
                            .child(grupo.getId() + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CadastroGrupoActivity.this, "Erro ao atualizar foto de perfil", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(CadastroGrupoActivity.this, "Foto do grupo atualizada com sucesso!", Toast.LENGTH_SHORT).show();

                            String url = taskSnapshot.getDownloadUrl().toString();
                            grupo.setFoto(url);
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
