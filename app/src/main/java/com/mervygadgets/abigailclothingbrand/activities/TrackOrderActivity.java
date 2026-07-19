package com.mervygadgets.abigailclothingbrand.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.mervygadgets.abigailclothingbrand.R;

public class TrackOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        // 1. Initialize Views
        ImageButton backBtn = findViewById(R.id.backBtn);
        TextView step1 = findViewById(R.id.step1);
        TextView step2 = findViewById(R.id.step2);
        TextView step3 = findViewById(R.id.step3);
        TextView step4 = findViewById(R.id.step4);


        //contact support initialisation block code here
        android.widget.Button supportBtn = findViewById(R.id.contactSupportBtn);
        String orderId = getIntent().getStringExtra("order_id");

        // 2. Handle Back Button
        backBtn.setOnClickListener(v -> finish());

        //===Event Listenere for Contact support button here
        supportBtn.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_SENDTO);
            intent.setData(android.net.Uri.parse("mailto:support@abigail_apprel.com"));
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Support for Order #" + orderId);
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "Hello, I need help with my order: " + orderId);
            startActivity(android.content.Intent.createChooser(intent, "Contact Support via..."));
        });
        //===Event listenere ends here

        // 3. Get data from Intent
        String status = getIntent().getStringExtra("order_status");

        //Admin boolean starts  here
        boolean isAdmin = getIntent().getBooleanExtra("Admin", false);
        Button btnUpdateStatus = findViewById(R.id.btnUpdateStatus);

        if(isAdmin){
            btnUpdateStatus.setVisibility(View.VISIBLE);
        }

        //Admin Boolean ends here

        // 4. Update UI based on status
        if (status != null) {
            updateProgressSteps(status.toLowerCase(), step1, step2, step3, step4);
        }
    }

    private void updateProgressSteps(String status, TextView s1, TextView s2, TextView s3, TextView s4) {
        // Reset all to default (gray/white)
        int inactiveColor = 0xFF7A8BAD;
        int activeColor = 0xFFD4AF37; // Champagne Gold

        s1.setTextColor(inactiveColor);// Order Placed is always inactive
        s2.setTextColor(inactiveColor);
        s3.setTextColor(inactiveColor);
        s4.setTextColor(inactiveColor);

        if (status.equals("pending") || status.equals("paid")) {
            s2.setTextColor(activeColor);
        } else if (status.equals("shipped")) {
            s2.setTextColor(activeColor);
            s3.setTextColor(activeColor);
        } else if (status.equals("delivered")) {
            s2.setTextColor(activeColor);
            s3.setTextColor(activeColor);
            s4.setTextColor(activeColor);
        } else if (status.equals("cancelled")) {
            s1.setTextColor(0xFFFF4444); // Red for cancelled
        }
    }
}