package instagram.com.rogerwillian.helper;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import instagram.com.rogerwillian.model.Usuario;

public class UsuarioFirebase {

    public static FirebaseUser getUsuarioAtual() {
        return FirebaseConfig.getFirebaseAuth().getCurrentUser();
    }

    public static void atualizarNomeUsuario(String nome) {
        try {

            // Usuario logado no app
            FirebaseUser usuarioAtual = getUsuarioAtual();

            // Configurar objeto para alteração do perfil
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();
            usuarioAtual.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful())
                        Log.d("PEFIL", "Erro ao atualizar nome de usuario: " + task.getException().getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void atualizarFotoUsuario(Uri url) {
        try {

            // Usuario logado no app
            FirebaseUser usuarioAtual = getUsuarioAtual();

            // Configurar objeto para alteração do perfil
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();
            usuarioAtual.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful())
                        Log.d("PEFIL", "Erro ao atualizar foto de perfil: " + task.getException().getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Usuario getDadosUsuarioLogado() {

        FirebaseUser usuarioAtual = getUsuarioAtual();

        Usuario usuario = new Usuario();
        usuario.setId(usuarioAtual.getUid());
        usuario.setNome(usuarioAtual.getDisplayName());
        usuario.setEmail(usuarioAtual.getEmail());

        if (usuarioAtual.getPhotoUrl() == null)
            usuario.setFoto("");
        else
            usuario.setFoto(usuarioAtual.getPhotoUrl().toString());

        return usuario;
    }

    public static String getIdentificadorUsuario() {
        return getUsuarioAtual().getUid();
    }
}
