package com.rogerwillian.organizze.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ReceitasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private DatabaseReference firebaseRef = FirebaseConfig.getFirebaseDatabase();
    private FirebaseAuth autenticacao = FirebaseConfig.getFirebaseAuth();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        campoValor = findViewById(R.id.editValorReceita);
        campoData = findViewById(R.id.editDataReceita);
        campoCategoria = findViewById(R.id.editCategoriaReceita);
        campoDescricao = findViewById(R.id.editDescricaoReceita);
        campoData.setText(DataCustom.dataAtual());

        recuperarReceitaTotal();
    }

    public void salvarReceita(View view) {
        if (validarCamposDespesas(view)) {
            String data = campoData.getText().toString();
            Double valor = Double.parseDouble(campoValor.getText().toString());
            Movimentacao movimentacao = new Movimentacao();
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setData(data);
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setValor(valor);
            movimentacao.setTipo("r");

            // Atualizando valor despesaTotal
            Double despesaAtualizada = receitaTotal + valor;
            atualizarReceita(despesaAtualizada);
            movimentacao.salvar(data);

            Toast.makeText(this, "Receita lançada com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void atualizarReceita(Double receitaTotal) {
        String idUsuario = Base64Custom.codificar(autenticacao.getCurrentUser().getEmail());
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
        usuarioRef.child("receitaTotal").setValue(receitaTotal);
    }

    public void recuperarReceitaTotal() {
        String idUsuario = Base64Custom.codificar(autenticacao.getCurrentUser().getEmail());
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
}
