    package com.example.ericdesedas.expohub.buildsystem.modules;

import android.content.Context;

import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.data.network.ApiTokenRequestInterceptor;
import com.example.ericdesedas.expohub.data.network.AuthRequestInterceptor;
import com.example.ericdesedas.expohub.data.network.SessionManagerImpl;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.example.ericdesedas.expohub.data.network.factories.ApiClientFactory;
import com.example.ericdesedas.expohub.data.network.factories.MoshiFactory;
import com.example.ericdesedas.expohub.data.network.factories.OkHttpClientFactory;
import com.example.ericdesedas.expohub.data.network.factories.RetrofitFactory;
import com.example.ericdesedas.expohub.helpers.preferences.NetworkPreferenceHelper;
import com.example.ericdesedas.expohub.helpers.preferences.PreferenceHelper;
import com.squareup.moshi.Moshi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;

@Module
public class NetworkModule {

    private Context context;

    public NetworkModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Retrofit.Builder providesRetrofitBuilder() {
        return new Retrofit.Builder();
    }


    @Provides
    @Singleton
    Moshi.Builder providesMoshiBuilder() {
        return new Moshi.Builder();
    }

    @Provides
    @Singleton
    OkHttpClient.Builder providesOkHttpClientBuilder() {
        return new OkHttpClient.Builder();
    }

    @Provides
    @Singleton
    ApiTokenRequestInterceptor providesApiTokenRequestInterceptor() {
        return new ApiTokenRequestInterceptor(context);
    }

    @Provides
    @Singleton
    AuthRequestInterceptor providesAuthRequestInterceptor(SessionManager sessionManager) {
        return new AuthRequestInterceptor(sessionManager);
    }

    @Provides
    @Singleton
    NetworkPreferenceHelper providesNetworkPreferenceHelper() {
        return new NetworkPreferenceHelper(context);
    }

    @Provides
    @Singleton
    Moshi providesMoshi(Moshi.Builder moshiBuilder) {
        return moshiBuilder.build();
    }

    @Provides
    @Singleton
    Converter.Factory providesMoshiConverterFactory(MoshiFactory moshiFactory) {
        return moshiFactory.create();
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(OkHttpClientFactory okHttpClientFactory) {
        return okHttpClientFactory.create();
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(RetrofitFactory retrofitFactory) {
        return retrofitFactory.create();
    }

    @Provides
    @Singleton
    ApiClient providesApiClient(ApiClientFactory apiClientFactory) {
        return apiClientFactory.create();
    }

    @Provides
    @Singleton
    SessionManager providesSessionHelper(PreferenceHelper helper, Moshi moshi) {
        return new SessionManagerImpl(helper, moshi);
    }
}
