package com.mervygadgets.abigailclothingbrand.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mervygadgets.abigailclothingbrand.R;
import com.mervygadgets.abigailclothingbrand.data.TokenManager;
import com.mervygadgets.abigailclothingbrand.models.LoginRequest;
import com.mervygadgets.abigailclothingbrand.models.LoginResponse;
import com.mervygadgets.abigailclothingbrand.network.ApiClient;
import com.mervygadgets.abigailclothingbrand.network.ApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    private TokenManager tokenManager;

    EditText etEmail, etPassword;
    Button btnLogin, btnForgotPassword;
    TextView gotoSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        tokenManager = new TokenManager(this);


        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        gotoSignup = findViewById(R.id.goToSignup);


        //btnForgotPassword code logic starts here
        btnForgotPassword.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email first", Toast.LENGTH_SHORT).show();
                return;
            }

            //  recovery call code logic
            Toast.makeText(this, "Sending reset link to " + email, Toast.LENGTH_SHORT).show();
        });
        //ends here

        //gotoLogin starts here
        gotoSignup.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
        //gotoLogin ends here

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();


            // This is where I will add the Supabase auth call!
            LoginRequest loginRequest = new LoginRequest(email, password);

            //ApiService Authentication code block
            ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

            //String Bearer Token
            //String bearerToken = "Bearer" + ApiClient.API_KEY;

            apiService.login(loginRequest).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //if response starts here
                    if (response.isSuccessful()) {
                        try {
                            //JSON String parse
                            String responseBody = response.body().string();

                            com.google.gson.Gson gson = new com.google.gson.Gson();
                            LoginResponse loginResponse = gson.fromJson(responseBody, LoginResponse.class);

                            String myAccessToken = loginResponse.getAccessToken();
                            String myRefreshToken = loginResponse.getRefreshToken();

                            //Assumptionof backend/response sent back from tokens
                            tokenManager.saveTokens(myAccessToken, myRefreshToken);

                            android.util.Log.d("LOGIN_DEBUG", "Login successful, attempting to start MainActivity");

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            //finish();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error processing login", Toast.LENGTH_SHORT).show();
                            //throw new RuntimeException(e);
                        }

                        //if block code ends here

                    } else {
                        // Handle error (e.g., incorrect password)
                        String errorMsg = "Unknown Error";
                        try {
                            // This gets the specific error message from your server/Supabase
                            if (response.errorBody() != null) {
                                errorMsg = response.errorBody().string();
                            }
                        } catch (java.io.IOException e) {
                            errorMsg = "Code: " + response.code();
                        }
                        Toast.makeText(LoginActivity.this, "Failed: " + errorMsg, Toast.LENGTH_LONG).show();
                        //Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }//OnCreate method ends here
}