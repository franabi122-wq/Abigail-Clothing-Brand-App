package com.mervygadgets.abigailclothingbrand.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mervygadgets.abigailclothingbrand.R;
import com.mervygadgets.abigailclothingbrand.adapters.OrderAdapter;
import com.mervygadgets.abigailclothingbrand.models.Order;
import com.mervygadgets.abigailclothingbrand.network.ApiClient;
import com.mervygadgets.abigailclothingbrand.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private LinearLayout emptyState;
    private TextView tabAll, tabPending, tabDelivered, tabCancelled;
    private ImageButton backBtn;

    private OrderAdapter orderAdapter;
    private List<Order> allOrders = new ArrayList<>();
    private List<Order> filteredOrders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        emptyState = findViewById(R.id.emptyState);
        tabAll = findViewById(R.id.tabAll);
        tabPending = findViewById(R.id.tabPending);
        tabDelivered = findViewById(R.id.tabDelivered);
        tabCancelled = findViewById(R.id.tabCancelled);
        backBtn = findViewById(R.id.backBtn);

        orderAdapter = new OrderAdapter(this, filteredOrders, order -> {
            Toast.makeText(this, "Order by " + order.getCustomerName(), Toast.LENGTH_SHORT).show();
        });

        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ordersRecyclerView.setAdapter(orderAdapter);

        backBtn.setOnClickListener(v -> finish());

        tabAll.setOnClickListener(v -> {
            setActiveTab(tabAll);
            filterOrders("all");
        });

        tabPending.setOnClickListener(v -> {
            setActiveTab(tabPending);
            filterOrders("pending");
        });

        tabDelivered.setOnClickListener(v -> {
            setActiveTab(tabDelivered);
            filterOrders("delivered");
        });

        tabCancelled.setOnClickListener(v -> {
            setActiveTab(tabCancelled);
            filterOrders("cancelled");
        });

        loadOrders();
    }

    private void loadOrders() {
        ApiService apiService = ApiClient.getApiService(this);
        apiService.getOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allOrders.clear();
                    allOrders.addAll(response.body());
                    filteredOrders.clear();
                    filteredOrders.addAll(allOrders);
                    orderAdapter.updateList(filteredOrders);
                    checkEmpty();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                //Toast.makeText(OrderHistoryActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();

                //emptyState.setVisibility(View.VISIBLE);
                //ordersRecyclerView.setVisibility(View.GONE);

                // 2. Give the user a quick toast
                Toast.makeText(OrderHistoryActivity.this, "Connection lost, retrying...", Toast.LENGTH_SHORT).show();


                /*new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                    if (!isFinishing()) {
                        loadOrders(); // This will try to fetch the data again!
                    }
                }, 3000);*/
            }
        });
    }

    private void filterOrders(String status) {
        filteredOrders.clear();

        for (Order o : allOrders) {
            // This will print the actual status from Supabase to your Logcat
            android.util.Log.d("OrderDebug", "Comparing: " + o.getStatus() + " with: " + status);

            if (status.equals("all")) {
                filteredOrders.add(o);
            } else if (status.equals("pending")) {
                if (o.getStatus() != null && (o.getStatus().equalsIgnoreCase("pending") || o.getStatus().equalsIgnoreCase("paid"))) {
                    filteredOrders.add(o);
                }
            } else {
                if (o.getStatus() != null && o.getStatus().equalsIgnoreCase(status)) {
                    filteredOrders.add(o);
                }
            }
        }
        orderAdapter.updateList(filteredOrders);
        checkEmpty();
    }

    private void setActiveTab(TextView activeTab) {
        tabAll.setTextColor(0xFF7A8BAD);
        tabPending.setTextColor(0xFF7A8BAD);
        tabDelivered.setTextColor(0xFF7A8BAD);
        tabCancelled.setTextColor(0xFF7A8BAD);
        tabAll.setBackgroundColor(0x00000000);
        tabPending.setBackgroundColor(0x00000000);
        tabDelivered.setBackgroundColor(0x00000000);
        tabCancelled.setBackgroundColor(0x00000000);

        activeTab.setTextColor(0xFFFFFFFF);
        activeTab.setBackgroundColor(0xFFD4AF37);
    }

    private void checkEmpty() {
        if (filteredOrders.isEmpty()) {
            ordersRecyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            ordersRecyclerView.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }
    }
}