package com.example.ericdesedas.expohub.domain.interactors;

import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.squareup.moshi.Moshi;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class LogoutUseCase extends ApiUseCase<ResponseBody> {

    private SessionManager sessionManager;

    /**
     * Constructor
     *
     * @param apiClient         the {@link ApiClient} instance
     * @param moshi             the {@link Moshi} moshi instance
     * @param sessionManager    the {@link SessionManager} instance
     */
    public LogoutUseCase(ApiClient apiClient, Moshi moshi, SessionManager sessionManager) {
        super(apiClient, moshi);
        this.sessionManager = sessionManager;
    }

    public void executeRequest() {
        apiClient.logout().enqueue(callback);
    }

    @Override
    public void processResponseData(Response<ResponseBody> response) {

        super.processResponseData(response);

        if (response.isSuccessful()) {
            sessionManager.logout();
        }
    }
}
