package br.fecap.nippoprojeto.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;
import br.fecap.nippoprojeto.R;

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextSenha;
    private EditText editTextTelefone;
    private EditText editTextCpf;
    private Button buttonCadastrar;
    private Button buttonLogin;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        editTextNome = findViewById(R.id.inputNomeCadastro);
        editTextEmail = findViewById(R.id.inputEmailCadastro);
        editTextSenha = findViewById(R.id.inputSenhaCadastro);
        editTextTelefone = findViewById(R.id.inputTelefoneCadastro);
        editTextCpf = findViewById(R.id.inputCpfCadastro);
        buttonCadastrar = findViewById(R.id.btnCadastro);
        buttonLogin = findViewById(R.id.btnLogin);
        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarUsuario();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void cadastrarUsuario() {
        String nome = editTextNome.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String senha = editTextSenha.getText().toString().trim();
        String telefone = editTextTelefone.getText().toString().trim();
        String cpf = editTextCpf.getText().toString().trim();

        if (!validarCampo(editTextNome, "Nome") ||
                !validarCampo(editTextEmail, "E-mail") ||
                !validarCampo(editTextSenha, "Senha") ||
                !validarCampo(editTextTelefone, "Telefone") ||
                !validarCampo(editTextCpf, "CPF") ||
                !validarFormatoEmail(editTextEmail) ||
                !validarCPF(editTextCpf) ||
                !validarTelefone(editTextTelefone) ||
                !validarSenha(editTextSenha)) {
            return;
        }


        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        String url = "https://ljtt7h-3000.csb.app/cadastro";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("Cadastro", "Resposta do servidor: " + response);
                        Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CadastroActivity.this, LoginActivity.class));
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("Cadastro", "Erro ao fazer solicitação: " + error.toString());
                Toast.makeText(CadastroActivity.this, "Erro ao cadastrar usuário. Por favor, tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nome", nome);
                params.put("email", email);
                params.put("senha", senha);
                params.put("telefone", telefone);
                params.put("cpf", cpf);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private boolean validarTelefone(EditText campoTelefone) {
        String telefone = campoTelefone.getText().toString().replaceAll("[^0-9]", "");
        if (telefone.length() < 10 || telefone.length() > 11) {
            campoTelefone.setError("Telefone inválido");
            return false;
        }
        return true;
    }

    private boolean validarCampo(EditText campo, String nomeCampo) {
        String valor = campo.getText().toString();
        if (valor.isEmpty()) {
            campo.setError(nomeCampo + " é obrigatório");
            return false;
        }
        return true;
    }

    private boolean validarFormatoEmail(EditText campoEmail) {
        String email = campoEmail.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            campoEmail.setError("Formato de email inválido");
            return false;
        }
        return true;
    }

    private boolean validarCPF(EditText campoCPF) {
        String cpf = campoCPF.getText().toString().replaceAll("[^0-9]", "");
        if (cpf.length() != 11) {
            campoCPF.setError("CPF inválido");
            return false;
        }

        // Calcula o primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int digito1 = 11 - (soma % 11);
        if (digito1 > 9) {
            digito1 = 0;
        }

        // Calcula o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        int digito2 = 11 - (soma % 11);
        if (digito2 > 9) {
            digito2 = 0;
        }

        // Verifica se os dígitos calculados conferem com os dígitos informados
        if ((cpf.charAt(9) - '0' == digito1) && (cpf.charAt(10) - '0' == digito2)) {
            return true;
        } else {
            campoCPF.setError("CPF inválido");
            return false;
        }
    }

    private boolean validarSenha(EditText campoSenha) {
        String senha = campoSenha.getText().toString();
        if (senha.length() < 8) {
            campoSenha.setError("A senha deve ter pelo menos 8 caracteres");
            return false;
        }
        if (!senha.matches(".*[A-Z].*") || !senha.matches(".*[a-z].*") || !senha.matches(".*\\d.*") || !senha.matches(".*[@#$%^&+=].*")) {
            campoSenha.setError("A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial");
            return false;
        }
        return true;
    }
}

