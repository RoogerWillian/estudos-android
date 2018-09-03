package whatsapp.com.rogerwillian.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import whatsapp.com.rogerwillian.whatsapp.R;
import whatsapp.com.rogerwillian.whatsapp.adapter.MensagensAdapter;
import whatsapp.com.rogerwillian.whatsapp.config.FirebaseConfig;
import whatsapp.com.rogerwillian.whatsapp.helper.Base64Custom;
import whatsapp.com.rogerwillian.whatsapp.helper.UsuarioFirebase;
import whatsapp.com.rogerwillian.whatsapp.model.Conversa;
import whatsapp.com.rogerwillian.whatsapp.model.Grupo;
import whatsapp.com.rogerwillian.whatsapp.model.Mensagem;
import whatsapp.com.rogerwillian.whatsapp.model.Usuario;

public class ChatActivity extends AppCompatActivity {

    private CircleImageView circleImageViewChat;
    private TextView textViewNomeChat;
    private Usuario usuarioDestinatario;
    private Usuario usuarioRemetente;
    private EditText editTextMensagem;
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;
    private RecyclerView recyclerMensagens;
    private MensagensAdapter adapter;
    private List<Mensagem> mensagens = new ArrayList<>();
    private DatabaseReference database;
    private StorageReference storage;
    private DatabaseReference mensagensRef;
    private ChildEventListener childEventListenerMensagens;
    private Grupo grupo;
    private static final int SELECAO_CAMERA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configuração iniciais
        circleImageViewChat = findViewById(R.id.circleImageFoto);
        textViewNomeChat = findViewById(R.id.textViewNomeChat);
        editTextMensagem = findViewById(R.id.editMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);
        idUsuarioRemetente = UsuarioFirebase.getIdentificador();
        usuarioRemetente = UsuarioFirebase.getDadosUsuarioLogado();

        // Recuperar os dados do usuario destinario
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (bundle.containsKey("chatGrupo")) {

                grupo = (Grupo) bundle.getSerializable("chatGrupo");
                idUsuarioDestinatario = grupo.getId();
                textViewNomeChat.setText(grupo.getNome());
                String foto = grupo.getFoto();
                if (foto != null) {
                    Glide.with(ChatActivity.this)
                            .load(Uri.parse(foto))
                            .into(circleImageViewChat);
                } else {
                    circleImageViewChat.setImageResource(R.drawable.padrao);
                }

            } else {
                usuarioDestinatario = (Usuario) bundle.getSerializable("chatContato");
                idUsuarioDestinatario = Base64Custom.codificar(usuarioDestinatario.getEmail());
                textViewNomeChat.setText(usuarioDestinatario.getNome());

                String foto = usuarioDestinatario.getFoto();
                if (foto != null) {
                    Glide.with(ChatActivity.this)
                            .load(Uri.parse(foto))
                            .into(circleImageViewChat);
                } else {
                    circleImageViewChat.setImageResource(R.drawable.padrao);
                }
            }
        }

        // Configurando recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerMensagens.setLayoutManager(layoutManager);
        recyclerMensagens.setHasFixedSize(true);

        // Configurando adapter
        adapter = new MensagensAdapter(mensagens, getApplicationContext());
        recyclerMensagens.setAdapter(adapter);

        database = FirebaseConfig.getFirebaseDatabase();
        storage = FirebaseConfig.getFirebaseStorage();
        mensagensRef = database.child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);
    }

    public void enviarImagem(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, SELECAO_CAMERA);
        }
    }

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
                }

                if (imagem != null) {

                    //Recuperar os dados da imagem para o Firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    String nomeImagem = UUID.randomUUID().toString();

                    // Configurar referencia do firebase
                    StorageReference imagemRef = storage.child("imagens").child("fotos").child(idUsuarioRemetente).child(nomeImagem + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatActivity.this, "Erro ao fazer upload" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                            Mensagem mensagem = new Mensagem();
                            mensagem.setIdUsuario(idUsuarioRemetente);
                            mensagem.setMensagem("imagem.jpeg");
                            mensagem.setImagem(downloadUrl);

                            salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);
                            salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

                            Toast.makeText(ChatActivity.this, "Imagem enviada com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void enviarMensagem(View view) {
        String textoMensagem = editTextMensagem.getText().toString();
        if (!textoMensagem.isEmpty()) {

            if (usuarioDestinatario != null) {
                Mensagem mensagem = new Mensagem();
                mensagem.setIdUsuario(idUsuarioRemetente);
                mensagem.setMensagem(textoMensagem);

                // Salvando mensagem para o remetente
                this.salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

                // Salvando mensagem para o destinario
                this.salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

                // Salvando conversa para o remetente
                this.salvarConversa(idUsuarioRemetente, idUsuarioDestinatario, usuarioDestinatario, mensagem, false);

                // Salvando conversa para o destinario
                this.salvarConversa(idUsuarioDestinatario, idUsuarioRemetente, usuarioRemetente, mensagem, false);
            } else {

                for (Usuario membro : grupo.getMembros()) {

                    String idRemetenteGrupo = Base64Custom.codificar(membro.getEmail());
                    String idUsuarioLogadoGrupo = UsuarioFirebase.getIdentificador();

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioLogadoGrupo);
                    mensagem.setMensagem(textoMensagem);
                    mensagem.setNome(usuarioRemetente.getNome());

                    // Salvando mensagem para o membro
                    this.salvarMensagem(idRemetenteGrupo, idUsuarioDestinatario, mensagem);

                    // Salvando conversa para o membro
                    this.salvarConversa(idRemetenteGrupo, idUsuarioDestinatario, usuarioDestinatario, mensagem, true);
                }

            }

        } else {
            Toast.makeText(this, "Digite uma mensagem para enviar!", Toast.LENGTH_SHORT).show();
        }
    }

    private void salvarConversa(String idRemetente, String idDestinatario, Usuario usuarioExibicao, Mensagem mensagem, boolean isGroup) {
        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdRemetente(idRemetente);
        conversaRemetente.setIdDestinatario(idDestinatario);
        conversaRemetente.setUltimaMensagem(mensagem.getMensagem());

        if (isGroup) {
            conversaRemetente.setIsGroup("true");
            conversaRemetente.setGrupo(grupo);
        } else {
            conversaRemetente.setUsuarioExibicao(usuarioExibicao);
            conversaRemetente.setIsGroup("false");
        }

        conversaRemetente.salvar();
    }

    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem msg) {
        DatabaseReference database = FirebaseConfig.getFirebaseDatabase();
        DatabaseReference mensagemRef = database.child("mensagens");

        mensagemRef.child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(msg);

        editTextMensagem.setText("");
        editTextMensagem.requestFocus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagens();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener(childEventListenerMensagens);
    }

    private void recuperarMensagens() {
        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Mensagem mensagem = dataSnapshot.getValue(Mensagem.class);
                mensagens.add(mensagem);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Mensagem mensagem = dataSnapshot.getValue(Mensagem.class);
                mensagens.add(mensagem);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Mensagem mensagem = dataSnapshot.getValue(Mensagem.class);
                mensagens.add(mensagem);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
