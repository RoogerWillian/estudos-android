package com.example.roger.calculadoragorjeta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText etValor;
    private TextView tvPorcentagem, tvGorjeta, tvTotal;
    private SeekBar seekGorjeta;

    private Double porcentagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.etValor = findViewById(R.id.etValor);
        this.tvPorcentagem = findViewById(R.id.tvPorcentagem);
        this.tvGorjeta = findViewById(R.id.tvGorjeta);
        this.tvTotal = findViewById(R.id.tvTotal);
        this.seekGorjeta = findViewById(R.id.seekGorjeta);

        // Calculando gorjeta;
        configurarSeek();
    }

    private void configurarSeek() {

        seekGorjeta.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                porcentagem = Double.valueOf(seekBar.getProgress());
                tvPorcentagem.setText(Math.round(porcentagem) + "%");
                calcular();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void calcular() {
        // recuperando valor
        double valorDigitado = Double.valueOf(this.etValor.getText().toString());

        // calculando a gorjeta
        double gorjeta = valorDigitado * (porcentagem / 100);
        double total = gorjeta + valorDigitado;

        tvGorjeta.setText("R$ " + Math.round(gorjeta));
        tvTotal.setText("R$ " + total);
    }
}
