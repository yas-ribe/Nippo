package br.fecap.nippoprojeto.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import br.fecap.nippoprojeto.R;
import br.fecap.nippoprojeto.service.ConexaoServidor;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private TextInputEditText senhaEditText;
    private Button buttonLogin;
    private Button buttonCadastro;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando...");
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.inputEmailLogin);
        senhaEditText = findViewById(R.id.inputSenhaLogin);
        buttonLogin = findViewById(R.id.btnEntrar);
        buttonCadastro = findViewById(R.id.buttonCadastro);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarLogin();
            }
        });

        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirSignUp();
            }
        });
    }

    private void realizarLogin() {
        String email = emailEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();

        if (!validarCampos(email, senha)) {
            return;
        }

        progressDialog.show();

        ConexaoServidor.realizarLogin(this, email, senha, new ConexaoServidor.Callback() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                abrirPerfil();
            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                if (message.equals("Conta não encontrada") || message.equals("Senha incorreta")) {
                    Toast.makeText(LoginActivity.this, "Endereço de e-mail ou senha incorretos. Por favor, tente novamente.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Erro ao fazer login. Por favor, tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean validarCampos(String email, String senha) {
        boolean isValid = true;

        if (email.isEmpty()) {
            emailEditText.setError("Por favor, insira um endereço de e-mail.");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Por favor, insira um endereço de e-mail válido.");
            isValid = false;
        } else {
            emailEditText.setError(null);
        }

        if (senha.isEmpty()) {
            senhaEditText.setError("Por favor, insira uma senha.");
            isValid = false;
        } else if (senha.length() < 6 || senha.length() > 20) {
            senhaEditText.setError("A senha deve ter entre 6 e 20 caracteres.");
            isValid = false;
        } else {
            senhaEditText.setError(null);
        }

        return isValid;
    }


    private void abrirSignUp() {
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }

    private void abrirPerfil() {
        Intent intent = new Intent(LoginActivity.this, PerfilActivity.class);
        startActivity(intent);
        finish();
    }
}
