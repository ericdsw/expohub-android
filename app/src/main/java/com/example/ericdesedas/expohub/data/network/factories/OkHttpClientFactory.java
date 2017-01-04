package com.example.ericdesedas.expohub.data.network.factories;

import com.example.ericdesedas.expohub.data.network.ApiTokenRequestInterceptor;
import com.example.ericdesedas.expohub.helpers.preferences.NetworkPreferenceHelper;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

/**
 * Http client factory
 */
public class OkHttpClientFactory {

    ApiTokenRequestInterceptor apiTokenRequestInterceptor;
    OkHttpClient.Builder builder;
    NetworkPreferenceHelper networkPreferenceHelper;

    @Inject
    public OkHttpClientFactory(ApiTokenRequestInterceptor apiTokenRequestInterceptor,
                               OkHttpClient.Builder builder,
                               NetworkPreferenceHelper networkPreferenceHelper) {

        this.apiTokenRequestInterceptor = apiTokenRequestInterceptor;
        this.builder                    = builder;
        this.networkPreferenceHelper    = networkPreferenceHelper;
    }

    public OkHttpClient create() {

        return builder.addInterceptor(apiTokenRequestInterceptor)
                .readTimeout(networkPreferenceHelper.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(networkPreferenceHelper.getWriteTimeout(), TimeUnit.SECONDS)
                .build();
    }
}
