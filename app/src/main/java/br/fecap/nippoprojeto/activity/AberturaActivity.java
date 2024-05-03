package br.fecap.nippoprojeto.activity;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import br.fecap.nippoprojeto.R;

public class AberturaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abertura);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AberturaActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000); 
    }
}

