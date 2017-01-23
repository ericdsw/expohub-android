package com.example.ericdesedas.expohub.domain.interactors;

import com.example.ericdesedas.expohub.data.models.ApiMeta;
import com.example.ericdesedas.expohub.data.models.User;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.squareup.moshi.Moshi;

import moe.banana.jsonapi2.Document;
import moe.banana.jsonapi2.JsonBuffer;
import retrofit2.Response;

public class RegisterUseCase extends ApiUseCase<Document<User>> {

    private SessionManager sessionManager;

    /**
     * Constructor
     *
     * @param apiClient         the {@link ApiClient} instance
     * @param moshi             the {@link Moshi} moshi instance
     * @param sessionManager    the {@link SessionManager} instance
     */
    public RegisterUseCase(ApiClient apiClient, Moshi moshi, SessionManager sessionManager) {
        super(apiClient, moshi);
        this.sessionManager = sessionManager;
    }

    /**
     * Executes the request
     *
     * @param name      a {@link String} instance with the provided name
     * @param username  a {@link String} instance with the provided username
     * @param email     a {@link String} instance with the provided email
     * @param password  a {@link String} instance with the provided password
     */
    public void executeRequest(String name, String username, String email, String password) {
        apiClient.register(name, username, email, password).enqueue(callback);
    }

    @Override
    public void processResponseData(Response<Document<User>> response) {

        super.processResponseData(response);

        if (response.isSuccessful()) {

            Document<User> document     = response.body();
            JsonBuffer<ApiMeta> buffer  = document.getMeta();

            User user       = document.get();
            ApiMeta apiMeta = buffer.get(new ApiMeta.Adapter());

            this.sessionManager.login(user, apiMeta.metaElements.get("token"));
        }
    }
}
