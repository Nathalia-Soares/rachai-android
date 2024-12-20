package com.fatec.desenvolvimentomobile.rachai;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fatec.desenvolvimentomobile.rachai.Model.Usuario;
import com.fatec.desenvolvimentomobile.rachai.Model.Veiculo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Cadastro2Activity extends AppCompatActivity {

    private EditText editTextModelo, editTextPlaca, editTextCor;
    private Button btnVoltar, btnContinuar, btnCarona, btnMotorista;
    private String tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro2);

        editTextModelo = findViewById(R.id.editTextModelo_cadastro2);
        editTextPlaca = findViewById(R.id.editTextPlaca_cadastro2);
        editTextCor = findViewById(R.id.editTextCor_cadastro2);
        btnVoltar = findViewById(R.id.btn_voltar_login);
        btnContinuar = findViewById(R.id.btn_login_login);
        btnCarona = findViewById(R.id.btn_carona_cadastro2);
        btnMotorista = findViewById(R.id.btn_motorista_cadastro2);

        Intent intent = getIntent();
        String nome = intent.getStringExtra("nome");
        String email = intent.getStringExtra("email");
        String ra = intent.getStringExtra("ra");
        String curso = intent.getStringExtra("curso");

        btnVoltar.setOnClickListener(v -> {
            Intent voltarIntent = new Intent(Cadastro2Activity.this, CadastroActivity.class);
            startActivity(voltarIntent);
            finish();
        });

        btnCarona.setOnClickListener(v -> {
            tipoUsuario = "PASSAGEIRO";
            Toast.makeText(Cadastro2Activity.this, "Você selecionou: Carona", Toast.LENGTH_SHORT).show();
        });

        btnMotorista.setOnClickListener(v -> {
            tipoUsuario = "MOTORISTA";
            Toast.makeText(Cadastro2Activity.this, "Você selecionou: Motorista", Toast.LENGTH_SHORT).show();
        });

        btnContinuar.setOnClickListener(v -> {
            String modelo = editTextModelo.getText().toString().trim();
            String placa = editTextPlaca.getText().toString().trim();
            String cor = editTextCor.getText().toString().trim();

            if (tipoUsuario == null) {
                Toast.makeText(Cadastro2Activity.this, "Selecione o tipo de usuário!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (modelo.isEmpty() || placa.isEmpty() || cor.isEmpty()) {
                Toast.makeText(Cadastro2Activity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            Veiculo veiculo = new Veiculo();
            veiculo.setModelo(modelo);
            veiculo.setPlaca(placa);
            veiculo.setCor(cor);

            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setRa(ra);
            usuario.setCurso(curso);
            usuario.setTipoUsuario(tipoUsuario);
            usuario.setVeiculo(veiculo);

            try {
                JSONObject usuarioJson = criarUsuarioJson(usuario);
                enviarDadosServidor(usuarioJson);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Cadastro2Activity.this, "Erro ao montar JSON!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private JSONObject criarUsuarioJson(Usuario usuario) throws JSONException {
        JSONObject usuarioJson = new JSONObject();
        usuarioJson.put("nome", usuario.getNome());
        usuarioJson.put("email", usuario.getEmail());
        usuarioJson.put("ra", usuario.getRa());
        usuarioJson.put("curso", usuario.getCurso());
        usuarioJson.put("tipo_usuario", usuario.getTipoUsuario());

        JSONObject veiculoJson = new JSONObject();
        veiculoJson.put("modelo", usuario.getVeiculo().getModelo());
        veiculoJson.put("placa", usuario.getVeiculo().getPlaca());
        veiculoJson.put("cor", usuario.getVeiculo().getCor());

        usuarioJson.put("veiculos", veiculoJson);

        Log.d("JSON Enviado", usuarioJson.toString());

        return usuarioJson;
    }

    private void enviarDadosServidor(JSONObject dados) {
        new Thread(() -> {
            try {
                URL url = new URL("https://rachai-backend-github.onrender.com/usuarios/criar_usuario");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = dados.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == 201 || responseCode == 200) {
                    runOnUiThread(() -> {
                        Toast.makeText(Cadastro2Activity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                        Intent continuarIntent = new Intent(Cadastro2Activity.this, Cadastro3Activity.class);
                        startActivity(continuarIntent);
                        finish();
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(Cadastro2Activity.this, "Erro ao cadastrar usuário!", Toast.LENGTH_SHORT).show()
                    );
                }

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(Cadastro2Activity.this, "Erro de conexão!", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}
