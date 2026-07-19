package com.mervygadgets.abigailclothingbrand.activities;

import android.content.Intent;
import android.drm.ProcessedData;
import android.view.View;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.mervygadgets.abigailclothingbrand.CartManager;
import com.mervygadgets.abigailclothingbrand.R;
import com.mervygadgets.abigailclothingbrand.WishlistManager;
import com.mervygadgets.abigailclothingbrand.adapters.ProductAdapter;
import com.mervygadgets.abigailclothingbrand.models.Product;
import com.mervygadgets.abigailclothingbrand.repository.ProductRepository;
import com.mervygadgets.abigailclothingbrand.models.Review;
import com.mervygadgets.abigailclothingbrand.adapters.ReviewAdapter;
import com.mervygadgets.abigailclothingbrand.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productPrice, productCategory, productDescription, qtyCount, reviewCountText;
    private Button addToCartBtn, buyNowBtn, increaseQty, decreaseQty, writeReviewButton;
    private EditText reviewNameInput;
    private EditText reviewCommentInput;
    private RatingBar reviwRatingBar;
    private ImageButton backBtn, wishlistBtn, shareBtn;
    private RecyclerView relatedRecyclerView;
    private ProductAdapter relatedAdapter;
    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    private ReviewRepository reviewRepository;



    private Product product;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productCategory = findViewById(R.id.productCategory);
        productDescription = findViewById(R.id.productDescription);
        qtyCount = findViewById(R.id.qtyCount);
        addToCartBtn = findViewById(R.id.addToCartBtn);
        buyNowBtn = findViewById(R.id.buyNowBtn);
        increaseQty = findViewById(R.id.increaseQty);
        decreaseQty = findViewById(R.id.decreaseQty);
        backBtn = findViewById(R.id.backBtn);
        wishlistBtn = findViewById(R.id.wishlistBtn);
       shareBtn = findViewById(R.id.shareBtn);
       writeReviewButton = findViewById(R.id.writeReviewBtn);
       reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
       reviewCountText = findViewById(R.id.reviewCountText);

       //reviews section starts here
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewRepository = new ReviewRepository(this);
        //reviews layout ends here

        product = new Product();
        product.setId(getIntent().getIntExtra("product_id", 0));
        product.setName(getIntent().getStringExtra("product_name"));
        product.setPrice(getIntent().getDoubleExtra("product_price", 0));
        product.setImageUrl(getIntent().getStringExtra("product_image"));
        product.setDescription(getIntent().getStringExtra("product_description"));
        product.setCategory(getIntent().getStringExtra("product_category"));

        //check for product ID
        Toast.makeText(this, "Product ID = " + product.getId(),
                Toast.LENGTH_SHORT).show();

        //loadReviews() declared here
        loadReviews();


        productName.setText(product.getName());
        productPrice.setText("GHS " + String.format("%.2f", product.getPrice()));
        productCategory.setText(product.getCategory());
        productDescription.setText(product.getDescription());

        Glide.with(this)
                .load(product.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(productImage);




        backBtn.setOnClickListener(v -> finish());

        wishlistBtn.setOnClickListener(v -> {
            WishlistManager.getInstance().addToWishlist(product);
            Toast.makeText(this, "Added to wishlist!", Toast.LENGTH_SHORT).show();
        });

        shareBtn.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing product from Abigail Apparel: " + product.getName());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, "Share via");
            startActivity(shareIntent);
        });

        increaseQty.setOnClickListener(v -> {
            quantity++;
            qtyCount.setText(String.valueOf(quantity));
        });

        decreaseQty.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                qtyCount.setText(String.valueOf(quantity));
            }
        });

        addToCartBtn.setOnClickListener(v -> {
            for (int i = 0; i < quantity; i++) {
                CartManager.getInstance().addToCart(product);
            }
            Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
        });

        buyNowBtn.setOnClickListener(v -> {
            for (int i = 0; i < quantity; i++) {
                CartManager.getInstance().addToCart(product);
            }
            startActivity(new Intent(this, CartActivity.class));
        });

        //write review set on click listenere code here
        writeReviewButton.setOnClickListener(v ->{
            showWriteReviewDialog();
        });

     //Note buyNowBtn() dupliacate cancelled here
        //Initialisation of productRecyclerview
        //buyNowBtn.setOnClickListener(v ->{
            //for (int i = 0; i < quantity; i++){
                //CartManager.getInstance().addToCart(product);
            //}
            //startActivity(new Intent(this, CartActivity.class));
        //});

        relatedRecyclerView = findViewById(R.id.relatedRecyclerView);
        relatedRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Product> allProducts = ProductRepository.getInstance().getAllProducts();
        List<Product> relatedList = new ArrayList<>();

        for(Product p : allProducts){
            if (p.getCategory().equalsIgnoreCase(product.getCategory()) && p.getId() != product.getId()){
                relatedList.add(p);
            }
        }

        relatedAdapter = new ProductAdapter(this, relatedList, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product clickedProduct) {
                Intent intent = new Intent(ProductDetailActivity.this, ProductDetailActivity.class);
                intent.putExtra("product_id", clickedProduct.getId());
                intent.putExtra("product_name", clickedProduct.getName());
                intent.putExtra("product_price", clickedProduct.getPrice());
                intent.putExtra("product_image", clickedProduct.getImageUrl());
                intent.putExtra("product_description", clickedProduct.getDescription());
                intent.putExtra("product_category", clickedProduct.getCategory());
                startActivity(intent);
                finish();

            }

            @Override
            public void onAddToCartClick(Product p) {
              CartManager.getInstance().addToCart(p);
              Toast.makeText(ProductDetailActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });


        relatedRecyclerView.setAdapter(relatedAdapter);
        //ends here
    }//end of onCreate method

    //loadReviews() starts here
    private void loadReviews(){

        reviewRepository.getReviewsByProduct(
                String.valueOf(product.getId()),
                new Callback<List<Review>>() {

                    @Override
                    public void onResponse(Call<List<Review>> call,
                                           Response<List<Review>> response) {

                        if(response.isSuccessful() && response.body() != null){

                            List<Review> reviews = response.body();

                            Toast.makeText(
                                    ProductDetailActivity.this,
                                    "Reviews found: " + reviews.size(),
                                    Toast.LENGTH_LONG
                            ).show();


                            reviewAdapter = new ReviewAdapter(
                                    ProductDetailActivity.this,
                                    reviews
                            );

                            reviewsRecyclerView.setAdapter(reviewAdapter);

                        } else {

                            try {

                                Toast.makeText(
                                        ProductDetailActivity.this,
                                        "Error Code: " + response.code()
                                                + "\n" + response.errorBody().string(),
                                        Toast.LENGTH_LONG
                                ).show();

                            } catch (Exception e) {

                                Toast.makeText(
                                        ProductDetailActivity.this,
                                        "Unknown error",
                                        Toast.LENGTH_LONG
                                ).show();
                            }


                        }
                    }


                    @Override
                    public void onFailure(Call<List<Review>> call, Throwable t){

                        Toast.makeText(
                                ProductDetailActivity.this,
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    }

                }
        );
    }
    //loadReviews() ends here

    //showWriteDialog() starts here
    private void showWriteReviewDialog(){

        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_write_review, null);


        EditText reviewNameInput = view.findViewById(R.id.reviewNameInput);
        EditText reviewCommentInput = view.findViewById(R.id.reviewCommentInput);
        RatingBar reviewRatingBar = view.findViewById(R.id.reviewRatingBar);
        Button submitReviewBtn = view.findViewById(R.id.submitReviewBtn);


        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();


        submitReviewBtn.setOnClickListener(v -> {


            String name = reviewNameInput.getText()
                    .toString()
                    .trim();


            String comment = reviewCommentInput.getText()
                    .toString()
                    .trim();


            int rating = (int) reviewRatingBar.getRating();



            if(name.isEmpty() || comment.isEmpty()){

                Toast.makeText(
                        ProductDetailActivity.this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }



            Review review = new Review();

            review.setProductId(product.getId());
            review.setCustomerName(name);
            review.setRating(rating);
            review.setComment(comment);



            reviewRepository.addReview(review, new Callback<List<Review>>() {

                @Override
                public void onResponse(Call<List<Review>> call,
                                       Response<List<Review>> response) {


                    if(response.isSuccessful()){


                        Toast.makeText(
                                ProductDetailActivity.this,
                                "Review submitted successfully!",
                                Toast.LENGTH_SHORT
                        ).show();


                        dialog.dismiss();


                        // Refresh reviews after adding new one
                        loadReviews();


                    } else {


                        Toast.makeText(
                                ProductDetailActivity.this,
                                "Failed to submit review",
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                }


                @Override
                public void onFailure(Call<List<Review>> call,
                                      Throwable t) {


                    Toast.makeText(
                            ProductDetailActivity.this,
                            "Error: " + t.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();

                }
            });

        });


        dialog.show();

    }
    //ends here
}
