package com.example.ericdesedas.expohub.unit.domain.interactors;

import com.example.ericdesedas.expohub.data.models.User;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleUserUseCase;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Map;

import moe.banana.jsonapi2.Document;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest( { Response.class, Moshi.class, ResponseBody.class, JsonAdapter.class })
public class GetSingleUserUseCaseTest extends BaseApiUseCaseTest {

    @Mock
    ApiUseCase.Listener<Document<User>> listener;

    private GetSingleUserUseCase useCase;
    private ApiClientRequestCallback<Document<User>, GetSingleUserUseCase> genericCallback;
    private String userId = "1";

    @Before
    public void setUp() {
        super.setUp();

        useCase         = new GetSingleUserUseCase(apiClientMock, moshiMock);
        genericCallback = new ApiClientRequestCallback<Document<User>, GetSingleUserUseCase>() {
            @Override
            public void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<Document<User>> call) {
                when(apiClient.getUser(userId, parameters)).thenReturn(call);
            }

            @Override
            public void triggerRequest(GetSingleUserUseCase apiUseCase) {
                apiUseCase.executeRequest(userId);
            }

            @Override
            public void onProcessResponseData(Response<Document<User>> response) {
                // Empty
            }
        };
    }

    @Test
    public void it_successfully_fetches_user() {

        Document<User> document = new Document<>();
        executeSuccessExpectations(useCase, listener, document, genericCallback);
    }

    @Test
    public void it_propagates_error() throws IOException {
        executeErrorExpectations(useCase, listener, genericCallback);
    }

    @Test
    public void it_propagates_failure_on_invalid_json() throws IOException {
        executeFailureExpectation(useCase, listener, true, genericCallback);
    }

    @Test
    public void it_propagates_failure_on_library_error() throws IOException {
        executeFailureExpectation(useCase, listener, false, genericCallback);
    }
}
