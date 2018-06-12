package com.example.roger.pedrapapeloutesoura;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void selecionarPedra(View view) {
        this.opcaoSelecionada("pedra");
    }

    public void selecionarPapel(View view) {
        this.opcaoSelecionada("papel");
    }

    public void selecionarTesoura(View view) {
        this.opcaoSelecionada("tesoura");
    }

    public void opcaoSelecionada(String escolhaUsuario) {
        ImageView imagemResultado = this.findViewById(R.id.img_resultado);
        TextView textoResultado = this.findViewById(R.id.txt_resultado);
        textoResultado.setText("Verificando resultado...");
        int numero = new Random().nextInt(3);
        String[] opcoes = {"pedra", "papel", "tesoura"};
        String escolhaApp = opcoes[numero];

        switch (escolhaApp) {
            case "pedra":
                imagemResultado.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                imagemResultado.setImageResource(R.drawable.papel);
                break;
            case "tesoura":
                imagemResultado.setImageResource(R.drawable.tesoura);
                break;
        }

        if (
                (escolhaApp == "pedra" && escolhaUsuario == "tesoura") ||
                        (escolhaApp == "papel" && escolhaUsuario == "pedra") ||
                        (escolhaApp == "tesoura" && escolhaUsuario == "papel")
                ) { // App ganhador
            textoResultado.setText("SE FUDEU kkk");
            Toast.makeText(this, "app ganhou :(", Toast.LENGTH_SHORT).show();
        } else if (
                (escolhaUsuario == "pedra" && escolhaApp == "tesoura") ||
                        (escolhaUsuario == "papel" && escolhaApp == "pedra") ||
                        (escolhaUsuario == "tesoura" && escolhaApp == "papel")
                ) { // Usuario ganhador
            textoResultado.setText("Você ganhou :)");
            Toast.makeText(this, "Ai sim ;)", Toast.LENGTH_SHORT).show();
        } else { // empate
            textoResultado.setText("Empatamos");
        }
    }
}
