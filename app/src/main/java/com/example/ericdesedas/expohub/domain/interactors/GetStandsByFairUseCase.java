package com.example.ericdesedas.expohub.domain.interactors;

import com.example.ericdesedas.expohub.data.models.Stand;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

import moe.banana.jsonapi2.Document;

public class GetStandsByFairUseCase extends ApiUseCase<Document<Stand>> {

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public GetStandsByFairUseCase(ApiClient apiClient, Moshi moshi) {
        super(apiClient, moshi);
    }

    /**
     * Executes the request
     *
     * @param fairId the {@link String} reference containing the related fair's id
     */
    public void executeRequest(String fairId) {
        apiClient.getStandsByFair(fairId, apiParameters).enqueue(callback);
    }
}
