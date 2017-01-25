package com.example.ericdesedas.expohub.domain.interactors;

import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

import moe.banana.jsonapi2.Document;

public class GetSingleFairEventUseCase extends ApiUseCase<Document<FairEvent>> {

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public GetSingleFairEventUseCase(ApiClient apiClient, Moshi moshi) {
        super(apiClient, moshi);
    }

    /**
     * Executes the request
     *
     * @param id the {@link String} reference containing the fairEvent's id
     */
    public void executeRequest(String id) {
        apiClient.getFairEvent(id, apiParameters).enqueue(callback);
    }
}
