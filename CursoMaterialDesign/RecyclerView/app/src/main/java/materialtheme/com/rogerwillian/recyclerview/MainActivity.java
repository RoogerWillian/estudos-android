package materialtheme.com.rogerwillian.recyclerview;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.Callback;
import com.pkmmte.pkrss.PkRSS;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements Callback {

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<Article> articles = new ArrayList<>();
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerAdapter(articles);
        recyclerView.setAdapter(adapter);

        PkRSS.with(this).load("http://www.androidpro.com.br/feed/").skipCache().callback(this).async();

        alertDialog = new SpotsDialog.Builder().setContext(this).setMessage("Buscando posts...").setCancelable(false).build();
    }

    @Override
    public void onPreload() {
        alertDialog.show();
    }

    @Override
    public void onLoaded(List<Article> newArticles) {
        articles.clear();
        articles.addAll(newArticles);
        alertDialog.dismiss();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFailed() {

    }
}
