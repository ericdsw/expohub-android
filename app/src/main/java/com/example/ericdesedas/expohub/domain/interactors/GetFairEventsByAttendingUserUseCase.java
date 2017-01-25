package com.example.ericdesedas.expohub.domain.interactors;

import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

import moe.banana.jsonapi2.Document;

public class GetFairEventsByAttendingUserUseCase extends ApiUseCase<Document<FairEvent>> {

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public GetFairEventsByAttendingUserUseCase(ApiClient apiClient, Moshi moshi) {
        super(apiClient, moshi);
    }

    /**
     * Executes the request
     *
     * @param userId a {@link String} containing the attending user's id
     */
    public void executeRequest(String userId) {
        apiClient.getFairEventsByAttendingUser(userId, apiParameters).enqueue(callback);
    }
}
