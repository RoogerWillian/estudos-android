package materialtheme.com.rogerwillian.toolbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_menu:
                Toast.makeText(this, "buscar", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cart_menu:
                Toast.makeText(this, "carrinho", Toast.LENGTH_SHORT).show();
                break;
            case R.id.config_menu:
                Toast.makeText(this, "configuracoes", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about_menu:
                Toast.makeText(this, "sobre", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
