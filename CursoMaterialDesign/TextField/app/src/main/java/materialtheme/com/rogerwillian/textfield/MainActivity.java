package materialtheme.com.rogerwillian.textfield;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private AppCompatEditText editTextEmail;
    private AppCompatEditText editTextPassword;

    private TextInputLayout textLayoutEmail;
    private TextInputLayout textLayoutPassword;

    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.editTextEmail = findViewById(R.id.et_email);
        this.editTextPassword = findViewById(R.id.et_password);
        this.textLayoutEmail = findViewById(R.id.txtLayoutEmail);
        this.textLayoutPassword = findViewById(R.id.txtLayoutPassword);
        this.btnLogin = findViewById(R.id.btn_login);
        this.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
            }
        });
    }

    private void validateForm() {
        if (editTextEmail.getText().toString().isEmpty()) {
            textLayoutEmail.setErrorEnabled(true);
            textLayoutEmail.setError("Preencha o seu e-mail");
        } else {
            textLayoutEmail.setErrorEnabled(false);
        }

        if (editTextPassword.getText().toString().isEmpty()) {
            textLayoutPassword.setErrorEnabled(true);
            textLayoutPassword.setError("Preencha a sua senha");
        } else {
            textLayoutPassword.setErrorEnabled(false);
        }
    }
}
