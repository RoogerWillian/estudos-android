package instagram.com.rogerwillian.model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import instagram.com.rogerwillian.helper.FirebaseConfig;
import instagram.com.rogerwillian.helper.UsuarioFirebase;

public class Usuario implements Serializable {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String foto;
    private int seguidores = 0;
    private int seguindo = 0;
    private int publicacoes = 0;

    public Usuario() {

    }

    public void salvar() {
        DatabaseReference databaseReference = FirebaseConfig.getFirebaseDatabase();
        databaseReference.child("usuarios")
                .child(getId())
                .setValue(this);
    }

    public void atualizarQtdPostagem() {
        DatabaseReference firebaseRef = FirebaseConfig.getFirebaseDatabase();
        DatabaseReference usuarios = firebaseRef.child("usuarios").child(getId());
        HashMap<String, Object> dados = new HashMap<>();
        dados.put("publicacoes", getPublicacoes());
        usuarios.updateChildren(dados);
    }

    public void atualizar() {
        DatabaseReference firebaseRef = FirebaseConfig.getFirebaseDatabase();
        DatabaseReference usuarios = firebaseRef.child("usuarios").child(getId());
        usuarios.updateChildren(this.converterParaMap());
    }

    private Map<String, Object> converterParaMap() {
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("id", getId());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("email", getEmail());
        usuarioMap.put("foto", getFoto());
        usuarioMap.put("seguidores", getSeguidores());
        usuarioMap.put("seguindo", getSeguindo());
        usuarioMap.put("publicacoes", getPublicacoes());

        return usuarioMap;
    }

    public int getPublicacoes() {
        return publicacoes;
    }

    public void setPublicacoes(int publicacoes) {
        this.publicacoes = publicacoes;
    }

    public int getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }

    public int getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(int seguindo) {
        this.seguindo = seguindo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setNome(String nome) {
        this.nome = nome.toUpperCase();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
