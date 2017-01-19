package com.example.ericdesedas.expohub.domain.interactors;

import android.util.Log;

import com.example.ericdesedas.expohub.data.models.ApiMeta;
import com.example.ericdesedas.expohub.data.models.User;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.squareup.moshi.Moshi;

import moe.banana.jsonapi2.Document;
import moe.banana.jsonapi2.JsonBuffer;

public class LoginUseCase extends ApiUseCase<User> {

    private SessionManager sessionManager;

    /**
     * Constructor
     *
     * @param apiClient         the {@link ApiClient} instance
     * @param moshi             the {@link Moshi} moshi instance
     * @param sessionManager    the {@link SessionManager} instance
     */
    public LoginUseCase(ApiClient apiClient, Moshi moshi, SessionManager sessionManager) {
        super(apiClient, moshi);
        this.sessionManager = sessionManager;
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
    public void processResponseData(Document<User> document) {

        super.processResponseData(document);

        JsonBuffer<ApiMeta> buffer = (JsonBuffer<ApiMeta>) document.getMeta();

        User user       = document.get();
        ApiMeta apiMeta = buffer.get(new ApiMeta.Adapter());

        Log.d("LoginUseCase", "got this token: " + apiMeta.metaElements.get("token"));

        this.sessionManager.login(user, apiMeta.metaElements.get("token"));
    }
}
