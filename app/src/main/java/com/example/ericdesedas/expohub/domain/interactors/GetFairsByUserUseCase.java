package com.example.ericdesedas.expohub.domain.interactors;

import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

public class GetFairsByUserUseCase extends ApiUseCase<Fair> {

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public GetFairsByUserUseCase(ApiClient apiClient, Moshi moshi) {
        super(apiClient, moshi);
    }

    /**
     * Executes the request
     * @param userId a {@link String} containing the related user's id
     */
    public void executeRequest(String userId) {
        apiClient.getFairsByUser(userId, apiParameters).enqueue(callback);
    }
}
