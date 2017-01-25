package com.example.ericdesedas.expohub.domain.interactors;

import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

import okhttp3.ResponseBody;

public class UnAttendFairEventUseCase extends ApiUseCase<ResponseBody> {

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public UnAttendFairEventUseCase(ApiClient apiClient, Moshi moshi) {
        super(apiClient, moshi);
    }

    public void executeRequest(String fairEventId) {
        apiClient.unAttendFairEvent(fairEventId).enqueue(callback);
    }
}
