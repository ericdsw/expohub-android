package com.example.ericdesedas.expohub.domain.interactors;

import android.util.Log;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moe.banana.jsonapi2.Document;
import moe.banana.jsonapi2.ResourceIdentifier;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ApiUseCase<T> {

    protected ApiClient apiClient;
    protected Moshi moshi;
    protected Map<String, String> apiParameters;
    protected Callback<T> callback;

    private List<Listener<T>> listeners;

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public ApiUseCase(ApiClient apiClient, Moshi moshi) {

        // Internal Assignments
        this.moshi      = moshi;
        this.apiClient  = apiClient;

        // List Creations
        apiParameters   = new HashMap<>();
        listeners       = new ArrayList<>();

        // Callback Initialization
        callback = new Callback<T>() {

            @Override
            public void onResponse(Call<T> call, Response<T> response) {

                processResponseData(response);

                if (response.isSuccessful()) {
                    for (Listener<T> listener : listeners) {
                        listener.onResponse(response.code(), response.body());
                    }
                } else {
                    try {
                        ApiErrorWrapper apiErrorWrapper = ApiUseCase.this.moshi.adapter(ApiErrorWrapper.class)
                                .fromJson(response.errorBody().string());

                        for (Listener listener : listeners) {
                            listener.onError(response.code(), apiErrorWrapper);
                        }
                    } catch (IOException e) {
                        for (Listener listener : listeners) {
                            listener.onFailure(e);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e(ApiUseCase.class.getName(), t.getMessage());
                for (Listener listener : listeners) {
                    listener.onFailure(t);
                }
            }
        };
    }

    /**
     * Registers a new parameter for the api calls
     *
     * @param key   a {@link String} instance containing the parameter key
     * @param value a {@link String} instance containing the parameter value
     */
    public void addParameter(String key, String value) {
        apiParameters.put(key, value);
    }

    /**
     * Registers a set of parameters
     *
     * @param apiParameters a {@link Map<String, String>} instance containing the required parameters
     */
    public void addParameters(Map<String, String> apiParameters) {
        this.apiParameters = apiParameters;
    }

    /**
     * Registers a new listener to the array
     *
     * @param listener a {@link Listener<T>} reference containing the server's response
     */
    public void registerListener(Listener<T> listener) {
        listeners.add(listener);
    }

    /**
     * Unregisters specified listener to the array
     *
     * @param listener the {@link Listener<T>} reference to remove
     */
    public void unregisterListener(Listener<T> listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    /**
     * Performs additional processing on the response data
     *
     * @param response the {@link Response<T>}
     */
    public void processResponseData(Response<T> response) {
        // Empty, should be overwritten by children that want to process document data
    }

    public interface Listener<K> {
        void onResponse(int statusCode, K result);
        void onError(int statusCode, ApiErrorWrapper apiError);
        void onFailure(Throwable throwable);
    }
}
