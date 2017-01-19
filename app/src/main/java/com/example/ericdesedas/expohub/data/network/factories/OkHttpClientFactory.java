package com.example.ericdesedas.expohub.data.network.factories;

import com.example.ericdesedas.expohub.data.network.ApiTokenRequestInterceptor;
import com.example.ericdesedas.expohub.data.network.AuthRequestInterceptor;
import com.example.ericdesedas.expohub.helpers.preferences.NetworkPreferenceHelper;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

/**
 * Http client factory
 */
public class OkHttpClientFactory {

    private ApiTokenRequestInterceptor apiTokenRequestInterceptor;
    private AuthRequestInterceptor authRequestInterceptor;
    private OkHttpClient.Builder builder;
    private NetworkPreferenceHelper networkPreferenceHelper;

    @Inject
    public OkHttpClientFactory(ApiTokenRequestInterceptor apiTokenRequestInterceptor,
                               AuthRequestInterceptor authRequestInterceptor,
                               OkHttpClient.Builder builder,
                               NetworkPreferenceHelper networkPreferenceHelper) {

        this.apiTokenRequestInterceptor = apiTokenRequestInterceptor;
        this.authRequestInterceptor     = authRequestInterceptor;
        this.builder                    = builder;
        this.networkPreferenceHelper    = networkPreferenceHelper;
    }

    public OkHttpClient create() {

        return builder.addInterceptor(apiTokenRequestInterceptor)
                .addInterceptor(authRequestInterceptor)
                .readTimeout(networkPreferenceHelper.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(networkPreferenceHelper.getWriteTimeout(), TimeUnit.SECONDS)
                .build();
    }
}
