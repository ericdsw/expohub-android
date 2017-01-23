package com.example.ericdesedas.expohub.unit.domain.interactors;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleFairUseCase;
import com.example.ericdesedas.expohub.helpers.NetworkMockHelper;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import moe.banana.jsonapi2.Document;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//TODO: Refactor with new implementation
@RunWith(PowerMockRunner.class)
@PrepareForTest( { Response.class, Moshi.class, ResponseBody.class, JsonAdapter.class })
public class GetSingleFairUseCaseTest {

    @Mock ApiClient apiClientMock;
    @Mock Moshi moshiMock;
    @Mock JsonAdapter<ApiErrorWrapper> errorApiJsonAdapterMock;
    @Mock ApiUseCase.Listener<Document<Fair>> listener;

    private NetworkMockHelper networkMockHelper;
    private GetSingleFairUseCase getSingleFairUseCase;

    @Before
    public void setUp() {
        networkMockHelper       = new NetworkMockHelper();
        getSingleFairUseCase    = new GetSingleFairUseCase(apiClientMock, moshiMock);
    }

    @Test
    public void it_successfully_fetches_single_fair() {

        String fairId                       = "1";
        Document<Fair> fair                 = new Document<>();
        int statusCode                      = 200;
        Map<String, String> apiParameters   = new HashMap<>();

        Call<Document<Fair>> call                                 = networkMockHelper.getCallMock();
        Response<Document<Fair>> response                         = networkMockHelper.getResponseMock();
        ArgumentCaptor<Callback<Document<Fair>>> argumentCaptor   = networkMockHelper.getResponseCallbackCaptor();

        when(apiClientMock.getFair(fairId, apiParameters)).thenReturn(call);

        when(response.body()).thenReturn(fair);
        when(response.code()).thenReturn(200);
        when(response.isSuccessful()).thenReturn(true);

        getSingleFairUseCase.addParameters(apiParameters);
        getSingleFairUseCase.registerListener(listener);
        getSingleFairUseCase.executeRequest(fairId);

        verify(call).enqueue(argumentCaptor.capture());
        argumentCaptor.getValue().onResponse(call, response);

        verify(listener).onResponse(statusCode, fair);
    }

    @Test
    public void it_propagates_error() throws IOException {

        String fairId                       = "1";
        String errorBodyString              = "foo";
        int statusCode                      = 400;
        Map<String, String> apiParameters   = new HashMap<>();
        ApiErrorWrapper apiErrorWrapper     = new ApiErrorWrapper();
        ResponseBody responseBodyMock       = PowerMockito.mock(ResponseBody.class);

        Call<Document<Fair>> call                                 = networkMockHelper.getCallMock();
        Response<Document<Fair>> response                         = networkMockHelper.getResponseMock();
        ArgumentCaptor<Callback<Document<Fair>>> argumentCaptor   = networkMockHelper.getResponseCallbackCaptor();

        when(response.code()).thenReturn(statusCode);
        when(response.errorBody()).thenReturn(responseBodyMock);

        when(responseBodyMock.string()).thenReturn(errorBodyString);

        when(apiClientMock.getFair(fairId, apiParameters)).thenReturn(call);

        when(moshiMock.adapter(eq(ApiErrorWrapper.class)))
                .thenReturn(errorApiJsonAdapterMock);

        when(errorApiJsonAdapterMock.fromJson(errorBodyString))
                .thenReturn(apiErrorWrapper);

        when(response.isSuccessful()).thenReturn(false);

        getSingleFairUseCase.addParameters(apiParameters);
        getSingleFairUseCase.registerListener(listener);
        getSingleFairUseCase.executeRequest(fairId);

        verify(call).enqueue(argumentCaptor.capture());
        argumentCaptor.getValue().onResponse(call, response);

        verify(listener).onError(statusCode, apiErrorWrapper);
    }

    @Test
    public void it_propagates_failure_on_invalid_json() throws IOException {

        String fairId                       = "1";
        String errorBodyString              = "foo";
        int statusCode                      = 400;
        Map<String, String> apiParameters   = new HashMap<>();
        ResponseBody responseBodyMock       = PowerMockito.mock(ResponseBody.class);

        IOException ioException = new IOException();

        Call<Document<Fair>> call                                 = networkMockHelper.getCallMock();
        Response<Document<Fair>> response                         = networkMockHelper.getResponseMock();
        ArgumentCaptor<Callback<Document<Fair>>> argumentCaptor   = networkMockHelper.getResponseCallbackCaptor();

        when(response.code()).thenReturn(statusCode);
        when(response.errorBody()).thenReturn(responseBodyMock);

        when(responseBodyMock.string()).thenReturn(errorBodyString);

        when(apiClientMock.getFair(fairId, apiParameters)).thenReturn(call);

        when(moshiMock.adapter(eq(ApiErrorWrapper.class)))
                .thenReturn(errorApiJsonAdapterMock);

        when(errorApiJsonAdapterMock.fromJson(errorBodyString))
                .thenThrow(ioException);

        when(response.isSuccessful()).thenReturn(false);

        getSingleFairUseCase.addParameters(apiParameters);
        getSingleFairUseCase.registerListener(listener);
        getSingleFairUseCase.executeRequest(fairId);

        verify(call).enqueue(argumentCaptor.capture());
        argumentCaptor.getValue().onResponse(call, response);

        verify(listener).onFailure(ioException);
    }

    @Test
    public void it_propagates_error_on_library_failure() {

        String fairId                       = "1";
        Map<String, String> apiParameters   = new HashMap<>();
        IOException exception               = new IOException();

        Call<Document<Fair>> call                               = networkMockHelper.getCallMock();
        ArgumentCaptor<Callback<Document<Fair>>> argumentCaptor = networkMockHelper.getResponseCallbackCaptor();

        when(apiClientMock.getFair(fairId, apiParameters)).thenReturn(call);

        getSingleFairUseCase.addParameters(apiParameters);
        getSingleFairUseCase.registerListener(listener);
        getSingleFairUseCase.executeRequest(fairId);

        verify(call).enqueue(argumentCaptor.capture());
        argumentCaptor.getValue().onFailure(call, exception);

        verify(listener).onFailure(exception);
    }
}
