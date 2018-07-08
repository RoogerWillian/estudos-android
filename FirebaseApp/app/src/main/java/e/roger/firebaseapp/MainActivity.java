package e.roger.firebaseapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    //private FirebaseAuth usuario = FirebaseAuth.getInstance();

    private ImageView imageFoto;
    private Button buttonUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageFoto = findViewById(R.id.imageFoto);
        buttonUpload = findViewById(R.id.buttonUpload);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Configura para a imagem ser salva em memoria
                imageFoto.setDrawingCacheEnabled(true);
                imageFoto.buildDrawingCache();

                //Recupar bitmap da imagem(imagem a ser carregada)
                Bitmap bitmap = imageFoto.getDrawingCache();

                //Comprimo bitmap para o um formato png/jpeg
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);

                //Converte o baos para pixel brutos em uma matriz de bytes(dados da imagem)
                byte[] dadosImagem = baos.toByteArray();

                // Define nós para o storage
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference imagens = storageReference.child("imagens");
                //String nomeArquivo = UUID.randomUUID().toString();
                StorageReference imagemRef = imagens.child("celular.jpeg");

                //Deletar um arquivo
                /*imagemRef.delete().addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Erro ao deletar arquivo", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Sucesso ao deletar o arquivo", Toast.LENGTH_SHORT).show();
                    }
                });*/

                // Retorna objeto que irá controlar o upload
                /*UploadTask uploadTask = imagemRef.putBytes(dadosImagem);

                uploadTask.addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Problema ao fazer upload: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri url = taskSnapshot.getDownloadUrl();
                        Toast.makeText(MainActivity.this, "Sucesso ao fazer upload: " + url.toString(), Toast.LENGTH_SHORT).show();
                    }
                });*/

                //Baixando imagem

            }
        });

        DatabaseReference usuarios = referencia.child("usuarios");
        /* Pesquisando por Nó */
        /*DatabaseReference usuarioPesquisa = usuarios.child("-LFwpdFoR9mr5aperzQa");*/
        //Query usuarioPesquisa = usuarios.orderByChild("nome").equalTo("Enes");
        //Query usuarioPesquisa = usuarios.orderByKey().limitToFirst(2);
        //Query usuarioPesquisa = usuarios.orderByKey().limitToLast(2);

        /* Maior ou igual */
        //Query usuarioPesquisa = usuarios.orderByChild("idade").startAt(40);

        /* Menor ou igual */
        //Query usuarioPesquisa = usuarios.orderByChild("idade").endAt(25);

        /* Entre dois valores */
        //Query usuarioPesquisa = usuarios.orderByChild("idade").startAt(18).endAt(30);

        /* Filtrar palavras */
        /*Query usuarioPesquisa = usuarios.orderByChild("nome").startAt("Roger").endAt("Roger" + "\uf8ff");

        usuarioPesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Usuario dadosUsuario = dataSnapshot.getValue(Usuario.class);
                //Log.i("FIREBASE_PESQUISA", dadosUsuario.toString());
                Log.i("FIREBASE_PESQUISA", dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        /*Usuario usuario = new Usuario();
        usuario.setNome("Natalia");
        usuario.setSobrenome("Pereira de Oliveira");
        usuario.setIdade(24);
        usuarios.push().setValue(usuario);*/

        /* Deslogar o usuario */
        //usuario.signOut();

        /* Logando usuário */

        /*usuario.signInWithEmailAndPassword("roger.nizoli@gmail.com", "roger1231")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Sucesso ao logar usuário", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Erro ao logar o usuário", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

        /* Verifica usuario logado
        if (usuario.getCurrentUser() != null) {
            Toast.makeText(this, "Usuario Logado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Usuario não logado", Toast.LENGTH_SHORT).show();
        }*/

        /* Cadastrando um usuario */
        /*usuario.createUserWithEmailAndPassword("roger.nizoli@gmail.com", "roger1231")
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Sucesso ao cadastradar usuário", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Erro ao cadastrar o usuário", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

        /*DatabaseReference usuarios = referencia.child("usuarios").child("001");
        DatabaseReference produtos = referencia.child("produtos");*/

        // Reuperando dados no firebase
        /*usuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("FIREBASE_RETORNO", dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /* Salvando dados no Firebase*/
/*        Usuario usuario = new Usuario();
        usuario.setNome("Roger Willian");
        usuario.setSobrenome("Nizoli Rocha");
        usuario.setIdade(23);
        usuarios.push().setValue(usuario);

        Produto produto = new Produto();
        produto.setDescricao("Moto G5 Plus");
        produto.setMarca("Motorola/Lenovo");
        produto.setPreco((float) 850.00);
        produtos.child("002").setValue(produto);*/


    }
}
