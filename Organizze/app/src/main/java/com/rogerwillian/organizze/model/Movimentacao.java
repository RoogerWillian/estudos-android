package com.rogerwillian.organizze.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.rogerwillian.organizze.config.FirebaseConfig;
import com.rogerwillian.organizze.helper.Base64Custom;
import com.rogerwillian.organizze.helper.DataCustom;

public class Movimentacao {

    private String key;
    private String data;
    private String categoria;
    private String tipo;
    private String descricao;
    private double valor;

    public Movimentacao() {
    }

    public void salvar(String dataEscolhida) {

        FirebaseAuth autenticacao = FirebaseConfig.getFirebaseAuth();
        DatabaseReference firebase = FirebaseConfig.getFirebaseDatabase();
        String idUsuario = Base64Custom.codificar(autenticacao.getCurrentUser().getEmail());
        String mesAno = DataCustom.mesAnoDataEscolhida(dataEscolhida);
        firebase.child("movimentacao")
                .child(idUsuario)
                .child(mesAno)
                .push()
                .setValue(this);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
