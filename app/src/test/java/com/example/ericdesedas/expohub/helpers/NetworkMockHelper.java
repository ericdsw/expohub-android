package com.example.ericdesedas.expohub.helpers;

import android.support.annotation.NonNull;

import org.mockito.ArgumentCaptor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.Mockito.mock;

public class NetworkMockHelper {

    @NonNull
    public <T> ArgumentCaptor<Callback<T>> getResponseCallbackCaptor() {
        Class<Callback<T>> callbackClass = (Class<Callback<T>>) (Class) Callback.class;
        return ArgumentCaptor.forClass(callbackClass);
    }

    public <T> Response<T> getResponseMock() {
        return (Response<T>) mock(Response.class);
    }

    public <T> Call<T> getCallMock() {
        return (Call<T>) mock(Call.class);
    }

}
