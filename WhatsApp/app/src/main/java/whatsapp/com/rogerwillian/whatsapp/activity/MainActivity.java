package whatsapp.com.rogerwillian.whatsapp.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import whatsapp.com.rogerwillian.whatsapp.R;
import whatsapp.com.rogerwillian.whatsapp.config.FirebaseConfig;
import whatsapp.com.rogerwillian.whatsapp.fragment.ContatosFragment;
import whatsapp.com.rogerwillian.whatsapp.fragment.ConversasFragment;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private MaterialSearchView searchView;
    private FragmentPagerItemAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseConfig.getFirebaseAuth();

        // Configurando toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("WhatstApp");
        setSupportActionBar(toolbar);

        // Configurando abas
        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add(R.string.aba_conversas, ConversasFragment.class)
                        .add(R.string.aba_contatos, ContatosFragment.class)
                        .create()
        );

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewPageTtab);
        viewPagerTab.setViewPager(viewPager);

        //Configurar SearchView
        searchView = findViewById(R.id.materialSearchPrincipal);
        searchView.setHint("Pesquise conversas");
        // Listenear para o searchview
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                ConversasFragment fragment = (ConversasFragment) adapter.getPage(0);
                fragment.recarregarConversas();
            }
        });

        // Listener para caixa de texto
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pesquisa) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pesquisa) {

                // Verifica se estamos na aba de contatos ou conversas
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        ConversasFragment conversasFragment = (ConversasFragment) adapter.getPage(0);
                        if (pesquisa != null && !pesquisa.isEmpty()) {
                            conversasFragment.pesquisarConversas(pesquisa.toLowerCase());
                        } else {
                            conversasFragment.recarregarConversas();
                        }
                        break;
                    case 1:
                        ContatosFragment contatosFragment = (ContatosFragment) adapter.getPage(1);
                        if (pesquisa != null && !pesquisa.isEmpty()) {
                            contatosFragment.pesquisarContatos(pesquisa.toLowerCase());
                        } else {
                            contatosFragment.recarregarContatos();
                        }
                        break;
                }

                return true;
            }
        });

        // Change viewpage
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = adapter.getPage(position);
                if (fragment.getClass().isAssignableFrom(ContatosFragment.class))
                    searchView.setHint("Pesquise contatos");
                else if (fragment.getClass().isAssignableFrom(ConversasFragment.class)) {
                    searchView.setHint("Pesquise conversas");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Configura o botao pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuSair:
                deslogarUsuario();
                finish();
                break;
            case R.id.menuConfiguracoes:
                abrirConfiguracoes();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void abrirConfiguracoes() {
        startActivity(new Intent(MainActivity.this, ConfiguracoesActivity.class));
    }

    public void deslogarUsuario() {
        try {
            auth.signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
