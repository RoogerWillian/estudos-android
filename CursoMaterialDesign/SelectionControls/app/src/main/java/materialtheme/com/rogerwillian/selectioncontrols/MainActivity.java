package materialtheme.com.rogerwillian.selectioncontrols;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SwitchCompat switchCompat;
    private AppCompatCheckBox checkBox;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchCompat = findViewById(R.id.swtichButton);
        checkBox = findViewById(R.id.checkboxButton);
        radioGroup = findViewById(R.id.radioGroup);

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Toast.makeText(MainActivity.this, "Ligado", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Desligado", Toast.LENGTH_SHORT).show();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Toast.makeText(MainActivity.this, "Selecionado", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Não selecionado", Toast.LENGTH_SHORT).show();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.option1:
                        Toast.makeText(MainActivity.this, "Opção 1", Toast.LENGTH_SHORT).show();
                    break;

                    case R.id.option2:
                        Toast.makeText(MainActivity.this, "Opção 2", Toast.LENGTH_SHORT).show();
                    break;

                    case R.id.option3:
                        Toast.makeText(MainActivity.this, "Opção 3", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        });
    }
}
