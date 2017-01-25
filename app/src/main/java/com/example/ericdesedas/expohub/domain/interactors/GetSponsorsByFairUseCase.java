package com.example.ericdesedas.expohub.domain.interactors;

import com.example.ericdesedas.expohub.data.models.Sponsor;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

import moe.banana.jsonapi2.Document;

public class GetSponsorsByFairUseCase extends ApiUseCase<Document<Sponsor>> {

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public GetSponsorsByFairUseCase(ApiClient apiClient, Moshi moshi) {
        super(apiClient, moshi);
    }

    /**
     * Executes the request
     *
     * @param fairId the {@link String} containing the fair's id
     */
    public void executeRequest(String fairId) {
        apiClient.getSponsorsByFair(fairId, apiParameters).enqueue(callback);
    }
}
