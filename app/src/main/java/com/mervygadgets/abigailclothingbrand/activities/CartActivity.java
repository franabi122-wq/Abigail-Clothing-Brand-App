package com.mervygadgets.abigailclothingbrand.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mervygadgets.abigailclothingbrand.CartManager;
import com.mervygadgets.abigailclothingbrand.R;
import com.mervygadgets.abigailclothingbrand.adapters.CartAdapter;
import com.mervygadgets.abigailclothingbrand.models.CartItem;
import com.mervygadgets.abigailclothingbrand.models.Order;
import com.mervygadgets.abigailclothingbrand.network.ApiClient;
import com.mervygadgets.abigailclothingbrand.network.ApiService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings({"AndroidLintSetTextI18n", "NotifyDataSetChanged"})
public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private TextView subtotalText, deliveryText, totalText, clearCartBtn;
    private TextInputEditText addressInput, landmarkInput, nameInput, phoneInput;
    private Button checkoutBtn;
    private ImageButton backBtn;

    // Layout containers for visibility toggling
    private LinearLayout emptyCartView;
    private ScrollView mainCartContentLayout;
    private LinearLayout bottomCheckoutCard;

    private CartAdapter cartAdapter;
    private CartManager cartManager;

    private static final double DELIVERY_FEE = 10.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        cartManager = CartManager.getInstance();

        setupRecyclerView();

        backBtn.setOnClickListener(v -> finish());

        clearCartBtn.setOnClickListener(v -> {
            cartManager.clearCart();
            cartAdapter.notifyDataSetChanged();
            updateSummary();
            checkEmpty();
        });

        checkoutBtn.setOnClickListener(v -> startCheckoutProcess());

        updateSummary();
        checkEmpty();
    }

    private void initViews() {
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        subtotalText = findViewById(R.id.subtotalText);
        deliveryText = findViewById(R.id.deliveryText);
        totalText = findViewById(R.id.totalText);
        addressInput = findViewById(R.id.addressInput);
        landmarkInput = findViewById(R.id.landmarkInput);
        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        checkoutBtn = findViewById(R.id.checkoutBtn);
        clearCartBtn = findViewById(R.id.clearCartBtn);
        backBtn = findViewById(R.id.backBtn);

        // Connected your new UI containers
        emptyCartView = findViewById(R.id.emptyCartView);
        mainCartContentLayout = findViewById(R.id.mainCartContentLayout);
        bottomCheckoutCard = findViewById(R.id.bottomCheckoutCard);
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(this, cartManager.getCartItems(), new CartAdapter.OnCartActionListener() {
            @Override
            public void onIncrease(int position) {
                cartManager.increaseQuantity(position);
                cartAdapter.notifyItemChanged(position);
                updateSummary();
            }

            @Override
            public void onDecrease(int position) {
                cartManager.decreaseQuantity(position);
                cartAdapter.notifyDataSetChanged();
                updateSummary();
                checkEmpty();
            }

            @Override
            public void onDelete(int position) {
                cartManager.removeFromCart(position);
                cartAdapter.notifyItemRemoved(position);
                updateSummary();
                checkEmpty();
            }
        });
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);
    }

    private void startCheckoutProcess() {
        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please complete your delivery details!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cartManager.getItemCount() == 0) {
            Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        checkoutBtn.setEnabled(false);
        checkoutBtn.setText("Processing Payment...");

        finalizeOrder();
    }

    private void finalizeOrder() {
        checkoutBtn.setText("Saving Order...");

        StringBuilder itemsList = new StringBuilder();
        for (CartItem item : cartManager.getCartItems()) {
            itemsList.append(item.getProduct().getName())
                    .append(" (x").append(item.getQuantity()).append("), ");
        }

        double finalAmount = cartManager.getTotal() + DELIVERY_FEE;

        Order order = new Order();
        order.setCustomerName(nameInput.getText().toString().trim());
        order.setCustomerPhone(phoneInput.getText().toString().trim());
        order.setAddress(addressInput.getText().toString().trim());
        order.setLandmark(landmarkInput.getText().toString().trim());
        order.setTotalAmount(finalAmount);
        order.setStatus("paid");
        order.setItems(itemsList.toString());

        List<Order> orderBulkList = Collections.singletonList(order);

        ApiService apiService = ApiClient.getApiService(this);
        apiService.createOrder(orderBulkList).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // THIS WILL FORCE LOGCAT TO PRINT THE CODE (e.g., 400, 401, 201)
                android.util.Log.e("SUPABASE_RUN", "Response Code: " + response.code());

                if (response.isSuccessful()) {
                    android.util.Log.e("SUPABASE_RUN", "SUCCESSFULLY INSERTED!");
                    checkoutBtn.setEnabled(true);
                    checkoutBtn.setText("Checkout with MoMo");
                    cartManager.clearCart();
                    startActivity(new Intent(CartActivity.this, OrderSuccessActivity.class));
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Empty body";
                        // THIS PRINTS THE EXACT DATABASE ERROR STRING
                        android.util.Log.e("SUPABASE_RUN", "REJECTION REASON: " + errorBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    checkoutBtn.setEnabled(true);
                    checkoutBtn.setText("Checkout with MoMo");
                    Toast.makeText(CartActivity.this, "Server rejected request!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                android.util.Log.e("SUPABASE_RUN", "PIPELINE FAILURE: ", t);
                checkoutBtn.setEnabled(true);
                checkoutBtn.setText("Checkout with MoMo");
                Toast.makeText(CartActivity.this, "Network pipeline failure!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSummary() {
        double subtotal = cartManager.getTotal();
        double total = subtotal + DELIVERY_FEE;
        subtotalText.setText("GHS " + String.format("%.2f", subtotal));
        deliveryText.setText("GHS " + String.format("%.2f", DELIVERY_FEE));
        totalText.setText("GHS " + String.format("%.2f", total));
    }

    private void checkEmpty() {
        if (cartManager.getItemCount() == 0) {
            // Show the luxury empty message directly in the center screen space
            emptyCartView.setVisibility(View.VISIBLE);

            // Hide the text boxes, item list, and summary card completely
            mainCartContentLayout.setVisibility(View.GONE);
            bottomCheckoutCard.setVisibility(View.GONE);
        } else {
            // Keep empty screen components hidden when items exist
            emptyCartView.setVisibility(View.GONE);
            mainCartContentLayout.setVisibility(View.VISIBLE);
            bottomCheckoutCard.setVisibility(View.VISIBLE);
        }
    }
}