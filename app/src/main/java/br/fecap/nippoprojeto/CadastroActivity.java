package br.fecap.nippoprojeto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextSenha;
    private EditText editTextTelefone;
    private EditText editTextCpf;
    private Button buttonCadastrar;
    private Button buttonLogin;

    private RequestQueue requestQueue;
    private ProgressDialog progressDialog; // Adicione esta linha

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicialize as views
        editTextNome = findViewById(R.id.inputNomeCadastro);
        editTextEmail = findViewById(R.id.inputEmailCadastro);
        editTextSenha = findViewById(R.id.inputSenhaCadastro);
        editTextTelefone = findViewById(R.id.inputTelefoneCadastro);
        editTextCpf = findViewById(R.id.inputCpfCadastro);
        buttonCadastrar = findViewById(R.id.btnCadastro);
        buttonLogin = findViewById(R.id.btnLogin);

        // Inicialize a fila de solicitações Volley
        requestQueue = Volley.newRequestQueue(this);

        // Inicialize o ProgressDialog
        progressDialog = new ProgressDialog(this); // Adicione esta linha

        // Defina o evento de clique do botão de cadastro
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarUsuario();
            }
        });

        // Defina o evento de clique do botão de login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void cadastrarUsuario() {
        // Mostre o ProgressDialog
        progressDialog.setMessage("Carregando..."); // Adicione esta linha
        progressDialog.show(); // Adicione esta linha

        // Obtenha os dados do usuário dos campos de entrada
        String nome = editTextNome.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String senha = editTextSenha.getText().toString().trim();
        String telefone = editTextTelefone.getText().toString().trim();
        String cpf = editTextCpf.getText().toString().trim();

        // URL do servidor Node.js
        String url = "https://ljtt7h-3001.csb.app/cadastro";

        // Crie uma solicitação POST usando Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Esconda o ProgressDialog
                        progressDialog.dismiss(); // Adicione esta linha

                        Log.d("Cadastro", "Resposta do servidor: " + response);
                        Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                        // Redirecione para LoginActivity após o cadastro bem-sucedido
                        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Encerra a CadastroActivity
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Esconda o ProgressDialog
                progressDialog.dismiss(); // Adicione esta linha

                Log.e("Cadastro", "Erro ao fazer solicitação: " + error.toString());
                Toast.makeText(CadastroActivity.this, "Erro ao cadastrar usuário. Por favor, tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
                // Lidar com o erro aqui, se necessário
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

        // Adicione a solicitação à fila de solicitações Volley
        requestQueue.add(stringRequest);
    }
}
