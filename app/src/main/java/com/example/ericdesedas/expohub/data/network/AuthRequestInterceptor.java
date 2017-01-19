package com.example.ericdesedas.expohub.data.network;

import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthRequestInterceptor implements Interceptor {

    private SessionManager sessionManager;

    public AuthRequestInterceptor(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        if (sessionManager.isLoggedIn()) {
            try {
                Request request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + sessionManager.getLoggedUser().token)
                        .build();
                return chain.proceed(request);
            } catch (IOException e) {
                //
            }
        }

        return chain.proceed(chain.request());
    }
}
