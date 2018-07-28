package whatsapp.com.rogerwillian.whatsapp.helper;

import java.text.SimpleDateFormat;

public class DataCustom {

    public static String dataAtual() {
        long data = System.currentTimeMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/MM/yyyy");
        return simpleDateFormat.format(data);
    }

    public static String mesAnoDataEscolhida(String data) {

        String[] dataQuebrada = data.split("/");

        return dataQuebrada[1] + dataQuebrada[2];
    }
}
