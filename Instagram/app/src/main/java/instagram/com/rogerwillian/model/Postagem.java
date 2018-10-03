package instagram.com.rogerwillian.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;

import instagram.com.rogerwillian.helper.FirebaseConfig;

public class Postagem implements Serializable {

    public static final String POSTAGENS_REF = "postagens";
    private String id;
    private String idUsuario;
    private String descricao;
    private String caminhoFoto;

    public Postagem() {
        DatabaseReference firebaseRef = FirebaseConfig.getFirebaseDatabase();
        DatabaseReference postagens = firebaseRef.child(POSTAGENS_REF);
        setId(postagens.push().getKey());
    }

    public boolean salvar() {

        DatabaseReference firebaseRef = FirebaseConfig.getFirebaseDatabase();
        DatabaseReference postagensRef = firebaseRef.child(POSTAGENS_REF).child(getIdUsuario()).child(getId());
        postagensRef.setValue(this);

        return true;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
