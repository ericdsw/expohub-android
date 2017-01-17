package com.example.ericdesedas.expohub.domain.interactors;

import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

public class GetSingleFairUseCase extends ApiUseCase<Fair> {

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public GetSingleFairUseCase(ApiClient apiClient, Moshi moshi) {
        super(apiClient, moshi);
    }

    /**
     * Fetches a single fair
     *
     * @param id a {@link String} instance containing the desired fair id
     */
    public void executeRequest(String id) {
        apiClient.getFair(id, apiParameters).enqueue(callback);
    }
}
