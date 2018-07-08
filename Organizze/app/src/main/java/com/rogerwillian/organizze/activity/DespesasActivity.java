package com.rogerwillian.organizze.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rogerwillian.organizze.R;
import com.rogerwillian.organizze.config.FirebaseConfig;
import com.rogerwillian.organizze.helper.Base64Custom;
import com.rogerwillian.organizze.helper.DataCustom;
import com.rogerwillian.organizze.model.Movimentacao;
import com.rogerwillian.organizze.model.Usuario;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private FloatingActionButton botaoSalvar;
    private DatabaseReference firebaseRef = FirebaseConfig.getFirebaseDatabase();
    private FirebaseAuth autenticacao = FirebaseConfig.getFirebaseAuth();
    private Double despesaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);
        campoData.setText(DataCustom.dataAtual());
        botaoSalvar = findViewById(R.id.fabSalvar);

        recuperarDespesaTotal();

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarDespesa(view);
            }
        });
    }

    private void salvarDespesa(View view) {

        if (validarCamposDespesas(view)) {
            String data = campoData.getText().toString();
            Double valor = Double.parseDouble(campoValor.getText().toString());
            Movimentacao movimentacao = new Movimentacao();
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setData(data);
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setValor(valor);
            movimentacao.setTipo("d");

            // Atualizando valor despesaTotal
            Double despesaAtualizada = despesaTotal + valor;
            atualizarDespesa(despesaAtualizada);
            movimentacao.salvar(data);

            Toast.makeText(this, "Despesa lançada com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public Boolean validarCamposDespesas(View view) {
        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if (!textoValor.isEmpty()) {
            if (!textoData.isEmpty()) {
                if (!textoCategoria.isEmpty()) {
                    if (!textoDescricao.isEmpty()) {
                        return true;
                    } else {
                        Snackbar.make(view, "Por favor, informe uma descrição", Snackbar.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Snackbar.make(view, "Por favor, informe uma categoria", Snackbar.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Snackbar.make(view, "Por favor, informe uma data", Snackbar.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Snackbar.make(view, "Por favor, informe um valor", Snackbar.LENGTH_SHORT).show();
            return false;
        }
    }

    public void atualizarDespesa(Double despesaTotal) {
        String idUsuario = Base64Custom.codificar(autenticacao.getCurrentUser().getEmail());
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
        usuarioRef.child("despesaTotal").setValue(despesaTotal);
    }

    public void recuperarDespesaTotal() {
        String idUsuario = Base64Custom.codificar(autenticacao.getCurrentUser().getEmail());
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
