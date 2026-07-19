package com.mervygadgets.abigailclothingbrand.network;

import com.mervygadgets.abigailclothingbrand.data.TokenManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor{
private TokenManager tokenManager;

public AuthInterceptor(TokenManager tokenManager){
    this.tokenManager = tokenManager;
}

@Override
    public Response intercept(Chain chain) throws IOException{
    Request.Builder builder = chain.request().newBuilder();
    String token = tokenManager.getAccessToken();

    if(token != null){
        builder.addHeader("Authorization", "Bearer "+ token);
    }

    return chain.proceed(builder.build());
}
}
