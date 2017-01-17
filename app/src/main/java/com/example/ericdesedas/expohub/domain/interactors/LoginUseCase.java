package com.example.ericdesedas.expohub.domain.interactors;

import com.example.ericdesedas.expohub.data.models.User;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.squareup.moshi.Moshi;

import moe.banana.jsonapi2.Document;
import moe.banana.jsonapi2.JsonBuffer;

public class LoginUseCase extends ApiUseCase<User> {

    /**
     * Constructor
     *
     * @param apiClient the {@link ApiClient} instance
     * @param moshi     the {@link Moshi} moshi instance
     */
    public LoginUseCase(ApiClient apiClient, Moshi moshi) {
        super(apiClient, moshi);
    }

    /**
     * Executes the request
     *
     * @param loginParam    a {@link String} instance containing the loginParam (either email or username) field
     * @param password      a {@link String} instance containing the password field
     */
    public void executeRequest(String loginParam, String password) {
        apiClient.login(loginParam, password).enqueue(callback);
    }

    @Override
    public void processResponseData(Document document) {

        super.processResponseData(document);
        JsonBuffer meta = document.getMeta();
    }
}
