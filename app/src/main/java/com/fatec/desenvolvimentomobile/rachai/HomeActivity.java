package com.fatec.desenvolvimentomobile.rachai;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fatec.desenvolvimentomobile.rachai.Adapters.MotoristaAdapter;
import com.fatec.desenvolvimentomobile.rachai.Model.Usuario;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    private ListView listViewMotoristas;
    private Button btnLogout;

    private final String MOTORISTAS_URL = "https://rachai-backend-github.onrender.com/usuarios/usuario/motoristas";
    private final String LOGOUT_URL = "https://rachai-backend-github.onrender.com/auth/logout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listViewMotoristas = new ListView(this);
        btnLogout = findViewById(R.id.btn_logout_home);

        LinearLayout layout = findViewById(R.id.listaMotoristas_home);
        layout.addView(listViewMotoristas);

        carregarMotoristas();

        btnLogout.setOnClickListener(v -> realizarLogout());
    }

    private void carregarMotoristas() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(MOTORISTAS_URL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(HomeActivity.this, "Erro ao carregar motoristas", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    try {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Usuario>>() {}.getType();
                        List<Usuario> motoristas = gson.fromJson(responseData, listType);

                        runOnUiThread(() -> {
                            MotoristaAdapter adapter = new MotoristaAdapter(HomeActivity.this, motoristas);
                            listViewMotoristas.setAdapter(adapter);
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() ->
                                Toast.makeText(HomeActivity.this, "Erro ao processar dados", Toast.LENGTH_SHORT).show()
                        );
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(HomeActivity.this, "Erro ao obter lista de motoristas", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    private void realizarLogout() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(LOGOUT_URL)
                .post(RequestBody.create(null, new byte[0]))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(HomeActivity.this, "Erro ao realizar logout", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(HomeActivity.this, "Logout bem-sucedido", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }, 5000);
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(HomeActivity.this, "Erro ao realizar logout", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}