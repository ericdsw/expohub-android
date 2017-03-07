package com.example.ericdesedas.expohub.domain.interactors;


import com.example.ericdesedas.expohub.data.models.News;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

import moe.banana.jsonapi2.Document;

public class GetSingleNewsUseCase extends ApiUseCase<Document<News>> {

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public GetSingleNewsUseCase(ApiClient apiClient, Moshi moshi) {
        super(apiClient, moshi);
    }

    public void executeRequest(String newsId) {
        apiClient.getNewsById(newsId, apiParameters).enqueue(callback);
    }
}
