package whatsapp.com.rogerwillian.whatsapp.helper;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;

import whatsapp.com.rogerwillian.whatsapp.config.FirebaseConfig;
import whatsapp.com.rogerwillian.whatsapp.model.Usuario;

public class UsuarioFirebase {

    public static String getIdentificador() {

        FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
        String idUsuario = Base64Custom.codificar(firebaseAuth.getCurrentUser().getEmail());

        return idUsuario;
    }

    public static FirebaseUser getUsuarioAtual() {
        FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
        return firebaseAuth.getCurrentUser();
    }

    public static boolean atualizarFotoUsuario(Uri uri) {

        try {
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profileChange = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build();
            user.updateProfile(profileChange).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful())
                        Log.d("Perfil", "Erro ao atualizar foto do usuário: " + task.getException().getMessage());
                }
            });

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean atualizarNomeUsuario(String nome) {

        try {
            // Atualizando no firebase
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profileChange = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();
            user.updateProfile(profileChange).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful())
                        Log.d("Perfil", "Erro ao atualizar nome de perfil: " + task.getException().getMessage());
                }
            });

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Usuario getDadosUsuarioLogado() {
        FirebaseUser firebaseUser = getUsuarioAtual();
        Usuario usuario = new Usuario();
        usuario.setEmail(firebaseUser.getEmail());
        usuario.setNome(firebaseUser.getDisplayName());

        if (firebaseUser.getPhotoUrl() == null)
            usuario.setFoto("");
        else
            usuario.setFoto(firebaseUser.getPhotoUrl().toString());

        return usuario;
    }
}
