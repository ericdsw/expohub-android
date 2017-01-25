package com.example.ericdesedas.expohub.unit.domain.interactors;

import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetEventsByFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairsByUserUseCase;
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
public class GetFairsByUserUseCaseTest extends BaseApiUseCaseTest {

    @Mock
    ApiUseCase.Listener<Document<Fair>> listener;

    private GetFairsByUserUseCase useCase;
    private ApiClientRequestCallback<Document<Fair>, GetFairsByUserUseCase> genericCallback;
    private String userId = "1";

    @Before
    public void setUp() {
        super.setUp();

        useCase         = new GetFairsByUserUseCase(apiClientMock, moshiMock);
        genericCallback = new ApiClientRequestCallback<Document<Fair>, GetFairsByUserUseCase>() {
            @Override
            public void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<Document<Fair>> call) {
                when(apiClient.getFairsByUser(userId, parameters)).thenReturn(call);
            }

            @Override
            public void triggerRequest(GetFairsByUserUseCase apiUseCase) {
                apiUseCase.executeRequest(userId);
            }

            @Override
            public void onProcessResponseData(Response<Document<Fair>> response) {
                //
            }
        };
    }

    @Test
    public void it_successfully_fetches_fairs_by_user() {

        Document<Fair> document = new Document<>();
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