package com.example.roger.atmconsultoria.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.roger.atmconsultoria.R;

import mehdi.sakout.aboutpage.AboutPage;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sobre);

        View sobre = new AboutPage(this)
                .setImage(R.drawable.logo)
                .addGroup("Fale conosco")
                .addEmail("atmconsultoria@gmail.com", "Envie um e-mail")
                .addWebsite("https://www.google.com/", "Acesse nosso site")
                .addGroup("Acesse nossas redes sociais")
                .addFacebook("roogerwillian", "Facebook")
                .addTwitter("RogerNizoli", "Twitter")
                .addYoutube("UCB0JSO6d5ysH2Mmqz5I9rIw", "Youtube")
                .addPlayStore("com.google.android.apps.plus", "Play Store")
                .addGitHub("google", "Github")
                .addInstagram("google", "Instagram")
                .create();

        setContentView(sobre);
    }
}
