package com.example.ericdesedas.expohub.domain.interactors;

import com.example.ericdesedas.expohub.data.models.User;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

public class GetSingleUserUseCase extends ApiUseCase<User> {

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public GetSingleUserUseCase(ApiClient apiClient, Moshi moshi) {
        super(apiClient, moshi);
    }

    /**
     * Executes the request
     *
     * @param userId a {@link String} containing the user's id
     */
    public void executeRequest(String userId) {
        apiClient.getUser(userId).enqueue(callback);
    }
}
