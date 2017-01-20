package com.example.ericdesedas.expohub.domain.interactors;

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

public abstract class ApiUseCase<T extends ResourceIdentifier> {

    protected ApiClient apiClient;
    protected Moshi moshi;
    protected Map<String, String> apiParameters;
    protected Callback<Document<T>> callback;

    private List<Listener<Document<T>>> listeners;

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
        callback = new Callback<Document<T>>() {

            @Override
            public void onResponse(Call<Document<T>> call, Response<Document<T>> response) {

                processResponseData(response);

                if (response.isSuccessful()) {
                    for (Listener<Document<T>> listener : listeners) {
                        listener.onResponse(response.code(), response.body());
                    }
                } else {
                    try {
                        Document<T> document = ApiUseCase.this.moshi.adapter(Document.class)
                                .fromJson(response.errorBody().source());

                        ApiErrorWrapper apiErrorWrapper = new ApiErrorWrapper();
                        apiErrorWrapper.setErrorList(document.errors());

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
            public void onFailure(Call<Document<T>> call, Throwable t) {
                for (Listener listener : listeners) {
                    listener.onFailure(t);
                }
            }
        };
    }

    public void addParameter(String key, String value) {
        apiParameters.put(key, value);
    }

    public void registerListener(Listener<Document<T>> listener) {
        listeners.add(listener);
    }

    public void unregisterListener(Listener<Document<T>> listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void processResponseData(Response<Document<T>> response) {
        // Empty, should be overwritten by children that want to process document data
    }

    public interface Listener<K> {
        void onResponse(int statusCode, K result);
        void onError(int statusCode, ApiErrorWrapper apiError);
        void onFailure(Throwable throwable);
    }
}
