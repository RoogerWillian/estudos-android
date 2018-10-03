package instagram.com.rogerwillian.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import instagram.com.rogerwillian.R;
import instagram.com.rogerwillian.helper.FirebaseConfig;
import instagram.com.rogerwillian.helper.MessageHelper;
import instagram.com.rogerwillian.helper.UsuarioFirebase;
import instagram.com.rogerwillian.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Configurações Iniciais
        campoNome = findViewById(R.id.editLoginEmail);
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editLoginSenha);
        progressBar = findViewById(R.id.progressCadastro);
        progressBar.setVisibility(View.GONE);
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
    }

    public void solicitarCadastroUsuario(View view) {
        if (validarCadastro(view)) {
            progressBar.setVisibility(View.VISIBLE);
            usuario = new Usuario();
            usuario.setNome(campoNome.getText().toString());
            usuario.setEmail(campoEmail.getText().toString());
            usuario.setSenha(campoSenha.getText().toString());
            this.cadastrar();
        }
    }

    private void cadastrar() {
        try {

            firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                try {
                                    //Salvar dados no firebase
                                    String idUsuario = task.getResult().getUser().getUid();
                                    usuario.setId(idUsuario);
                                    usuario.salvar();

                                    // Salvar dados no profile do Firebase
                                    UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());

                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();

                                    // Enviando para activity principal
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                progressBar.setVisibility(View.GONE);
                                String excecao;

                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    excecao = "Por favor, insira uma senha mais forte";
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    excecao = "Por favor, insira um e-mail válido";
                                } catch (FirebaseAuthUserCollisionException e) {
                                    excecao = "E-mail já está em uso, tente outro!";
                                } catch (Exception e) {
                                    excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                                    e.printStackTrace();
                                }

                                MessageHelper.exibirSnackbar(findViewById(R.id.layoutCadastro), excecao, Snackbar.LENGTH_SHORT);
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validarCadastro(View view) {
        String nome = campoNome.getText().toString();
        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();
        boolean retorno = false;

        if (!nome.isEmpty()) {
            if (!email.isEmpty()) {
                if (!senha.isEmpty()) {
                    retorno = true;
                } else {
                    MessageHelper.exibirSnackbar(view, "Por favor, informe a senha", Snackbar.LENGTH_SHORT);
                    campoSenha.requestFocus();
                }
            } else {
                MessageHelper.exibirSnackbar(view, "Por favor, informe o email", Snackbar.LENGTH_SHORT);
                campoEmail.requestFocus();
            }
        } else {
            MessageHelper.exibirSnackbar(view, "Por favor, informe o nome", Snackbar.LENGTH_SHORT);
            campoNome.requestFocus();
        }

        return retorno;
    }
}
