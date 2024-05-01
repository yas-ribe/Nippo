package br.fecap.nippoprojeto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Encontrar o botão Cadastre-se
        Button buttonCadastro = findViewById(R.id.buttonCadastro);

        // Definir um OnClickListener para o botão Cadastre-se
        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir a tela de cadastro
                abrirCadastro();
            }
        });
    }

    // Método para abrir a tela de cadastro
    private void abrirCadastro() {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }
}
