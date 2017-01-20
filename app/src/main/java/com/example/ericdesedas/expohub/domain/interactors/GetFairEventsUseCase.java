package com.example.ericdesedas.expohub.domain.interactors;

import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

public class GetFairEventsUseCase extends ApiUseCase<FairEvent> {

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public GetFairEventsUseCase(ApiClient apiClient, Moshi moshi) {
        super(apiClient, moshi);
    }

    /**
     * Executes the request
     */
    public void executeRequest() {
        apiClient.getFairEvents(apiParameters).enqueue(callback);
    }
}
