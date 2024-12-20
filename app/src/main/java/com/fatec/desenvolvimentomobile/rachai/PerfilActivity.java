package com.fatec.desenvolvimentomobile.rachai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fatec.desenvolvimentomobile.rachai.Model.Usuario;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PerfilActivity extends AppCompatActivity {

    private TextView textViewNomeUsuario, textViewEmailUsuario, textViewRaUsuario, textViewCursoUsuario, textViewTipoUsuario;
    private ImageView imageViewPerfil;
    private Button btnVoltar;

    private static final String BASE_URL = "https://rachai-backend-github.onrender.com/usuarios/usuario/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        textViewNomeUsuario = findViewById(R.id.textViewNomeUsuario_perfil);
        textViewEmailUsuario = findViewById(R.id.textViewEmailUsuario_perfil);
        textViewRaUsuario = findViewById(R.id.textViewRaUsuario_perfil);
        textViewCursoUsuario = findViewById(R.id.textViewCursoUsuario_perfil);
        textViewTipoUsuario = findViewById(R.id.textViewTipo_Usuario_perfil);
        imageViewPerfil = findViewById(R.id.imageView_perfil);
        btnVoltar = findViewById(R.id.btn_voltar_login);

        String userId = getIntent().getStringExtra("USER_ID");
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Erro ao obter ID do usuário", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        carregarDadosUsuario(userId);

        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void carregarDadosUsuario(String userId) {
        OkHttpClient client = new OkHttpClient();

        String url = BASE_URL + userId;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(PerfilActivity.this, "Erro ao carregar dados do usuário", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Gson gson = new Gson();

                    try {
                        Usuario usuario = gson.fromJson(responseData, Usuario.class);

                        runOnUiThread(() -> {
                            textViewNomeUsuario.setText(usuario.getNome());
                            textViewEmailUsuario.setText(usuario.getEmail());
                            textViewRaUsuario.setText(usuario.getRa());
                            textViewCursoUsuario.setText(usuario.getCurso());
                            textViewTipoUsuario.setText(usuario.getTipoUsuario());

                            Glide.with(PerfilActivity.this)
                                    .load(usuario.getImgUrl()) // Campo atualizado para carregar img_url
                                    .placeholder(R.drawable.profile) // Imagem padrão
                                    .error(R.drawable.profile) // Imagem para caso de erro
                                    .into(imageViewPerfil);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() ->
                                Toast.makeText(PerfilActivity.this, "Erro ao processar os dados do usuário", Toast.LENGTH_SHORT).show()
                        );
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(PerfilActivity.this, "Erro ao obter dados do usuário", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}
