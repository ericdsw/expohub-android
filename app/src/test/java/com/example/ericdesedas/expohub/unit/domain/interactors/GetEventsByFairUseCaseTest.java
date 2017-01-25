package com.example.ericdesedas.expohub.unit.domain.interactors;

import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetEventsByFairUseCase;
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

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest( { Response.class, Moshi.class, ResponseBody.class, JsonAdapter.class })
public class GetEventsByFairUseCaseTest extends BaseApiUseCaseTest {

    @Mock ApiUseCase.Listener<Document<FairEvent>> listener;

    private GetEventsByFairUseCase getEventsByFairUseCase;
    private ApiClientRequestCallback<Document<FairEvent>, GetEventsByFairUseCase> genericCallback;
    private String fairId = "1";

    @Before
    public void setUp() {
        super.setUp();
        getEventsByFairUseCase  = new GetEventsByFairUseCase(apiClientMock, moshiMock);
        genericCallback         = new ApiClientRequestCallback<Document<FairEvent>, GetEventsByFairUseCase>() {
            @Override
            public void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<Document<FairEvent>> call) {
                when(apiClient.getFairEventsByFair(fairId, parameters)).thenReturn(call);
            }

            @Override
            public void triggerRequest(GetEventsByFairUseCase apiUseCase) {
                apiUseCase.executeRequest(fairId);
            }

            @Override
            public void onProcessResponseData(Response<Document<FairEvent>> response) {
                // Empty
            }
        };
    }

    @Test
    public void it_successfully_fetches_fair_events_by_fair() {

        Document<FairEvent> document    = new Document<>();
        executeSuccessExpectations(getEventsByFairUseCase, listener, document, genericCallback);
    }

    @Test
    public void it_propagates_error() throws IOException {

        executeErrorExpectations(getEventsByFairUseCase, listener, genericCallback);
    }

    @Test
    public void it_propagates_error_on_invalid_json() throws IOException {

        executeFailureExpectation(getEventsByFairUseCase, listener, true, genericCallback);
    }

    @Test
    public void it_propagates_error_on_library_failure() throws IOException {

        executeFailureExpectation(getEventsByFairUseCase, listener, true, genericCallback);
    }
}
