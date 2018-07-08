package com.example.roger.listatarefas.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NOME_DB = "DB_TAREFAS";
    public static final String TABELA_TAREFAS = "tarefas";

    public DbHelper(Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            String sql = "CREATE TABLE IF NOT EXISTS " + TABELA_TAREFAS
                    + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " nome TEXT NOT NULL ); ";

            db.execSQL(sql);
            Log.i("INFO_DB", "Sucesso ao criar a tabela!");
        } catch (Exception e) {
            Log.i("INFO_DB", "Erro ao criar a tabela " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABELA_TAREFAS + ";";

        try {
            db.execSQL(sql);
            onCreate(db);
            Log.i("INFO_DB", "Sucesso ao atualizar o app!");
        } catch (Exception e) {
            Log.i("INFO_DB", "Erro ao atualizar o app " + e.getMessage());
        }
    }
}
