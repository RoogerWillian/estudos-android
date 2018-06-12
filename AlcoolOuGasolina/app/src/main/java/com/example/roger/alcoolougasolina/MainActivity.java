package com.example.roger.alcoolougasolina;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText etAlcool;
    private EditText etGasolina;
    private TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.etAlcool = findViewById(R.id.etAlcool);
        this.etGasolina = findViewById(R.id.etGasolina);
        this.tvResultado = findViewById(R.id.tvResultado);
    }

    public void calcularPreco(View view) {

        String precoAlcool = this.etAlcool.getText().toString();
        String precoGasolina = this.etGasolina.getText().toString();

        Boolean camposValidados = this.validarCampos(precoAlcool, precoGasolina);
        if (camposValidados) {
            this.calcularMelhorPreco(precoAlcool, precoGasolina);
        } else {
            tvResultado.setText("Preencha os preços primeiro!");
        }
    }

    public void calcularMelhorPreco(String pAlcool, String pGasolina) {

        //Convertendo valores;
        Double precoAlcool = Double.parseDouble(pAlcool);
        Double precoGasosa = Double.parseDouble(pGasolina);

        Double resultado = precoAlcool / precoGasosa;
        if (resultado >= 0.7) {
            tvResultado.setText("Melhor utilizar Gasosa!");
        } else {
            tvResultado.setText("Melhor utilizar Álcool!");
        }
    }

    public Boolean validarCampos(String pAlcool, String pGasolina) {
        Boolean camposValidados = true;

        if (pAlcool == null || pAlcool.equals("")) {
            camposValidados = false;
        } else if (pGasolina == null || pGasolina.equals("")) {
            camposValidados = false;
        }

        return camposValidados;
    }
}
