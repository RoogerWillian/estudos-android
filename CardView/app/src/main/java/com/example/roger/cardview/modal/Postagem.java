package com.example.roger.cardview.modal;

public class Postagem {

    private String usuario;
    private String descricao;
    private int imagem;

    public Postagem(String usuario, String descricao, int imagem) {
        this.usuario = usuario;
        this.descricao = descricao;
        this.imagem = imagem;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }
}
