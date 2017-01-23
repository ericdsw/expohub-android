package com.example.ericdesedas.expohub.unit.domain.interactors;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetEventsByFairUseCase;
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

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest( { Response.class, Moshi.class, ResponseBody.class, JsonAdapter.class })
public class GetEventsByFairUseCaseTest {

    @Mock
    ApiClient apiClientMock;

    @Mock
    Moshi moshiMock;

    @Mock
    JsonAdapter<ApiErrorWrapper> errorApiJsonAdapterMock;

    @Mock
    ApiUseCase.Listener<Document<FairEvent>> listener;

    private NetworkMockHelper networkMockHelper;
    private GetEventsByFairUseCase getEventsByFairUseCase;

    @Before
    public void setUp() {
        networkMockHelper       = new NetworkMockHelper();
        getEventsByFairUseCase  = new GetEventsByFairUseCase(apiClientMock, moshiMock);
    }

    @Test
    public void it_successfully_fetches_fair_events_by_fair() {

        String fairId                   = "1";
        Document<FairEvent> document    = new Document<>();
        int statusCode                  = 200;
        Map<String, String> parameters  = new HashMap<>();

        Call<Document<FairEvent>> call                                  = networkMockHelper.getCallMock();
        Response<Document<FairEvent>> response                          = networkMockHelper.getResponseMock();
        ArgumentCaptor<Callback<Document<FairEvent>>> argumentCaptor    = networkMockHelper.getResponseCallbackCaptor();

        when(apiClientMock.getFairEventsByFair(fairId, parameters)).thenReturn(call);

        when(response.body()).thenReturn(document);
        when(response.code()).thenReturn(statusCode);
        when(response.isSuccessful()).thenReturn(true);

        getEventsByFairUseCase.addParameters(parameters);
        getEventsByFairUseCase.registerListener(listener);
        getEventsByFairUseCase.executeRequest(fairId);

        verify(call).enqueue(argumentCaptor.capture());
        argumentCaptor.getValue().onResponse(call, response);

        verify(listener).onResponse(statusCode, document);
    }

    @Test
    public void it_propagates_error() throws IOException {

        String fairId                   = "1";
        String errorBody                = "foo";
        int statusCode                  = 400;
        Map<String, String> parameters  = new HashMap<>();
        ApiErrorWrapper apiErrorWrapper = new ApiErrorWrapper();
        ResponseBody responseBodyMock   = PowerMockito.mock(ResponseBody.class);

        Call<Document<FairEvent>> call                                  = networkMockHelper.getCallMock();
        Response<Document<FairEvent>> response                          = networkMockHelper.getResponseMock();
        ArgumentCaptor<Callback<Document<FairEvent>>> argumentCaptor    = networkMockHelper.getResponseCallbackCaptor();

        when(response.errorBody()).thenReturn(responseBodyMock);
        when(response.code()).thenReturn(statusCode);
        when(response.isSuccessful()).thenReturn(false);

        when(responseBodyMock.string()).thenReturn(errorBody);

        when(apiClientMock.getFairEventsByFair(fairId, parameters)).thenReturn(call);

        when(moshiMock.adapter(eq(ApiErrorWrapper.class))).thenReturn(errorApiJsonAdapterMock);

        when(errorApiJsonAdapterMock.fromJson(errorBody)).thenReturn(apiErrorWrapper);

        getEventsByFairUseCase.addParameters(parameters);
        getEventsByFairUseCase.registerListener(listener);
        getEventsByFairUseCase.executeRequest(fairId);

        verify(call).enqueue(argumentCaptor.capture());

        argumentCaptor.getValue().onResponse(call, response);

        verify(listener).onError(statusCode, apiErrorWrapper);
    }

    @Test
    public void it_propagates_error_on_invalid_json() throws IOException {

        String fairId                   = "1";
        String errorBody                = "foo";
        int statusCode                  = 400;
        Map<String, String> parameters  = new HashMap<>();
        ResponseBody responseBodyMock   = PowerMockito.mock(ResponseBody.class);
        IOException exception           = new IOException();

        Call<Document<FairEvent>> call                                  = networkMockHelper.getCallMock();
        Response<Document<FairEvent>> response                          = networkMockHelper.getResponseMock();
        ArgumentCaptor<Callback<Document<FairEvent>>> argumentCaptor    = networkMockHelper.getResponseCallbackCaptor();

        when(response.errorBody()).thenReturn(responseBodyMock);
        when(response.code()).thenReturn(statusCode);
        when(response.isSuccessful()).thenReturn(false);

        when(responseBodyMock.string()).thenReturn(errorBody);

        when(apiClientMock.getFairEventsByFair(fairId, parameters)).thenReturn(call);

        when(moshiMock.adapter(eq(ApiErrorWrapper.class))).thenReturn(errorApiJsonAdapterMock);

        when(errorApiJsonAdapterMock.fromJson(errorBody)).thenThrow(exception);

        getEventsByFairUseCase.addParameters(parameters);
        getEventsByFairUseCase.registerListener(listener);
        getEventsByFairUseCase.executeRequest(fairId);

        verify(call).enqueue(argumentCaptor.capture());

        argumentCaptor.getValue().onResponse(call, response);

        verify(listener).onFailure(exception);
    }

    @Test
    public void it_propagates_error_on_library_failure() throws IOException {

        String fairId                   = "1";
        Map<String, String> parameters  = new HashMap<>();
        IOException exception           = new IOException();

        Call<Document<FairEvent>> call                                  = networkMockHelper.getCallMock();
        ArgumentCaptor<Callback<Document<FairEvent>>> argumentCaptor    = networkMockHelper.getResponseCallbackCaptor();

        when(apiClientMock.getFairEventsByFair(fairId, parameters)).thenReturn(call);

        getEventsByFairUseCase.addParameters(parameters);
        getEventsByFairUseCase.registerListener(listener);
        getEventsByFairUseCase.executeRequest(fairId);

        verify(call).enqueue(argumentCaptor.capture());

        argumentCaptor.getValue().onFailure(call, exception);

        verify(listener).onFailure(exception);

    }
}
