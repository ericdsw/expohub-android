package com.example.ericdesedas.expohub.domain.interactors;


import com.example.ericdesedas.expohub.data.models.Comment;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

import moe.banana.jsonapi2.Document;

public class CreateCommentUseCase extends ApiUseCase<Document<Comment>> {

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public CreateCommentUseCase(ApiClient apiClient, Moshi moshi) {
        super(apiClient, moshi);
    }


    /**
     * Executes the request
     *
     * @param text      a {@link String} instance containing the comment's text
     * @param newsId    a {@link String} instance containing the related news' id
     */
    public void executeRequest(String text, String newsId) {
        apiClient.createComment(text, newsId).enqueue(callback);
    }
}
