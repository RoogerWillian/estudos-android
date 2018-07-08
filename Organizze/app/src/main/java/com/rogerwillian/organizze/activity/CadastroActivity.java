package com.rogerwillian.organizze.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.rogerwillian.organizze.R;
import com.rogerwillian.organizze.config.FirebaseConfig;
import com.rogerwillian.organizze.helper.Base64Custom;
import com.rogerwillian.organizze.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText etNome;
    private EditText etEmail;
    private EditText etSenha;
    private Button buttonCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private View layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        this.etNome = findViewById(R.id.etNome);
        this.etEmail = findViewById(R.id.etEmail);
        this.etSenha = findViewById(R.id.etSenha);
        layout = findViewById(R.id.layout);
        this.buttonCadastrar = findViewById(R.id.buttonCadastrar);
        this.buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoNome = etNome.getText().toString();
                String textoEmail = etEmail.getText().toString();
                String textoSenha = etSenha.getText().toString();

                if (!textoNome.isEmpty()) {
                    if (!textoEmail.isEmpty()) {
                        if (!textoSenha.isEmpty()) {
                            usuario = new Usuario();
                            usuario.setNome(textoNome);
                            usuario.setEmail(textoEmail);
                            usuario.setSenha(textoSenha);

                            cadastrarUsuario(view);
                        } else {
                            Snackbar.make(view, "Por favor, preencha a senha", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        Snackbar.make(view, "Por favor, preencha o e-mail", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(view, "Por favor, preencha o nome", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        etNome.requestFocus();
    }

    public void cadastrarUsuario(View view) {
        autenticacao = FirebaseConfig.getFirebaseAuth();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String idUsuario = Base64Custom.codificar(usuario.getEmail());
                            usuario.setIdUsuario(idUsuario);
                            usuario.salvar();

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

                            Snackbar.make(layout, excecao, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
