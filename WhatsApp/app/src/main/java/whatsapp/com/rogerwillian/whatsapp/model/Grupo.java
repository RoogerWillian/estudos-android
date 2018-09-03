package whatsapp.com.rogerwillian.whatsapp.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

import whatsapp.com.rogerwillian.whatsapp.config.FirebaseConfig;
import whatsapp.com.rogerwillian.whatsapp.helper.Base64Custom;

public class Grupo implements Serializable {

    private String id;
    private String nome;
    private String foto;
    private List<Usuario> membros;

    public Grupo() {
        DatabaseReference database = FirebaseConfig.getFirebaseDatabase();
        DatabaseReference grupoRef = database.child("grupos");
        setId(grupoRef.push().getKey());
    }

    public void salvar() {
        DatabaseReference database = FirebaseConfig.getFirebaseDatabase();
        DatabaseReference grupoRef = database.child("grupos");
        grupoRef.child(getId()).setValue(this);

        // Salvar conversa para membros do grupo
        for (Usuario membro : getMembros()) {
            String idRemetente = Base64Custom.codificar(membro.getEmail());
            Conversa conversa = new Conversa();
            conversa.setIsGroup("true");
            conversa.setGrupo(this);
            conversa.setIdRemetente(idRemetente);
            conversa.setIdDestinatario(getId());
            conversa.setUltimaMensagem("");

            conversa.salvar();
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void setMembros(List<Usuario> membros) {
        this.membros = membros;
    }
}
