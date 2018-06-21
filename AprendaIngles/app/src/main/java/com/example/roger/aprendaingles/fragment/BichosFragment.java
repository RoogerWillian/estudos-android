package com.example.roger.aprendaingles.fragment;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.roger.aprendaingles.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BichosFragment extends Fragment implements View.OnClickListener {

    private ImageButton buttonCao, buttonGato, buttonLeao, buttonMacaco, buttonOvelha, buttonVaca;
    private MediaPlayer mediaPlayer;

    public BichosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bichos, container, false);

        this.buttonCao = view.findViewById(R.id.buttonCao);
        this.buttonGato = view.findViewById(R.id.buttonGato);
        this.buttonLeao = view.findViewById(R.id.buttonLeao);
        this.buttonMacaco = view.findViewById(R.id.buttonMacaco);
        this.buttonOvelha = view.findViewById(R.id.buttonOvelha);
        this.buttonVaca = view.findViewById(R.id.buttonVaca);

        this.buttonCao.setOnClickListener(this);
        this.buttonGato.setOnClickListener(this);
        this.buttonLeao.setOnClickListener(this);
        this.buttonMacaco.setOnClickListener(this);
        this.buttonOvelha.setOnClickListener(this);
        this.buttonVaca.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonCao:
                this.tocarSom(R.raw.dog);
                Toast.makeText(getActivity(), "This is a dog!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonGato:
                this.tocarSom(R.raw.cat);
                Toast.makeText(getActivity(), "This is a cat!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonLeao:
                this.tocarSom(R.raw.lion);
                Toast.makeText(getActivity(), "This is a lion", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonMacaco:
                this.tocarSom(R.raw.monkey);
                Toast.makeText(getActivity(), "This is a monkey!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonOvelha:
                this.tocarSom(R.raw.sheep);
                Toast.makeText(getActivity(), "This is a sheep!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonVaca:
                this.tocarSom(R.raw.cow);
                Toast.makeText(getActivity(), "This is a cow!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void tocarSom(int som) {
        this.mediaPlayer = MediaPlayer.create(getActivity(), som);

        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                }
            });
        }
    }
}
