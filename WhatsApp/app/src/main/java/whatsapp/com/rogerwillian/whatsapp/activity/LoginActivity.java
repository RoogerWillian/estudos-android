package whatsapp.com.rogerwillian.whatsapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import whatsapp.com.rogerwillian.whatsapp.R;
import whatsapp.com.rogerwillian.whatsapp.config.FirebaseConfig;
import whatsapp.com.rogerwillian.whatsapp.helper.MessageHelper;
import whatsapp.com.rogerwillian.whatsapp.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail, campoSenha;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.campoEmail = findViewById(R.id.etLoginEmail);
        this.campoSenha = findViewById(R.id.etLoginSenha);
        this.auth = FirebaseConfig.getFirebaseAuth();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if (usuarioAtual != null)
            abrirTelaPrincipal();
    }

    public void logarUsuario(Usuario usuario) {
        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            abrirTelaPrincipal();
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

                            MessageHelper.exibirSnackbar(findViewById(R.id.layoutLogin), excecao, Snackbar.LENGTH_SHORT);
                        }

                    }
                });
    }

    public void validarAutenticacaoUsuario(View view) {
        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();

        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setSenha(senha);
                logarUsuario(usuario);
            } else {
                MessageHelper.exibirSnackbar(view, "Por favor, informe a senha", Snackbar.LENGTH_LONG);
            }
        } else {
            MessageHelper.exibirSnackbar(view, "Por favor, informe o e-mail", Snackbar.LENGTH_LONG);
        }
    }

    public void abrirTelaCadastro(View view) {
        startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
    }

    public void abrirTelaPrincipal() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

}
