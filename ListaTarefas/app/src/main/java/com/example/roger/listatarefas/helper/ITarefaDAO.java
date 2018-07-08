package com.example.roger.listatarefas.helper;

import com.example.roger.listatarefas.model.Tarefa;

import java.util.List;

public interface ITarefaDAO {

    public boolean salvar(Tarefa tarefa);

    public boolean atualizar(Tarefa tarefa);

    public boolean deletar(Tarefa tarefa);

    public List<Tarefa> listar();

}
