package com.mervygadgets.abigailclothingbrand.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import com.mervygadgets.abigailclothingbrand.activities.MainActivity;
import com.mervygadgets.abigailclothingbrand.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        // 2. Safely find the progress bar matching our new XML ID
        ProgressBar progressBar = findViewById(R.id.progressBar);

        // 3. Move cleanly to MainActivity after 3 seconds
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}