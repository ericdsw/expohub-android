package com.example.ericdesedas.expohub.domain.interactors;


import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

import okhttp3.ResponseBody;

public class AttendFairEventUseCase extends ApiUseCase<ResponseBody> {

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public AttendFairEventUseCase(ApiClient apiClient, Moshi moshi) {
        super(apiClient, moshi);
    }

    /**
     * Executes the request
     *
     * @param fairEventId the {@link String} reference with the fairEvent's id
     */
    public void executeRequest(String fairEventId) {
        apiClient.attendFairEvent(fairEventId).enqueue(callback);
    }
}
