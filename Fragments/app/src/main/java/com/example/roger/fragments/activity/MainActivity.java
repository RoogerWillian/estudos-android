package com.example.roger.fragments.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.roger.fragments.R;
import com.example.roger.fragments.fragment.ContatosFragment;
import com.example.roger.fragments.fragment.ConversasFragment;

public class MainActivity extends AppCompatActivity {

    private ConversasFragment conversasFragment;
    private ContatosFragment contatosFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Remover sombra da actionBar
        getSupportActionBar().setElevation(0);

        conversasFragment = new ConversasFragment();
        this.renderizarFragment(conversasFragment);
    }

    public void mostrarFragmentConversas(View view) {
        conversasFragment = new ConversasFragment();
        this.renderizarFragment(conversasFragment);
    }

    public void mostrarFragmentContatos(View view) {
        contatosFragment = new ContatosFragment();
        this.renderizarFragment(contatosFragment);
    }

    /**
     * Reponsavel por renderizar um fragment
     *
     * @param fragment - fragment a ser renderizado
     */
    private void renderizarFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameConteudo, fragment);
        transaction.commit();
    }
}
