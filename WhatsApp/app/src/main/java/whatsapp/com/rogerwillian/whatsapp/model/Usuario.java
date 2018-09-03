package whatsapp.com.rogerwillian.whatsapp.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import whatsapp.com.rogerwillian.whatsapp.config.FirebaseConfig;
import whatsapp.com.rogerwillian.whatsapp.helper.UsuarioFirebase;

public class Usuario implements Serializable {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String foto;

    public Usuario() {

    }

    public void salvar() {
        DatabaseReference databaseReference = FirebaseConfig.getFirebaseDatabase();
        databaseReference.child("usuarios")
                .child(this.id)
                .setValue(this);
    }

    public void atualizar() {
        String identificadorUsuario = UsuarioFirebase.getIdentificador();
        DatabaseReference databaseReference = FirebaseConfig.getFirebaseDatabase();

        // Atualizando no bd
        DatabaseReference usuariosRef = databaseReference.child("usuarios").child(identificadorUsuario);

        usuariosRef.updateChildren(converterParaMap());
    }

    @Exclude
    private Map<String, Object> converterParaMap() {
        HashMap<String, Object> usuarioMap = new HashMap<>();

        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("foto", getFoto());

        return usuarioMap;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Exclude
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
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
