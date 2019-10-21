package com.example.ericdesedas.expohub.domain.interactors;

import android.util.Log;

import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

import moe.banana.jsonapi2.Document;

public class GetFairsUseCase extends ApiUseCase<Document<Fair>> {

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public GetFairsUseCase(ApiClient apiClient, Moshi moshi) {
        super(apiClient, moshi);
    }

    /**
     * Fetches all fairs
     */
    public void executeRequest() {
        apiClient.getFairs(apiParameters).enqueue(callback);
    }
}
