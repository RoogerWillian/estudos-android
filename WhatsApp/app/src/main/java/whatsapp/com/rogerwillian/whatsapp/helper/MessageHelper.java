package whatsapp.com.rogerwillian.whatsapp.helper;

import android.support.design.widget.Snackbar;
import android.view.View;

public class MessageHelper {

    public static void exibirSnackbar(View view, String mensagem, int duracao) {
        Snackbar.make(view, mensagem, duracao).show();
    }
}
