package materialtheme.com.rogerwillian.dialogs;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton btnAlert;
    private AppCompatButton btnAlertItems;
    private AlertDialog alertDialog;
    private AlertDialog alertDialogItems;

    private String[] items = {"Item 1", "Item 2", "Item 3", "Item 4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAlert = findViewById(R.id.btnAlert);
        btnAlertItems = findViewById(R.id.btnAlertItems);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja excluir?");
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "SIM", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "NÃO", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog = builder.create();
        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });


        // WITH ITEMS
        AlertDialog.Builder builderDialogItems = new AlertDialog.Builder(this);
        builderDialogItems.setTitle("Selecione a opção desejada");
        builderDialogItems.setPositiveButton("OK", null);
        builderDialogItems.setNegativeButton("CANCELAR", null);
//        builderDialogItems.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(MainActivity.this, items[i], Toast.LENGTH_SHORT).show();
//            }
//        });
        builderDialogItems.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                Toast.makeText(MainActivity.this, items[0] + b, Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogItems = builderDialogItems.create();
        btnAlertItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogItems.show();
            }
        });
    }
}
