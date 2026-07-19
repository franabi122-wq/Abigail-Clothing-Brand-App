package com.mervygadgets.abigailclothingbrand.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Looper;
import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mervygadgets.abigailclothingbrand.CartManager;
import com.mervygadgets.abigailclothingbrand.ProfileActivity;
import com.mervygadgets.abigailclothingbrand.R;
import com.mervygadgets.abigailclothingbrand.adapters.CategoryAdapter;
import com.mervygadgets.abigailclothingbrand.adapters.ProductAdapter;
import com.mervygadgets.abigailclothingbrand.adapters.BannerAdapter;
import com.mervygadgets.abigailclothingbrand.models.Product;
import com.mervygadgets.abigailclothingbrand.network.ApiClient;
import com.mervygadgets.abigailclothingbrand.network.ApiService;
import com.mervygadgets.abigailclothingbrand.repository.ProductRepository;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings({"FieldCanBeLocal","FieldMayBeFinal", "Convert2Lambda", "UnusedImport"})
public class MainActivity extends AppCompatActivity {
    //handler and Runnable interface declared here
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    //handler and Runnable interface declaration ends here
    private RecyclerView categoryRecyclerView, productRecyclerView;
    private EditText searchEditText;
    private android.widget.Button viewAllBtn;

    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;

    private List<Product> allProducts = new ArrayList<>();
    private List<Product> filteredProducts = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    @SuppressLint("WrongViewCast")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //--new Banner logic starts here---
        ViewPager2 bannerViewPager = findViewById(R.id.bannerViewPager);
        List<Integer> bannerImages = Arrays.asList(
                R.drawable.banner1,
                R.drawable.banner2,
                R.drawable.banner3
        );

        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        bannerViewPager.setAdapter(bannerAdapter);

        //Dots indicator declared here---
        DotsIndicator dotsIndicator = findViewById(R.id.dotsIndicator);
        dotsIndicator.attachTo(bannerViewPager);
        //Dots indicator ends here---



        //Handler handler = new Handler(Looper.getMainLooper());
        //Runnable runnable = new Runnable() {
        runnable = new Runnable(){
            @Override
            public void run() {
                int nextItem = bannerViewPager.getCurrentItem() + 1;
                if (nextItem >= bannerImages.size()) nextItem = 0;
                bannerViewPager.setCurrentItem(nextItem, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
        //--new Banner logic ends here-----


        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        productRecyclerView = findViewById(R.id.productRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        viewAllBtn = findViewById(R.id.viewAllBtn);

        categories.addAll(Arrays.asList("All", "Dresses", "Tops", "Bottoms", "Outerwears", "Footwears", "Accessories"));

        categoryAdapter = new CategoryAdapter(this, categories, (category, position) -> {
            filterByCategory(category);
        });

        categoryRecyclerView.setAdapter(categoryAdapter);

        productAdapter = new ProductAdapter(this, filteredProducts, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("product_id", product.getId());
                intent.putExtra("product_name", product.getName());
                intent.putExtra("product_price", product.getPrice());
                intent.putExtra("product_image", product.getImageUrl());
                intent.putExtra("product_description", product.getDescription());
                intent.putExtra("product_category", product.getCategory());
                startActivity(intent);
            }

            @Override
            public void onAddToCartClick(Product product) {
                CartManager.getInstance().addToCart(product);
                Toast.makeText(MainActivity.this, product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            }
        });

        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productRecyclerView.setAdapter(productAdapter);

        viewAllBtn = findViewById(R.id.viewAllBtn);
        if (viewAllBtn != null) {
            viewAllBtn.setOnClickListener(v -> {
                filterByCategory("All");
                Toast.makeText(this, "Showing all products", Toast.LENGTH_SHORT).show();
            });
        }

        if (searchEditText != null) {
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (productAdapter != null) {
                        productAdapter.getFilter().filter(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }


        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        if (bottomNav != null) {
            bottomNav.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    return true;
                } else if (id == R.id.nav_cart) {
                    startActivity(new Intent(this, CartActivity.class));
                    return true;
                } else if (id == R.id.nav_orders) {
                    startActivity(new Intent(this, OrderHistoryActivity.class));
                    return true;
                } else if (id == R.id.nav_wishlist) {
                    startActivity(new Intent(this, WishlistActivity.class));
                    return true;
                }
                //Profile tab logic
                else if (id == R.id.navigation_profile){
                    startActivity(new Intent(this, ProfileActivity.class));
                    return true;
                }
                return false;
            });
        }
        loadProducts();

    }//end of Oncreate() method function

    //onResume method starts here
    @Override
    protected void onResume(){
        super.onResume();

        loadProducts();

        if(runnable != null){
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 3000);
        }
    }
    //onResume method ends here
    //initialisation of onPause() starts here---
    @Override
    protected void onPause() {
        super.onPause();
        // This stops the auto-scrolling when the app is not in the foreground
        handler.removeCallbacks(runnable);
    }
    //initialisation of onPause() ends here here---

    //loadProducts() starts here
    private void loadProducts() {
        ApiService apiService = ApiClient.getApiService(this);

        apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // WRAP THE UI UPDATES IN THIS BLOCK
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            allProducts.clear();
                            allProducts.addAll(response.body());
                            filteredProducts.clear();
                            filteredProducts.addAll(allProducts);
                            productAdapter.updateList(filteredProducts);
                            ProductRepository.getInstance().setAllProducts(allProducts);
                        }
                    });
                    Log.d("DEBUG_TAG", "Total products now: " + allProducts.size());
                } else {
                    Log.d("DEBUG_TAG", "Response error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Even in failure, you can show a toast using runOnUiThread
                runOnUiThread(() -> {
                    //Toast.makeText(MainActivity.this, "Syncing Catalog... " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Syncing Catalog... ", Toast.LENGTH_SHORT).show();
                });

                // YOUR NEW RETRY LOGIC
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (!isFinishing()) loadProducts();
                }, 5000);
            }
        });
    }
    //load products ends here

    private void filterByCategory(String category) {
        filteredProducts.clear();
        if (category.equals("All")) {
            filteredProducts.addAll(allProducts);
        } else {
            for (Product p : allProducts) {
                if (p.getCategory().equalsIgnoreCase(category)) {
                    filteredProducts.add(p);
                }
            }
        }
        productAdapter.updateList(filteredProducts);
    }
}