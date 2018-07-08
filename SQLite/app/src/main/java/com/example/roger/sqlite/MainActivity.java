package com.example.roger.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // Criar banco de dados
            SQLiteDatabase bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);

            // Criar tabela
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS pessoas( nome VARCHAR, idade INT(3))");

            // Inserir dados
//             bancoDados.execSQL("INSERT INTO pessoas(nome, idade) VALUES ('Derik Gabriel', 15)");
//             bancoDados.execSQL("INSERT INTO pessoas(nome, idade) VALUES ('Natalia Pereira', 24)");

            // Recuperar pessoas
            String consulta =
                    "SELECT nome, idade " +
                    "FROM pessoas WHERE nome = 'Roger Willian'";
            Cursor cursor = bancoDados.rawQuery(consulta, null);

            // Indices da tabela
            int indiceNome = cursor.getColumnIndex("nome");
            int indiceIdade = cursor.getColumnIndex("idade");

            cursor.moveToFirst();
            while (cursor != null) {
                String nome = cursor.getString(indiceNome);
                String idade = String.valueOf(cursor.getInt(indiceIdade));

                Log.i("RESULTADO - nome ", nome);
                Log.i("RESULTADO - idade ", idade);
                Log.i("RESULTADO", "---------------------------------------");
                cursor.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
