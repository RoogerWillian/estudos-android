package instagram.com.rogerwillian.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import instagram.com.rogerwillian.R;
import instagram.com.rogerwillian.helper.FirebaseConfig;
import instagram.com.rogerwillian.helper.MessageHelper;
import instagram.com.rogerwillian.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressLogin;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verificaUsuarioLogado();
        inicializarComponentes();
    }

    private void verificaUsuarioLogado() {
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    public void abrirCadastro(View view) {
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }

    public void logarUsuario(View view) {
        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();

        if (validarCampos(view)) {
            usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setSenha(senha);
            validarLogin(usuario);
        }

    }

    private void inicializarComponentes() {
        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);
        progressLogin = findViewById(R.id.progressLogin);
        progressLogin.setVisibility(View.GONE);
    }

    private void validarLogin(Usuario usuario) {
        progressLogin.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressLogin.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    String excecao;

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Usuário não está cadastrado.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "E-mail ou senha inválidos";
                    } catch (Exception e) {
                        excecao = "Erro ao logar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }
                    progressLogin.setVisibility(View.GONE);
                    MessageHelper.exibirSnackbar(findViewById(R.id.layoutLogin), excecao, Snackbar.LENGTH_SHORT);
                }
            }
        });
    }

    private boolean validarCampos(View view) {
        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();
        boolean retorno = false;

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


        return retorno;
    }
}
