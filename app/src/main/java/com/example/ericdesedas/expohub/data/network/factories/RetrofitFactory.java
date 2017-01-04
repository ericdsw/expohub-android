package com.example.ericdesedas.expohub.data.network.factories;

import com.example.ericdesedas.expohub.helpers.preferences.NetworkPreferenceHelper;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class RetrofitFactory {

    private Retrofit.Builder retrofitBuilder;
    private OkHttpClient okHttpClient;
    private Converter.Factory converterFactory;
    private NetworkPreferenceHelper networkPreferenceHelper;

    @Inject
    public RetrofitFactory(Retrofit.Builder retrofitBuilder,
                           OkHttpClient okHttpClient,
                           Converter.Factory converterFactory,
                           NetworkPreferenceHelper networkPreferenceHelper) {

        this.okHttpClient               = okHttpClient;
        this.retrofitBuilder            = retrofitBuilder;
        this.converterFactory           = converterFactory;
        this.networkPreferenceHelper    = networkPreferenceHelper;
    }

    public Retrofit create() {

        return retrofitBuilder.baseUrl(networkPreferenceHelper.getBaseURL())
                .addConverterFactory(converterFactory)
                .client(okHttpClient)
                .build();
    }
}
