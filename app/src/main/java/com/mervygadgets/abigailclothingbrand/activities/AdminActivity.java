
package com.mervygadgets.abigailclothingbrand.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mervygadgets.abigailclothingbrand.R;
import com.mervygadgets.abigailclothingbrand.adapters.OrderAdapter;
import com.mervygadgets.abigailclothingbrand.adapters.ProductAdapter;
import com.mervygadgets.abigailclothingbrand.models.Order;
import com.mervygadgets.abigailclothingbrand.models.Product;
import com.mervygadgets.abigailclothingbrand.network.ApiClient;
import com.mervygadgets.abigailclothingbrand.network.ApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView adminRecyclerView;
    private TextView totalOrdersCount, pendingOrdersCount, totalRevenueCount;
    private TextView tabOrders, tabProducts, logoutBtn;

    private View errorLayout;

    private Button btnRetry;
    private Button addProductBtn;
    private ImageButton backBtn;

    private OrderAdapter orderAdapter;
    private List<Order> orderList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();

    private static final String ADMIN_PASSWORD = "bigboss2025";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Init views
        adminRecyclerView = findViewById(R.id.adminRecyclerView);
        totalOrdersCount = findViewById(R.id.totalOrdersCount);
        pendingOrdersCount = findViewById(R.id.pendingOrdersCount);
        totalRevenueCount = findViewById(R.id.totalRevenueCount);
        tabOrders = findViewById(R.id.tabOrders);
        tabProducts = findViewById(R.id.tabProducts);
        logoutBtn = findViewById(R.id.logoutBtn);
        addProductBtn = findViewById(R.id.addProductBtn);
        backBtn = findViewById(R.id.backBtn);
        errorLayout = findViewById(R.id.errorLayout);
        btnRetry = findViewById(R.id.btnRetry);

        //btnRetry logic here
        btnRetry.setOnClickListener(v -> loadOrders());

        // Setup adapter
        orderAdapter = new OrderAdapter(this, orderList, order -> {
            Toast.makeText(this, "Order - " + order.getStatus(), Toast.LENGTH_SHORT).show();
        });

        productAdapter = new ProductAdapter(this, productList, new ProductAdapter.OnProductClickListener(){
            @Override
            public void onProductClick(Product product){
                //code displayed here
            }


            @Override
            public void onAddToCartClick(Product product){
                //code displayed here
                new AlertDialog.Builder(AdminActivity.this)
                        .setTitle("Manage Product")
                        .setMessage("What would you like to do with " + product.getName() + "?")
                        .setPositiveButton("Edit", (dialog, which) -> {
                            Intent intent = new Intent(AdminActivity.this, EditProductActivity.class);
                            intent.putExtra("product", product);
                            startActivity(intent);
                        })
                                .setNegativeButton("Delete", (dialog, which) ->{
                                    //deleteProduct(String.valueOf(product.getId()));
                                    deleteProduct("eq." + product.getId());
                                    })
                                        .setNeutralButton("Cancel", null)
                                                .show();

                //Toast.makeText(AdminActivity.this, "Edit/Delete: " + product.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        adminRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adminRecyclerView.setAdapter(orderAdapter);

        backBtn.setOnClickListener(v -> finish());

        logoutBtn.setOnClickListener(v -> finish());


        //loadProdycts() method declared here

        //tab page starts here
        tabOrders.setOnClickListener(v -> {
            tabOrders.setTextColor(0xFFFFFFFF);
            tabOrders.setBackgroundColor(0xFF2979FF);
            tabProducts.setTextColor(0xFF7A8BAD);
            tabProducts.setBackgroundColor(0x00000000);

            adminRecyclerView.setAdapter((orderAdapter));
            loadOrders();
        });

        tabProducts.setOnClickListener(v -> {
            tabProducts.setTextColor(0xFFFFFFFF);
            tabProducts.setBackgroundColor(0xFF2979FF);
            tabOrders.setTextColor(0xFF7A8BAD);
            tabOrders.setBackgroundColor(0x00000000);

            adminRecyclerView.setAdapter(productAdapter);
            loadProducts();
            //Toast.makeText(this, "Products tab coming soon!", Toast.LENGTH_SHORT).show();
        });
        //tab page ends here


        addProductBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, EditProductActivity.class);
            startActivity(intent);
            //Toast.makeText(this, "Add product coming soon!", Toast.LENGTH_SHORT).show();
        });

        loadOrders();
    }//onCreate() ends here

    //private void loadOrders method starts here()
    private void loadOrders() {
        ApiService apiService = ApiClient.getApiService(this);
        apiService.getOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //error lists success starts here
                    errorLayout.setVisibility(View.GONE);
                    adminRecyclerView.setVisibility(View.VISIBLE);

                    orderList.clear();
                    orderList.addAll(response.body());
                    orderAdapter.updateList(orderList);
                    //orderAdapter.updateList(orderList);
                    updateStats();
                }else{

                    //errorLayout.setVisibility(View.VISIBLE);
                    //adminRecyclerView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                // Toast to let the user know what's happening
                Toast.makeText(AdminActivity.this, "Check internet to sync latest data", Toast.LENGTH_SHORT).show();

                // Show your error layout instead of a blank screen
                // errorLayout.setVisibility(View.VISIBLE);


               /* new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                    if (!isFinishing()) {
                        loadOrders(); // Use loadOrders() for Admin/Order pages
                    }
                }, 3000);*/
            }
        });
    }//load orders code ends here

    //load Products here
    private void loadProducts() {
        ApiService apiService = ApiClient.getApiService(this);
        apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    errorLayout.setVisibility(View.GONE);
                    adminRecyclerView.setVisibility(View.VISIBLE);

                    // Clear the Product list, not the Order list
                    productList.clear();
                    productList.addAll(response.body());
                    // Update the Product adapter
                    productAdapter.updateList(productList);
                } else {
                    errorLayout.setVisibility(View.VISIBLE);
                    adminRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                errorLayout.setVisibility(View.VISIBLE);
                adminRecyclerView.setVisibility(View.GONE);
                Toast.makeText(AdminActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //load products ends here




    //updateOrderInSupabase method starts here
    public void updateOrderInSupabase(String orderId, String newStatus) {
        ApiService apiService1 = ApiClient.getApiService(this);
        Map<String, String> body = new HashMap<>();
        body.put("status", newStatus);

        apiService1.updateOrderStatus(orderId, body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AdminActivity.this, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
                    loadOrders();
                } else {
                    Toast.makeText(AdminActivity.this, "Update failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AdminActivity.this, "Connection failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //endshere



private void updateStats() {
        int total = orderList.size();
        int pending = 0;
        double revenue = 0;

        for (Order o : orderList) {
            if (o.getStatus() != null && o.getStatus().equalsIgnoreCase("pending")) {
                pending++;
            }
            revenue += o.getTotalAmount();
        }

        totalOrdersCount.setText(String.valueOf(total));
        pendingOrdersCount.setText(String.valueOf(pending));
        totalRevenueCount.setText("GHS " + String.format("%.0f", revenue));
    }

    //delete function block starts here
    public void deleteProduct(String productId) {
        ApiService apiService = ApiClient.getApiService(this);

        apiService.deleteProduct(productId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AdminActivity.this, "Product deleted successfully!", Toast.LENGTH_SHORT).show();
                    // Refresh the product list so the deleted item disappears
                    loadProducts();
                } else {
                    Toast.makeText(AdminActivity.this, "Delete failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AdminActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //ends here
}