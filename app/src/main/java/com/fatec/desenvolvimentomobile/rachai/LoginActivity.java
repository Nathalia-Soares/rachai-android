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

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button btnLogin;
    private Button btnVoltar;

    private final String LOGIN_URL = "https://rachai-backend-github.onrender.com/auth/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail_login);
        editTextSenha = findViewById(R.id.editTextSenha_login);
        btnLogin = findViewById(R.id.btn_login_login);
        btnVoltar = findViewById(R.id.btn_voltar_login);

        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String senha = editTextSenha.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Auth auth = new Auth();
            auth.setEmail(email);
            auth.setSenha(senha);

            realizarLogin(auth);
        });
    }

    private void realizarLogin(Auth auth) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", auth.getEmail());
            jsonObject.put("senha", auth.getSenha());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("LoginActivity", "JSON gerado: " + jsonObject.toString());

        HttpHelper.enviarRequisicaoHttp(LOGIN_URL, jsonObject, "POST", new HttpHelper.HttpResponseCallback() {
            @Override
            public void onSuccess(String response, int statusCode) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String token = jsonResponse.getString("token");

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("auth_token", token);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, "Erro ao processar a resposta do servidor", Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(LoginActivity.this, "Erro no login: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}
