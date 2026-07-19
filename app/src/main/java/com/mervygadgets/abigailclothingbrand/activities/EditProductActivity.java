package com.mervygadgets.abigailclothingbrand.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mervygadgets.abigailclothingbrand.R;
import com.mervygadgets.abigailclothingbrand.models.Product;
import com.mervygadgets.abigailclothingbrand.network.ApiClient;
import com.mervygadgets.abigailclothingbrand.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductActivity extends AppCompatActivity {
    private EditText etName, etPrice, etCategory, etDescription, etImageUrl;
    private Button btnSave;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_product);

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etCategory = findViewById(R.id.etCategory);
        etDescription = findViewById(R.id.etDescription);
        etImageUrl = findViewById(R.id.etImageUrl);
        btnSave = findViewById(R.id.btnSave);


        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            etName.setText(product.getName());
            etPrice.setText(String.valueOf(product.getPrice()));
            etCategory.setText(product.getCategory());
            etDescription.setText(product.getDescription());
            etImageUrl.setText(product.getImageUrl());
        }else{
            product = new Product();
        }

        btnSave.setOnClickListener(v -> {
            Toast.makeText(EditProductActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
            updateProductInSupabase();
        });
    }

    //Declaration of updateProductInSupabase starts here
    private void updateProductInSupabase() {
        android.util.Log.d("DEBUG_TAG", "Save button was clicked");

        //if block starts here
        if(etName.getText().toString().isEmpty() || etPrice.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill in Name and  price!", Toast.LENGTH_SHORT).show();
            return;
        }
        //if bock ends here

        //extra safety validation starts here
        if(product == null){
            product = new Product();
        }
        //extra safety validation ends here

            //Assignment of value ends here

        // 4. Get values  all fields
        product.setName(etName.getText().toString());
        product.setCategory(etCategory.getText().toString());
        product.setDescription(etDescription.getText().toString());
        product.setImageUrl(etImageUrl.getText().toString());

      try{
          product.setPrice(Double.parseDouble(etPrice.getText().toString()));
      } catch (NumberFormatException e) {
          Toast.makeText(this, "Invalid Price format!", Toast.LENGTH_SHORT).show();
          return;
      }


        ApiService apiService = ApiClient.getApiService(this);

        if (product.getId() == null || product.getId() == 0) {
            product.setId(null);
            // CALL ADD PRODUCT
            apiService.addProduct(product).enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(EditProductActivity.this, "Added successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(EditProductActivity.this, "Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    Toast.makeText(EditProductActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // CALL UPDATE PRODUCT (Existing PATCH logic)
            String idFilter = "eq." + product.getId();
            apiService.patchProduct(idFilter, product).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(EditProductActivity.this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(EditProductActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    //Declaration of updateProductInSupabase ends here

}