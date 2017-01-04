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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ApiUseCase<T> {

    protected ApiClient apiClient;
    protected Moshi moshi;
    protected Map<String, String> apiParameters;
    protected List<Listener<T>> listeners;

    protected Callback<T> callback;

    /**
     * Constructor
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
                if (response.isSuccessful()) {
                    for (Listener<T> listener : listeners) {
                        listener.onResponse(response.code(), response.body());
                    }
                } else {
                    try {
                        ApiErrorWrapper apiErrorWrapper = ApiUseCase.this.moshi
                                .adapter(ApiErrorWrapper.class)
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
                for (Listener listener : listeners) {
                    listener.onFailure(t);
                }
            }
        };
    }

    /**
     * Registers a new query string parameter to the request
     *
     * @param key   the parameter's key
     * @param value the parameter's value
     */
    public void addParameter(String key, String value) {
        apiParameters.put(key, value);
    }

    /**
     * Overwrites existing parameter list with a new parameter set
     *
     * @param parameters a {@link Map} instance containing all the new parameters
     */
    public void addParameters(Map<String, String> parameters) {
        apiParameters.putAll(parameters);
    }

    /**
     * Registers a new listener for the use case
     *
     * @param listener the {@link Listener} reference to register
     */
    public void registerListener(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Removes specified listener from the listeners list
     *
     * @param listener the {@link Listener} to be removed
     */
    public void unregisterListener(Listener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    /**
     * This interface defines a common logic to handle all ApiUseCase possible responses
     * @param <K> The type defined for the specified ApiUseCase
     */
    public interface Listener<K> {
        void onResponse(int statusCode, K result);
        void onError(int statusCode, ApiErrorWrapper apiError);
        void onFailure(Throwable throwable);
    }
}
