package com.example.ericdesedas.expohub.unit.domain.interactors;

import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairEventsByAttendingUserUseCase;
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
public class GetFairEventsByAttendingUserUseCaseTest extends BaseApiUseCaseTest {

    @Mock ApiUseCase.Listener<Document<FairEvent>> listener;

    private GetFairEventsByAttendingUserUseCase useCase;

    @Before
    public void setUp() {
        super.setUp();
        useCase = new GetFairEventsByAttendingUserUseCase(apiClientMock, moshiMock);
    }

    @Test
    public void it_successfully_fetches_fair_events_by_attending_user() {

        final String userId                   = "1";
        final Document<FairEvent> document    = new Document<>();

        executeSuccessExpectations(
                useCase, listener, document,
                new ApiClientRequestCallback<Document<FairEvent>, GetFairEventsByAttendingUserUseCase>() {
                    @Override
                    public void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<Document<FairEvent>> call) {
                        when(apiClient.getFairEventsByAttendingUser(userId, parameters)).thenReturn(call);
                    }

                    @Override
                    public void triggerRequest(GetFairEventsByAttendingUserUseCase apiUseCase) {
                        apiUseCase.executeRequest(userId);
                    }

                    @Override
                    public void onProcessResponseData(Response<Document<FairEvent>> response) {
                        // Empty
                    }
                }
        );
    }

    @Test
    public void it_propagates_error() throws IOException {

        final String userId = "1";

        executeErrorExpectations(
                useCase, listener,
                new ApiClientRequestCallback<Document<FairEvent>, GetFairEventsByAttendingUserUseCase>() {
                    @Override
                    public void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<Document<FairEvent>> call) {
                        when(apiClient.getFairEventsByAttendingUser(userId, parameters)).thenReturn(call);
                    }

                    @Override
                    public void triggerRequest(GetFairEventsByAttendingUserUseCase apiUseCase) {
                        apiUseCase.executeRequest(userId);
                    }

                    @Override
                    public void onProcessResponseData(Response<Document<FairEvent>> response) {
                        // Empty
                    }
                }
        );
    }

    @Test
    public void it_propagates_failure_by_invalid_json() throws IOException {

        final String userId = "1";

        executeFailureExpectation(
                useCase, listener, true,
                new ApiClientRequestCallback<Document<FairEvent>, GetFairEventsByAttendingUserUseCase>() {
                    @Override
                    public void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<Document<FairEvent>> call) {
                        when(apiClient.getFairEventsByAttendingUser(userId, parameters)).thenReturn(call);
                    }

                    @Override
                    public void triggerRequest(GetFairEventsByAttendingUserUseCase apiUseCase) {
                        apiUseCase.executeRequest(userId);
                    }

                    @Override
                    public void onProcessResponseData(Response<Document<FairEvent>> response) {
                        // Empty
                    }
                }
        );
    }

    @Test
    public void it_propagates_failure_by_library_error() throws IOException {

        final String userId = "1";

        executeFailureExpectation(
                useCase, listener, false,
                new ApiClientRequestCallback<Document<FairEvent>, GetFairEventsByAttendingUserUseCase>() {
                    @Override
                    public void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<Document<FairEvent>> call) {
                        when(apiClient.getFairEventsByAttendingUser(userId, parameters)).thenReturn(call);
                    }

                    @Override
                    public void triggerRequest(GetFairEventsByAttendingUserUseCase apiUseCase) {
                        apiUseCase.executeRequest(userId);
                    }

                    @Override
                    public void onProcessResponseData(Response<Document<FairEvent>> response) {
                        // Empty
                    }
                }
        );
    }
}
