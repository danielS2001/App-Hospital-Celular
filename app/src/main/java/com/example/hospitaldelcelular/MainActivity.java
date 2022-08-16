package com.example.hospitaldelcelular;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnAgenda, btnCitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciar();

        btnAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AgendarActivity.class);
                startActivity(intent);
            }
        });

        btnCitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CitasActivity.class);
                startActivityForResult(i, 0);
            }
        });
    }
    private void iniciar() {
        btnAgenda = (Button) findViewById(R.id.btnAgenda);
        btnCitas = (Button) findViewById(R.id.btnCitas);
    }
}