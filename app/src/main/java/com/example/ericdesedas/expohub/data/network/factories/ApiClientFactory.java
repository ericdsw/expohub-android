package com.example.ericdesedas.expohub.data.network.factories;

import com.example.ericdesedas.expohub.data.network.ApiClient;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class ApiClientFactory {

    private Retrofit retrofit;

    @Inject
    public ApiClientFactory(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public ApiClient create() {
        return retrofit.create(ApiClient.class);
    }
}
