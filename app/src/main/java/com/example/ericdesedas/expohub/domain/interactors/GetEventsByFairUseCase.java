package com.example.ericdesedas.expohub.domain.interactors;

import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

public class GetEventsByFairUseCase extends ApiUseCase<FairEvent> {

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public GetEventsByFairUseCase(ApiClient apiClient, Moshi moshi) {
        super(apiClient, moshi);
    }

    /**
     * Executes the apiClient's request
     *
     * @param fairId the {@link String} reference containing the relate fair's id
     */
    public void executeRequest(String fairId) {
        apiClient.getFairEventsByFair(fairId, apiParameters).enqueue(callback);
    }
}
