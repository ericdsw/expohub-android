package com.example.ericdesedas.expohub.unit.domain.interactors;

import com.example.ericdesedas.expohub.data.models.ApiMeta;
import com.example.ericdesedas.expohub.data.models.User;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.LoginUseCase;
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

import moe.banana.jsonapi2.Document;
import moe.banana.jsonapi2.JsonBuffer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest( { Response.class, Moshi.class, ResponseBody.class, JsonAdapter.class })
public class LoginUseCaseTest extends BaseApiUseCaseTest {

    @Mock ApiUseCase.Listener<Document<User>> listener;
    @Mock SessionManager sessionManager;

    private LoginUseCase loginUseCase;

    @Before
    public void setUp() {
        super.setUp();
        loginUseCase = new LoginUseCase(apiClientMock, moshiMock, sessionManager);
    }

    @Test
    public void it_successfully_identifies_user() {

        final String loginParam     = "foo";
        final String password       = "bar";
        final User user             = new User();

        final Document<User> document = new Document<>();
        document.set(user);

        executeSuccessExpectations(loginUseCase, listener, document, new ApiClientRequestCallback<Document<User>, LoginUseCase>() {
            @Override
            public void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<Document<User>> call) {
                when(apiClient.login(loginParam, password)).thenReturn(call);
            }

            @Override
            public void triggerRequest(LoginUseCase apiUseCase) {
                apiUseCase.executeRequest(loginParam, password);
            }

            @Override
            public void onProcessResponseData(Response<Document<User>> response) {

                ApiMeta apiMeta = new ApiMeta();
                apiMeta.metaElements.put("token", "foo");

                JsonBuffer<ApiMeta> bufferMock = PowerMockito.mock(JsonBuffer.class);
                document.setMeta(bufferMock);

                when(response.body()).thenReturn(document);
                when(bufferMock.get(any(ApiMeta.Adapter.class))).thenReturn(apiMeta);
            }
        });

        verify(sessionManager).login(user, "foo");
    }

    @Test
    public void it_propagates_error() throws IOException {

        final String loginParam     = "foo";
        final String password       = "bar";
        final User user             = new User();

        final Document<User> document = new Document<>();
        document.set(user);

        executeErrorExpectations(loginUseCase, listener, new ApiClientRequestCallback<Document<User>, LoginUseCase>() {
            @Override
            public void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<Document<User>> call) {
                when(apiClient.login(loginParam, password)).thenReturn(call);
            }

            @Override
            public void triggerRequest(LoginUseCase apiUseCase) {
                apiUseCase.executeRequest(loginParam, password);
            }

            @Override
            public void onProcessResponseData(Response<Document<User>> response) {
                //
            }
        });

        verifyZeroInteractions(sessionManager);
    }

    @Test
    public void it_propagates_failure_on_invalid_json() throws IOException {

        final String loginParam     = "foo";
        final String password       = "bar";
        final User user             = new User();

        final Document<User> document = new Document<>();
        document.set(user);

        executeFailureExpectation(loginUseCase, listener, true, new ApiClientRequestCallback<Document<User>, LoginUseCase>() {
            @Override
            public void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<Document<User>> call) {
                when(apiClient.login(loginParam, password)).thenReturn(call);
            }

            @Override
            public void triggerRequest(LoginUseCase apiUseCase) {
                apiUseCase.executeRequest(loginParam, password);
            }

            @Override
            public void onProcessResponseData(Response<Document<User>> response) {
                //
            }
        });

        verifyZeroInteractions(sessionManager);
    }

    @Test
    public void it_propagates_failure_on_library_error() throws IOException {

        final String loginParam     = "foo";
        final String password       = "bar";
        final User user             = new User();

        final Document<User> document = new Document<>();
        document.set(user);

        executeFailureExpectation(loginUseCase, listener, false, new ApiClientRequestCallback<Document<User>, LoginUseCase>() {
            @Override
            public void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<Document<User>> call) {
                when(apiClient.login(loginParam, password)).thenReturn(call);
            }

            @Override
            public void triggerRequest(LoginUseCase apiUseCase) {
                apiUseCase.executeRequest(loginParam, password);
            }

            @Override
            public void onProcessResponseData(Response<Document<User>> response) {
                //
            }
        });

        verifyZeroInteractions(sessionManager);
    }
}
