package com.mervygadgets.abigailclothingbrand.activities;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.mervygadgets.abigailclothingbrand.R;
import com.mervygadgets.abigailclothingbrand.activities.MainActivity;

public class OrderSuccessActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        Button continueShoppingBtn = findViewById(R.id.continueShoppingBtn);

        //when clicked instanceg
        continueShoppingBtn.setOnClickListener(v ->{
            Intent intent = new Intent(OrderSuccessActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
