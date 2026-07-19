package com.mervygadgets.abigailclothingbrand;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mervygadgets.abigailclothingbrand.activities.AdminActivity;
import com.mervygadgets.abigailclothingbrand.activities.LoginActivity;
import com.mervygadgets.abigailclothingbrand.activities.SignupActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);


        Button btnLogin = findViewById(R.id.btn_login);
        Button btnSignup = findViewById(R.id.btn_signup);
        Button btnAdmin = findViewById(R.id.btn_admin);

        btnLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        btnSignup.setOnClickListener(v -> startActivity(new Intent(this, SignupActivity.class)));
        btnAdmin.setOnClickListener(v -> startActivity(new Intent(this, AdminActivity.class)));
        }
    }
