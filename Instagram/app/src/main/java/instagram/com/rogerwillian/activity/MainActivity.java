package instagram.com.rogerwillian.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import instagram.com.rogerwillian.R;
import instagram.com.rogerwillian.fragment.FeedFragment;
import instagram.com.rogerwillian.fragment.PerfilFragment;
import instagram.com.rogerwillian.fragment.PesquisaFragment;
import instagram.com.rogerwillian.fragment.PostagemFragment;
import instagram.com.rogerwillian.helper.FirebaseConfig;
import instagram.com.rogerwillian.helper.FragmentHelper;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Configura Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Instagram");
        setSupportActionBar(toolbar);

        // Configuracoes Iniciais
        autenticacao = FirebaseConfig.getFirebaseAuth();
        fragmentManager = getSupportFragmentManager();

        // Configurar bottom navegation view
        configuraBottomNavegationView();

        // Carregando fragmente feed por padrão
        FragmentHelper.showFragment(fragmentManager, new FeedFragment(), R.id.viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_sair:
                deslogarUsuario();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void configuraBottomNavegationView() {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavegation);

        // Configuracoes iniciais
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);

        // Habilitar navegacao
        this.habiitarNavegacao(bottomNavigationViewEx);

        // Configura aba selecionada inicialmente
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

    }

    /**
     * Método responsável por tratar eventos de click na BottomNavigationView
     *
     * @param viewEx
     */
    private void habiitarNavegacao(BottomNavigationViewEx viewEx) {
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.ic_home:
                        FragmentHelper.showFragment(fragmentManager, new FeedFragment(), R.id.viewPager);
                        return true;
                    case R.id.ic_pesquisa:
                        FragmentHelper.showFragment(fragmentManager, new PesquisaFragment(), R.id.viewPager);
                        return true;
                    case R.id.ic_postagem:
                        FragmentHelper.showFragment(fragmentManager, new PostagemFragment(), R.id.viewPager);
                        return true;
                    case R.id.ic_perfil:
                        FragmentHelper.showFragment(fragmentManager, new PerfilFragment(), R.id.viewPager);
                        return true;
                }

                return false;
            }
        });
    }

    private void deslogarUsuario() {
        try {
            autenticacao.signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
