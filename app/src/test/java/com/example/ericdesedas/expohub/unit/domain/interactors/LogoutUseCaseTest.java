package com.example.ericdesedas.expohub.unit.domain.interactors;

import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.LogoutUseCase;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest( { Response.class, Moshi.class, ResponseBody.class, JsonAdapter.class })
public class LogoutUseCaseTest extends BaseApiUseCaseTest {

    @Mock
    ApiUseCase.Listener<ResponseBody> listener;

    @Mock
    SessionManager sessionManager;

    private LogoutUseCase logoutUseCase;

    @Before
    public void setUp() {
        super.setUp();
        logoutUseCase = new LogoutUseCase(apiClientMock, moshiMock, sessionManager);
    }

    @Test
    public void it_successfully_logs_user_out() {

        final ResponseBody responseBody = PowerMockito.mock(ResponseBody.class);

        executeSuccessExpectations(logoutUseCase, listener, responseBody, new ApiClientRequestCallback<ResponseBody, LogoutUseCase>() {
            @Override
            public void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<ResponseBody> call) {
                when(apiClient.logout()).thenReturn(call);
            }

            @Override
            public void triggerRequest(LogoutUseCase apiUseCase) {
                apiUseCase.executeRequest();
            }

            @Override
            public void onProcessResponseData(Response<ResponseBody> response) {
                //
            }
        });

        verify(sessionManager).logout();
    }

    @Test
    public void it_propagates_error() throws IOException {

        executeErrorExpectations(logoutUseCase, listener, new ApiClientRequestCallback<ResponseBody, LogoutUseCase>() {
            @Override
            public void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<ResponseBody> call) {
                when(apiClient.logout()).thenReturn(call);
            }

            @Override
            public void triggerRequest(LogoutUseCase apiUseCase) {
                apiUseCase.executeRequest();
            }

            @Override
            public void onProcessResponseData(Response<ResponseBody> response) {
                //
            }
        });

        verifyZeroInteractions(sessionManager);
    }

    @Test
    public void it_propagates_failure_on_invalid_json() throws IOException {

        executeFailureExpectation(logoutUseCase, listener, true, new ApiClientRequestCallback<ResponseBody, LogoutUseCase>() {
            @Override
            public void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<ResponseBody> call) {
                when(apiClient.logout()).thenReturn(call);
            }

            @Override
            public void triggerRequest(LogoutUseCase apiUseCase) {
                apiUseCase.executeRequest();
            }

            @Override
            public void onProcessResponseData(Response<ResponseBody> response) {
                //
            }
        });

        verifyZeroInteractions(sessionManager);
    }

    @Test
    public void it_propagates_failure_on_library_error() throws  IOException {

        executeFailureExpectation(logoutUseCase, listener, false, new ApiClientRequestCallback<ResponseBody, LogoutUseCase>() {
            @Override
            public void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<ResponseBody> call) {
                when(apiClient.logout()).thenReturn(call);
            }

            @Override
            public void triggerRequest(LogoutUseCase apiUseCase) {
                apiUseCase.executeRequest();
            }

            @Override
            public void onProcessResponseData(Response<ResponseBody> response) {
                //
            }
        });

        verifyZeroInteractions(sessionManager);
    }
}
