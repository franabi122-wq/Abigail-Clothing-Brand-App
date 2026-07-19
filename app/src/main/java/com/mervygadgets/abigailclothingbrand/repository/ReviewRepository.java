package com.mervygadgets.abigailclothingbrand.repository;

import android.content.Context;

import com.mervygadgets.abigailclothingbrand.models.Review;
import com.mervygadgets.abigailclothingbrand.network.ApiClient;
import com.mervygadgets.abigailclothingbrand.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewRepository {

    private ApiService apiService;

    public ReviewRepository(Context context) {
        apiService = ApiClient.getApiService(context);
    }


    // Get reviews for a product
    public void getReviewsByProduct(String productId, Callback<List<Review>> callback) {

        apiService.getReviewsByProduct("eq." + productId)
                .enqueue(callback);
    }


    // Add customer review
    public void addReview(Review review, Callback<List<Review>> callback) {

        apiService.addReview(review)
                .enqueue(callback);
    }
}