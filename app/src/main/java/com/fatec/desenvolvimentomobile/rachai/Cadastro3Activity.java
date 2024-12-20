package com.fatec.desenvolvimentomobile.rachai;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fatec.desenvolvimentomobile.rachai.Helpers.HttpHelper;
import com.fatec.desenvolvimentomobile.rachai.Model.Auth;

import org.json.JSONObject;

public class Cadastro3Activity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextSenha;
    private EditText editTextSenhaConfirmacao;
    private Button btnCadastrar;
    private Button btnVoltar;

    private final String CADASTRO_URL = "https://rachai-backend-github.onrender.com/auth/cadastro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro3);

        editTextEmail = findViewById(R.id.editTextEmail_cadastro3);
        editTextSenha = findViewById(R.id.editTextPassword_cadastro3);
        editTextSenhaConfirmacao = findViewById(R.id.editTextPassword2_cadastro3);
        btnCadastrar = findViewById(R.id.btn_login_login);
        btnVoltar = findViewById(R.id.btn_voltar_login);

        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(Cadastro3Activity.this, Cadastro2Activity.class);
            startActivity(intent);
            finish();
        });

        btnCadastrar.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String senha = editTextSenha.getText().toString().trim();
            String senhaConfirmacao = editTextSenhaConfirmacao.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty() || senhaConfirmacao.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!senha.equals(senhaConfirmacao)) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                return;
            }

            Auth auth = new Auth();
            auth.setEmail(email);
            auth.setSenha(senha);

            realizarCadastro(auth);
        });
    }

    private void realizarCadastro(Auth auth) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", auth.getEmail());
            jsonObject.put("senha", auth.getSenha());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Cadastro3Activity", "JSON gerado: " + jsonObject.toString());


        HttpHelper.enviarRequisicaoHttp(CADASTRO_URL, jsonObject, "POST", new HttpHelper.HttpResponseCallback() {
            @Override
            public void onSuccess(String response, int statusCode) {
                if (statusCode == 201) {
                    runOnUiThread(() -> {
                        Toast.makeText(Cadastro3Activity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Cadastro3Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(Cadastro3Activity.this, "Erro ao realizar cadastro: Código " + statusCode, Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(Cadastro3Activity.this, "Erro ao realizar cadastro: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}


