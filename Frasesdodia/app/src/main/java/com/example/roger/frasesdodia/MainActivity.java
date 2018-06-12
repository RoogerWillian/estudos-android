package com.example.roger.frasesdodia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sortearFrase(View view) {

        String[] frases = {
                "Que o vento leve o necessário e me traga o suficiente.",
                "Algumas vezes coisas ruins acontecem em nossas vidas para nos colocar na direção das melhores coisas que poderíamos viver.",
                "Nunca saberemos o quão forte somos até que ser forte seja a única escolha.",
                "Ser feliz não é viver apenas momentos de alegria. É ter coragem de enfrentar os momentos de tristeza e sabedoria para transformar os problemas em aprendizado.",
                "Se a caminhada está difícil, é porque você está no caminho certo.",
                "Ame seus pais, sua vida e seus amigos. Seus pais, porque são únicos. Sua vida, porque é curta demais. Seus amigos, porque são raros.",
                "Chique é ser feliz. Elegante é ser honesto. Bonito é ser caridoso. Sábio é saber ser grato. O resto é inversão de valores.",
                "Seja humilde para admitir seus erros, inteligente para aprender com eles e maduro para corrigi-los.",
                "Prefira o sorriso, faz bem a você e aos que estão ao seu redor. Dê risada de tudo, de si mesmo. Não adie alegrias. Seja feliz hoje!",
                "O machado era de Assis. A rosa, do Guimarães. A bandeira, do Manuel. Mas feliz mesmo era o Jorge, que era amado.",
                "Quando algo ruim acontece você tem três escolhas: deixar isso definir você, deixar isso destruir você ou fazer isso te deixar mais forte."
        };

        int numero = new Random().nextInt(frases.length);
        TextView textoFrase = this.findViewById(R.id.txt_frase);
        textoFrase.setText(frases[numero]);
    }
}
