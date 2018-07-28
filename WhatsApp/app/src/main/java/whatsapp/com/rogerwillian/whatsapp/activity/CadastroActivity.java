package whatsapp.com.rogerwillian.whatsapp.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import whatsapp.com.rogerwillian.whatsapp.R;
import whatsapp.com.rogerwillian.whatsapp.config.FirebaseConfig;
import whatsapp.com.rogerwillian.whatsapp.helper.Base64Custom;
import whatsapp.com.rogerwillian.whatsapp.helper.MessageHelper;
import whatsapp.com.rogerwillian.whatsapp.helper.UsuarioFirebase;
import whatsapp.com.rogerwillian.whatsapp.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText etNome, etEmail, etSenha;
    private FirebaseAuth auth;
    private Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        auth = FirebaseConfig.getFirebaseAuth();
        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
    }

    public void validarUsuario(View view) {
        String nome = etNome.getText().toString();
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();

        if (!nome.isEmpty()) {
            if (!email.isEmpty()) {
                if (!senha.isEmpty()) {
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setSenha(senha);
                    usuario.setId(Base64Custom.codificar(usuario.getEmail()));
                    this.cadastrarUsuario();
                } else {
                    MessageHelper.exibirSnackbar(view, "Por favor, informe a senha", Snackbar.LENGTH_LONG);
                }
            } else {
                MessageHelper.exibirSnackbar(view, "Por favor, informe o email", Snackbar.LENGTH_LONG);
            }
        } else {
            MessageHelper.exibirSnackbar(view, "Por favor, informe o nome", Snackbar.LENGTH_LONG);
        }
    }

    private void cadastrarUsuario() {
        try {
            auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                usuario.salvar();
                                Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                                UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());
                                finish();
                            } else {
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
}
