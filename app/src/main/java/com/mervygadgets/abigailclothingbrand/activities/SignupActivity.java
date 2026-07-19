package com.mervygadgets.abigailclothingbrand.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mervygadgets.abigailclothingbrand.R;
import com.mervygadgets.abigailclothingbrand.models.UserRequest;
import com.mervygadgets.abigailclothingbrand.network.ApiClient;
import com.mervygadgets.abigailclothingbrand.network.ApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    EditText etName, etEmail, etPassword;
    Button btnSignup;
    TextView gotoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

      //block starts here
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);
        gotoLogin = findViewById(R.id.goToLogin);

        btnSignup.setOnClickListener(v -> {
                    String name = etName.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    //if-else block here
                    if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        return; // Stop here if fields are empty
                    }

            //ends here

            // Create your request model
            UserRequest signupRequest = new UserRequest(name, email, password);

            //ApiService token header code
            ApiService apiService = ApiClient.getClient(this).create(ApiService.class);


            //String Bearer Token
            //String bearerToken = "Bearer " + ApiClient.API_KEY;

            // Call the signup method
            apiService.signUp(signupRequest).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Signup Successful! Please Login.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        finish();
                    } else {
                     //logic starts here
                        String errorMsg = "Unknown Error";
                        try {
                            // This gets the specific error message from your server/Supabase
                            if (response.errorBody() != null) {
                                errorMsg = response.errorBody().string();
                                android.util.Log.e("SUPABASE_ERROR", errorMsg);
                            }
                        } catch (java.io.IOException e) {
                            errorMsg = "Code: " + response.code();
                        }
                        Toast.makeText(SignupActivity.this, "Failed: " + errorMsg, Toast.LENGTH_LONG).show();
                        //logic ends here
                       // Toast.make Text(SignupActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(SignupActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
        //block ends here
        });

        //gotoLogin page starts here
        gotoLogin.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        //gotoLogin page ends here
    }
}//ending block