package com.example.ericdesedas.expohub.unit.domain.interactors;

import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.AttendFairEventUseCase;
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

import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest( { Response.class, Moshi.class, ResponseBody.class, JsonAdapter.class })
public class AttendEventUseCaseTest extends BaseApiUseCaseTest {

    @Mock ApiUseCase.Listener<ResponseBody> listener;

    private AttendFairEventUseCase useCase;
    private ApiClientRequestCallback<ResponseBody, AttendFairEventUseCase> genericCallback;
    private String fairEventId = "1";

    @Before
    public void setUp() {

        super.setUp();

        useCase         = new AttendFairEventUseCase(apiClientMock, moshiMock);
        genericCallback = new ApiClientRequestCallback<ResponseBody, AttendFairEventUseCase>() {
            @Override
            public void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<ResponseBody> call) {
                when(apiClient.attendFairEvent(fairEventId)).thenReturn(call);
            }

            @Override
            public void triggerRequest(AttendFairEventUseCase apiUseCase) {
                apiUseCase.executeRequest(fairEventId);
            }

            @Override
            public void onProcessResponseData(Response<ResponseBody> response) {
                //
            }
        };
    }

    @Test
    public void it_successfully_attends_fair() {

        ResponseBody responseBody = PowerMockito.mock(ResponseBody.class);
        executeSuccessExpectations(useCase, listener, responseBody, genericCallback);
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
