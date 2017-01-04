package com.example.ericdesedas.expohub.data.network;

import android.content.Context;

import com.example.ericdesedas.expohub.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiTokenRequestInterceptor implements Interceptor {

    private String apiToken;

    public ApiTokenRequestInterceptor(Context context) {
        this.apiToken = context.getString(R.string.api_key);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request().newBuilder()
                .addHeader("x-api-authorization", apiToken)
                .build();
        return chain.proceed(request);
    }
}
