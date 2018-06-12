package com.example.roger.componentesbasicos2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private Switch switchEstado;
    private ToggleButton toggleEstado;
    private CheckBox checkBoxEstado;
    private TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.switchEstado = findViewById(R.id.switchEstado);
        this.toggleEstado = findViewById(R.id.toggleEstado);
        this.checkBoxEstado = findViewById(R.id.checkEstado);
        this.tvResultado = findViewById(R.id.tvResultado);
    }

    public void enviar(View view) {
        String resultado = "";

        if (switchEstado.isChecked()) {
            resultado += "Switch Ligado" + "\n";
        } else {
            resultado += "Switch Desligado" + "\n";
        }

        if (toggleEstado.isChecked()) {
            resultado += "Toggle Ligado" + "\n";
        } else {
            resultado += "Toggle Desligado" + "\n";
        }

        if (checkBoxEstado.isChecked()) {
            resultado += "Checkbox Ligado" + "\n";
        } else {
            resultado += "Checkbox Desligado" + "\n";
        }

        tvResultado.setText(resultado);

    }
}
