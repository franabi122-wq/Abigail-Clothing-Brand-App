package com.mervygadgets.abigailclothingbrand.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.mervygadgets.abigailclothingbrand.R;
import com.mervygadgets.abigailclothingbrand.models.Product;
import com.mervygadgets.abigailclothingbrand.network.ApiClient;
import com.mervygadgets.abigailclothingbrand.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {

    private EditText etName, etPrice, etCategory ,etDescription, etImageUrl;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Initialize  views here
        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etCategory = findViewById(R.id.etCategory); // Make sure this ID is in your XML!
        etDescription = findViewById(R.id.etDescription);
        etImageUrl = findViewById(R.id.etImageUrl);
        btnSave = findViewById(R.id.btnSave);
        // ... find the rest ...

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> saveProductToSupabase());
    }

    private void saveProductToSupabase() {
        //API_DEBUG code starts here
        Log.d("API_DEBUG", "Save button was clicked! Starting network call...");
        // Here is where you will use your ApiService to POST the new product
        // 1. Get data from UI
        String name = etName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please enter name and price", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);

        // 2. Create the Product object
        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setPrice(price);
        newProduct.setCategory(category);
        newProduct.setDescription(description);
        newProduct.setImageUrl(imageUrl);
        newProduct.setStock(10); // Default stock

        //New API Service launch
        Log.d("API_DEBUG", "About to enqueue the network call...");
        // 3. Send to API
        ApiService apiService = ApiClient.getApiService(this);
        Log.d("API_DEBUG", "Sending: " + name + ", "+ price +", "+ category);
        apiService.addProduct(newProduct).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    Log.d("API_SUCCESS", "Data sent successfully!"); // Check this in Logcat
                    Toast.makeText(AddProductActivity.this, "Product added successfully!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Log.e("API_ERROR", "Error code: " + response.code()); // Check this in Logcat
                    Toast.makeText(AddProductActivity.this, "Server Error: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API_FAILURE", "Error: " + t.getMessage());
                Toast.makeText(AddProductActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}



