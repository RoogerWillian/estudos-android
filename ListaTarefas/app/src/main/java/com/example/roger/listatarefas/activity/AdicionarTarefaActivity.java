package com.example.roger.listatarefas.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.roger.listatarefas.R;
import com.example.roger.listatarefas.helper.TarefaDAO;
import com.example.roger.listatarefas.model.Tarefa;

public class AdicionarTarefaActivity extends AppCompatActivity {

    private TextInputEditText editTarefa;
    private Tarefa tarefaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);
        editTarefa = findViewById(R.id.textTarefa);

        //Recuperar tarefa, caso seja edição.
        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");

        if (tarefaAtual != null) {
            getSupportActionBar().setTitle("Edição tarefa");
            editTarefa.setText(tarefaAtual.getNome());
        } else {
            getSupportActionBar().setTitle("Nova tarefa");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_tarefa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_salvar:
                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

                if (tarefaAtual != null) {
                    String nomeTarefa = this.editTarefa.getText().toString();
                    if (!nomeTarefa.isEmpty()) {
                        Tarefa tarefa = new Tarefa();
                        tarefa.setId(tarefaAtual.getId());
                        tarefa.setNome(nomeTarefa);
                        if (tarefaDAO.atualizar(tarefa)) {
                            Toast.makeText(this, "Tarefa atualizada com sucesso", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Erro ao atualizar tarefa", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Por favor, digite o nome da tarefa", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Tarefa tarefa = new Tarefa();
                    String nomeTarefa = this.editTarefa.getText().toString();
                    if (!nomeTarefa.isEmpty()) {
                        tarefa.setNome(nomeTarefa);
                        if (tarefaDAO.salvar(tarefa)) {
                            Toast.makeText(this, "Tarefa salva com sucesso", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Erro ao salva tarefa", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Por favor, digite o nome da tarefa", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
