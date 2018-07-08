package com.example.roger.minhaanotacoes;

import android.content.Context;
import android.content.SharedPreferences;

public class AnotacoesPreferences {

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String NOME_ARQUIVO = "anotacao.preferencias";
    private static final String CHANVE_NOME = "nome";

    public AnotacoesPreferences(Context context) {
        this.context = context;
        this.preferences = this.context.getSharedPreferences(NOME_ARQUIVO, 0);
        this.editor = this.preferences.edit();
    }

    public void salvarAnotacao(String anotacao) {
        editor.putString(CHANVE_NOME, anotacao);
        editor.commit();
    }

    public String recuperarAnotacao() {
        return preferences.getString(CHANVE_NOME, "");
    }
}
