package com.example.ericdesedas.expohub.domain.interactors;

import com.example.ericdesedas.expohub.data.models.News;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

public class GetNewsByFairUseCase extends ApiUseCase<News[]> {

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public GetNewsByFairUseCase(ApiClient apiClient, Moshi moshi) {
        super(apiClient, moshi);
    }

    /**
     * Executes the apiClient's request
     * @param fairId
     */
    public void executeRequest(String fairId) {
        apiClient.getNewsByFair(fairId, apiParameters).enqueue(callback);
    }
}
