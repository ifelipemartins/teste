package br.edu.fecap.meulogin;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button buttonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar as views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);




        // Configurar o clique do botão de login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obter os valores dos campos de email e senha
                String email = editTextEmail.getText().toString().trim();
                String senha = editTextSenha.getText().toString().trim();

                // Verificar se os campos não estão vazios
                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Realizar a solicitação de login
                realizarLogin(email, senha);
            }
        });
    }

    private void realizarLogin(String email, String senha) {
        String url = "https://24ywcg-3000.csb.app/login"; // substitua pelo IP do seu servidor

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("senha", senha);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            String token = response.getString("token");
                            // Faça algo com o token, como salvar no SharedPreferences e iniciar a próxima atividade
                            // Por exemplo:
                            // SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                            // SharedPreferences.Editor editor = sharedPreferences.edit();
                            // editor.putString("token", token);
                            // editor.apply();

                            // Redirecionar para a tela inicial (home)
                            startActivity(new Intent(MainActivity.this, MainActivity2.class));
                            finish(); // encerrar a atividade de login
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Trate erros de rede ou de servidor
                        Toast.makeText(MainActivity.this, "Erro ao fazer login: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Login", "Erro ao fazer login", error);
                    }
                });

        // Adicione a solicitação à fila de solicitações Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
