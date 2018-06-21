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
public class NumerosFragment extends Fragment implements View.OnClickListener {

    private ImageButton buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix;
    private MediaPlayer mediaPlayer;

    public NumerosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_numeros, container, false);

        this.buttonOne = view.findViewById(R.id.buttonOne);
        this.buttonTwo = view.findViewById(R.id.buttonTwo);
        this.buttonThree = view.findViewById(R.id.buttonThree);
        this.buttonFour = view.findViewById(R.id.buttonFour);
        this.buttonFive = view.findViewById(R.id.buttonFive);
        this.buttonSix = view.findViewById(R.id.buttonSix);

        this.buttonOne.setOnClickListener(this);
        this.buttonTwo.setOnClickListener(this);
        this.buttonThree.setOnClickListener(this);
        this.buttonFour.setOnClickListener(this);
        this.buttonFive.setOnClickListener(this);
        this.buttonSix.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonOne:
                this.tocarSom(R.raw.one);
                Toast.makeText(getActivity(), "This is number one", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonTwo:
                this.tocarSom(R.raw.two);
                Toast.makeText(getActivity(), "This is number two", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonThree:
                this.tocarSom(R.raw.three);
                Toast.makeText(getActivity(), "This is number three", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonFour:
                this.tocarSom(R.raw.four);
                Toast.makeText(getActivity(), "This is number four", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonFive:
                this.tocarSom(R.raw.five);
                Toast.makeText(getActivity(), "This is number five", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonSix:
                this.tocarSom(R.raw.six);
                Toast.makeText(getActivity(), "This is number six", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void tocarSom(int som) {
        mediaPlayer = MediaPlayer.create(getActivity(), som);
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
