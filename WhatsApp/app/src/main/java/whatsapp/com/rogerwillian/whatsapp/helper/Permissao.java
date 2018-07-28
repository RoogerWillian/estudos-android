package whatsapp.com.rogerwillian.whatsapp.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validarPermissoes(Activity activity, int resquestCode, String[] permissoes) {

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> listaPermissoes = new ArrayList<>();

            /* Percorres as permissoes necessarias verificando uma a uma se já foi liberada */
            for (String permissao : permissoes) {
                Boolean temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!temPermissao) listaPermissoes.add(permissao);
            }

            // Caso a lista esteja vazia, não é necessário solicitar as permissões
            if (listaPermissoes.isEmpty()) return true;
            String[] permissoesParaSolicitar = new String[listaPermissoes.size()];
            listaPermissoes.toArray(permissoesParaSolicitar);

            // Solicita permissao
            ActivityCompat.requestPermissions(activity, permissoesParaSolicitar, resquestCode);
        }


        return true;
    }

}
