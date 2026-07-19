package com.mervygadgets.abigailclothingbrand.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mervygadgets.abigailclothingbrand.CartManager;
import com.mervygadgets.abigailclothingbrand.R;
import com.mervygadgets.abigailclothingbrand.WishlistManager;
import com.mervygadgets.abigailclothingbrand.adapters.ProductAdapter;
import com.mervygadgets.abigailclothingbrand.models.Product;
import com.mervygadgets.abigailclothingbrand.models.WishlistItem;

import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends AppCompatActivity {

    private RecyclerView wishlistRecyclerView;
    private LinearLayout emptyState;
    private TextView clearWishlistBtn;
    private Button addAllToCartBtn;
    private ImageButton backBtn;

    private ProductAdapter productAdapter;
    private WishlistManager wishlistManager;
    private List<Product> wishlistProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        wishlistRecyclerView = findViewById(R.id.wishlistRecyclerView);
        emptyState = findViewById(R.id.emptyState);
        clearWishlistBtn = findViewById(R.id.clearWishlistBtn);
        addAllToCartBtn = findViewById(R.id.addAllToCartBtn);
        backBtn = findViewById(R.id.backBtn);

        wishlistManager = WishlistManager.getInstance();

        loadWishlist();

        productAdapter = new ProductAdapter(this, wishlistProducts, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                // open detail
            }

            @Override
            public void onAddToCartClick(Product product) {
                CartManager.getInstance().addToCart(product);
                Toast.makeText(WishlistActivity.this, product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            }
        });

        wishlistRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        wishlistRecyclerView.setAdapter(productAdapter);

        backBtn.setOnClickListener(v -> finish());

        clearWishlistBtn.setOnClickListener(v -> {
            wishlistManager.clearWishlist();
            loadWishlist();
            productAdapter.updateList(wishlistProducts);
            checkEmpty();
        });

        addAllToCartBtn.setOnClickListener(v -> {
            for (Product p : wishlistProducts) {
                CartManager.getInstance().addToCart(p);
            }
            Toast.makeText(this, "All items added to cart!", Toast.LENGTH_SHORT).show();
        });

        checkEmpty();
    }

    private void loadWishlist() {
        wishlistProducts.clear();
        for (WishlistItem item : wishlistManager.getWishlistItems()) {
            wishlistProducts.add(item.getProduct());
        }
    }

    private void checkEmpty() {
        if (wishlistProducts.isEmpty()) {
            wishlistRecyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            wishlistRecyclerView.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }
    }
}
