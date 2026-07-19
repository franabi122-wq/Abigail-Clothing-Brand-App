package com.mervygadgets.abigailclothingbrand.network;

import android.content.Context;
import com.mervygadgets.abigailclothingbrand.data.TokenManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://qnemgylggyotcnzpctrj.supabase.co/";
    public static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFuZW1neWxnZ3lvdGNuenBjdHJqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzYxNzI5ODksImV4cCI6MjA5MTc0ODk4OX0.TtEeXvZCCMyZ6HumcuBiStBnvufmmlD-6EV6PN4GfCU";

    public static Retrofit getClient(Context context) {
        TokenManager tokenManager = new TokenManager(context);

        // Add logging to see network requests in Logcat
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Build the client fresh every time to ensure headers/tokens are updated
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new AuthInterceptor(tokenManager))
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("apikey", API_KEY)

                           // .addHeader("Authorization", "Bearer " + API_KEY)

                            .addHeader("Content-Type", "application/json")
                            .addHeader("Prefer", "return=representation")
                            .build();
                    return chain.proceed(request);
                })
                .build();

        // Return a fresh Retrofit instance
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getApiService(Context context) {
        return getClient(context).create(ApiService.class);
    }
}