package com.example.roger.alertdialog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void abrirAlerta(View view) {

        /*
         * Criar dialog
         */

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        //Configurando titulo e mensagem
        dialog.setTitle("Título");
        dialog.setMessage("Mensagem");

        //Configurando cancelamento(Se pode fechar ou nao)
        dialog.setCancelable(false);

        //Configurando icone
        dialog.setIcon(android.R.drawable.ic_btn_speak_now);

        //Configurando acoes sim ou nao
        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Sim foi pressionado", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Não foi pressionado", Toast.LENGTH_SHORT).show();
            }
        });

        // Criar e exibir AlertDialog
        dialog.create();
        dialog.show();
    }
}
