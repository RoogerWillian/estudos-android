package com.example.roger.componentesbasicos;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText campoProduto;
    private TextView resultado;
    private CheckBox cbBranco, cbVerde, getCbVermelho;
    private List<String> check = new ArrayList<>();
    private RadioGroup rgEstoque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.campoProduto = findViewById(R.id.etNomeProduto);
        this.resultado = findViewById(R.id.tvResultado);
        this.cbBranco = findViewById(R.id.cbBranco);
        this.cbVerde = findViewById(R.id.cbVerde);
        this.getCbVermelho = findViewById(R.id.cbVermelho);
        this.rgEstoque = findViewById(R.id.rgEstoque);

        verificarRadio();
    }

    public void verificaCheck() {

        check.clear();

        if (this.cbBranco.isChecked()) {
            check.add(cbBranco.getText().toString());
        }

        if (this.cbVerde.isChecked()) {
            check.add(cbVerde.getText().toString());
        }

        if (this.getCbVermelho.isChecked()) {
            check.add(getCbVermelho.getText().toString());
        }

        resultado.setText(check.toString());
    }

    public void verificarRadio() {

        rgEstoque.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int idRadioSelecionado) {
                if (idRadioSelecionado == R.id.rbSim) {
                    resultado.setText("Sim");
                } else {
                    resultado.setText("Não");
                }
            }
        });

    }

    public void btEnviar(View view) {
        /*String nomeProduto = String.valueOf(this.campoProduto.getText());
        this.resultado.setText(nomeProduto);*/

        //verificaCheck();
    }
}
