package com.rogerwillian.organizze.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.rogerwillian.organizze.R;
import com.rogerwillian.organizze.adapter.AdapterMovimentacao;
import com.rogerwillian.organizze.config.FirebaseConfig;
import com.rogerwillian.organizze.helper.Base64Custom;
import com.rogerwillian.organizze.model.Movimentacao;
import com.rogerwillian.organizze.model.Usuario;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private DatabaseReference firebaseRef = FirebaseConfig.getFirebaseDatabase();
    private FirebaseAuth autenticacao = FirebaseConfig.getFirebaseAuth();
    private DatabaseReference movimentacaoRef;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovimentacoes;
    private AdapterMovimentacao adapterMovimentacoes;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private MaterialCalendarView calendarView;
    private Movimentacao movimentacao;
    private String mesAnoSelecionado;
    private DatabaseReference usuarioRef;
    private FloatingActionMenu fam;
    private TextView textSaudacao;
    private TextView textSaldo;
    private RecyclerView recyclerView;
    private Double receitaTotal;
    private Double despesaTotal;
    private Double saldoUsuario;
    private String nomeUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);
        fam = findViewById(R.id.fam);
        calendarView = findViewById(R.id.calendarView);
        textSaudacao = findViewById(R.id.textSaudacao);
        textSaldo = findViewById(R.id.textSaldo);
        recyclerView = findViewById(R.id.recyclerMovimento);
        autenticacao = FirebaseConfig.getFirebaseAuth();
        adapterMovimentacoes = new AdapterMovimentacao(movimentacoes, this);

        configurarCalendarView();
        configurarRecyclerView();
        swipe();
    }

    public void swipe() {

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                int dragsFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(dragsFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        movimentacao = movimentacoes.get(position);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Excluindo movimentação...");
        alert.setMessage("Tem certeza que deseja remover essa movimentação da sua conta ?");
        alert.setCancelable(false);
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(PrincipalActivity.this, "Operação cancelada!", Toast.LENGTH_SHORT).show();
                adapterMovimentacoes.notifyDataSetChanged();
            }
        });
        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String idUsuario = Base64Custom.codificar(autenticacao.getCurrentUser().getEmail());
                movimentacaoRef = firebaseRef.child("movimentacao")
                        .child(idUsuario)
                        .child(mesAnoSelecionado);

                movimentacaoRef.child(movimentacao.getKey()).removeValue();
                adapterMovimentacoes.notifyItemRemoved(position);
                atualizarSaldo(movimentacao);
            }
        });

        alert.create();
        alert.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fam.close(true);
        recuperarResumo();
        recuperarMovimentacoes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_sair:
                autenticacao.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void configurarRecyclerView() {
        recyclerView.setAdapter(adapterMovimentacoes);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    public void atualizarSaldo(Movimentacao movimentacao) {
        String idUsuario = Base64Custom.codificar(autenticacao.getCurrentUser().getEmail());
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
        String tipo = movimentacao.getTipo();

        if (tipo.equals("r")) {
            receitaTotal = receitaTotal - movimentacao.getValor();
            usuarioRef.child("receitaTotal").setValue(receitaTotal);
        } else if (tipo.equals("d")) {
            despesaTotal = despesaTotal - movimentacao.getValor();
            usuarioRef.child("despesaTotal").setValue(despesaTotal);
        }
    }

    public void recuperarResumo() {
        String idUsuario = Base64Custom.codificar(autenticacao.getCurrentUser().getEmail());
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                nomeUsuario = usuario.getNome();
                receitaTotal = usuario.getReceitaTotal();
                despesaTotal = usuario.getDespesaTotal();
                saldoUsuario = receitaTotal - despesaTotal;

                String resumoFormatado = new DecimalFormat("0.##").format(saldoUsuario);
                textSaudacao.setText("Olá, " + nomeUsuario);
                textSaldo.setText("R$ " + resumoFormatado);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void recuperarMovimentacoes() {
        String idUsuario = Base64Custom.codificar(autenticacao.getCurrentUser().getEmail());
        movimentacaoRef = firebaseRef.child("movimentacao")
                .child(idUsuario)
                .child(mesAnoSelecionado);

        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                movimentacoes.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                    movimentacao.setKey(dados.getKey());
                    movimentacoes.add(movimentacao);
                }
                adapterMovimentacoes.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void configurarCalendarView() {
        CharSequence[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        calendarView.setTitleMonths(meses);
        CalendarDay dataAtual = calendarView.getCurrentDate();
        String mesFormatado = String.format("%02d", (dataAtual.getMonth() + 1));
        mesAnoSelecionado = mesFormatado + "" + String.valueOf(dataAtual.getYear());
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesFormatado = String.format("%02d", (date.getMonth() + 1));
                mesAnoSelecionado = mesFormatado + "" + String.valueOf(date.getYear());

                movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
                recuperarMovimentacoes();
            }
        });
    }

    public void adicionarDespesa(View view) {
        startActivity(new Intent(this, DespesasActivity.class));
    }

    public void adicionarReceita(View view) {
        startActivity(new Intent(this, ReceitasActivity.class));
    }
}
