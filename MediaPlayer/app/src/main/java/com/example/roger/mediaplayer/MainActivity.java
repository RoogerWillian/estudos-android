package com.example.roger.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekVolume;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.musica);
        this.inicializarSeekbar();
    }

    private void inicializarSeekbar() {
        this.seekVolume = findViewById(R.id.seekVolume);

        // Configura o audio manager
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        int volumeMaximo = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volumeAtual = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        // Configurar os valores maximos para o Seekbar
        seekVolume.setMax(volumeMaximo);

        // Configurando o progresso atual com base no volume atual.
        seekVolume.setProgress(volumeAtual);

        seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, AudioManager.FLAG_SHOW_UI);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void executarMusica(View view) {

        if (mediaPlayer != null) {
            mediaPlayer.start();
            Toast.makeText(this, "Música iniciada...", Toast.LENGTH_SHORT).show();
        }

    }

    public void pausarMusica(View view) {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Toast.makeText(this, "Música pausada...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mediaPlayer != null) {
            mediaPlayer.start();
            Toast.makeText(this, "Música reiniciada...", Toast.LENGTH_SHORT).show();
        }
    }

    public void pararMusica(View view) {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            Toast.makeText(this, "Música parada...", Toast.LENGTH_SHORT).show();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.musica);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Toast.makeText(this, "Música pausada...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            // Liberar os recursos(Apaga a musica na memoria do celular)
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
