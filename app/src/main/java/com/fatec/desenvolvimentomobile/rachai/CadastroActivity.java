package com.fatec.desenvolvimentomobile.rachai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextNome, editTextEmail, editTextRa;
    private Button btnVoltar, btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Spinner spinnerCurso = findViewById(R.id.spinner_cursos_cadastro);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cursos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurso.setAdapter(adapter);

        editTextNome = findViewById(R.id.editTextNome_cadastro);
        editTextEmail = findViewById(R.id.editTextTextEmail_Cadastro);
        editTextRa = findViewById(R.id.editTextRa_Cadastro);
        spinnerCurso = findViewById(R.id.spinner_cursos_cadastro);
        btnVoltar = findViewById(R.id.btn_voltar_login);
        btnContinuar = findViewById(R.id.btn_login_login);

        btnVoltar.setOnClickListener(view -> {
            Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        Spinner finalSpinnerCurso = spinnerCurso;
        btnContinuar.setOnClickListener(view -> {
            String nome = editTextNome.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String ra = editTextRa.getText().toString().trim();
            String curso = finalSpinnerCurso.getSelectedItem().toString();

            if (nome.isEmpty() || email.isEmpty() || ra.isEmpty() || curso.isEmpty()) {
                Toast.makeText(CadastroActivity.this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(CadastroActivity.this, Cadastro2Activity.class);
            intent.putExtra("nome", nome);
            intent.putExtra("email", email);
            intent.putExtra("ra", ra);
            intent.putExtra("curso", curso);
            startActivity(intent);
        });
    }
}


