package com.mervygadgets.abigailclothingbrand.network;

import com.mervygadgets.abigailclothingbrand.models.LoginRequest;
import com.mervygadgets.abigailclothingbrand.models.Order;
import com.mervygadgets.abigailclothingbrand.models.Product;
import com.mervygadgets.abigailclothingbrand.models.UserRequest;
import com.mervygadgets.abigailclothingbrand.models.Review;

import java.util.List;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.DELETE;


public interface ApiService {

    // GET all products
    @GET("rest/v1/products")
    Call<List<Product>> getProducts();

    // GET products by category
    @GET("rest/v1/products")
    Call<List<Product>> getProductsByCategory(
            @Query("category") String category
    );

    // GET single product
    @GET("rest/v1/products")
    Call<List<Product>> getProductById(
            @Query("id") String id
    );

    @DELETE("rest/v1/products")
    Call<Void> deleteProduct(@Query("id") String idFilter);

    // GET all orders
    @GET("rest/v1/orders")
    Call<List<Order>> getOrders();

    // GET orders by phone
    @GET("rest/v1/orders")
    Call<List<Order>> getOrdersByPhone(
            @Query("customer_phone") String phone
    );

    // POST new order
    @POST("rest/v1/orders")
    Call<Void> createOrder(
            @Body List<Order> orders
    );

    @POST("rest/v1/products")
    @Headers({"Prefer: return = representation"})
    Call<List<Product>> addProduct(@Body Product product);


    @POST("auth/v1/signup")
    Call<ResponseBody> signUp(
            @Body UserRequest request
    );

    @POST("auth/v1/token?grant_type=password")
    Call<ResponseBody> login(
            @Body LoginRequest request
    );
    @POST("auth/v1/recover")
    Call<ResponseBody> resetPassword(@Body RequestBody emailRequest);

    // PATCH order status
    @PATCH("rest/v1/products")
    Call<Void> patchProduct(@Query("id") String id, @Body Product product);
    @PATCH("rest/v1/orders")
    Call<Void> updateOrderStatus(@Query("id") String id, @Body Map<String, String> body);

    //Reviews tab section
    @GET("rest/v1/reviews")
    Call<List<Review>>
    getReviewsByProduct(
            @Query("product_id")
            String productId
    );

    @POST("rest/v1/reviews")
    @Headers({"Prefers: return=representation"})
    Call<List<Review>>
    addReview(
            @Body Review review
    );

}