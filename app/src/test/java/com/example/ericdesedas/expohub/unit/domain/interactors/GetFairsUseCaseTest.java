package com.example.ericdesedas.expohub.unit.domain.interactors;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairsUseCase;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest( { Response.class, Moshi.class, ResponseBody.class, JsonAdapter.class })
public class GetFairsUseCaseTest {

    @Mock
    ApiClient apiClientMock;

    @Mock
    Moshi moshiMock;

    @Mock
    JsonAdapter<ApiErrorWrapper> errorApiJsonAdapterMock;

    @Mock
    ApiUseCase.Listener<Fair[]> listener;

    private NetworkMockHelper networkMockHelper;
    private GetFairsUseCase getFairsUseCase;

    @Before
    public void setUp() {
        networkMockHelper   = new NetworkMockHelper();
        getFairsUseCase     = new GetFairsUseCase(apiClientMock, moshiMock);
    }

    @Test
    public void it_successfully_fetches_fairs() {

        Fair[] fairs                        = new Fair[] {};
        int statusCode                      = 200;
        Map<String, String> apiParameters   = new HashMap<>();

        Call<Fair[]> call                               = networkMockHelper.getCallMock();
        Response<Fair[]> response                       = networkMockHelper.getResponseMock();
        ArgumentCaptor<Callback<Fair[]>> argumentCaptor = networkMockHelper.getResponseCallbackCaptor();

        when(apiClientMock.getFairs(apiParameters)).thenReturn(call);

        when(response.body()).thenReturn(fairs);
        when(response.code()).thenReturn(statusCode);
        when(response.isSuccessful()).thenReturn(true);

        getFairsUseCase.addParameters(apiParameters);
        getFairsUseCase.registerListener(listener);
        getFairsUseCase.executeRequest();

        verify(call).enqueue(argumentCaptor.capture());
        argumentCaptor.getValue().onResponse(call, response);

        verify(listener).onResponse(statusCode, fairs);
    }

    @Test
    public void it_propagates_errors() throws IOException {

        String errorBodyString              = "foo";
        int statusCode                      = 400;
        Map<String, String> apiParameters   = new HashMap<>();
        ApiErrorWrapper apiErrorWrapper     = new ApiErrorWrapper();
        ResponseBody responseBodyMock       = PowerMockito.mock(ResponseBody.class);

        Call<Fair[]> call                               = networkMockHelper.getCallMock();
        Response<Fair[]> response                       = networkMockHelper.getResponseMock();
        ArgumentCaptor<Callback<Fair[]>> argumentCaptor = networkMockHelper.getResponseCallbackCaptor();

        when(response.code()).thenReturn(statusCode);
        when(response.errorBody()).thenReturn(responseBodyMock);

        when(responseBodyMock.string()).thenReturn(errorBodyString);

        when(apiClientMock.getFairs(apiParameters)).thenReturn(call);

        when(moshiMock.adapter(eq(ApiErrorWrapper.class)))
                .thenReturn(errorApiJsonAdapterMock);

        when(errorApiJsonAdapterMock.fromJson(errorBodyString))
                .thenReturn(apiErrorWrapper);

        when(response.isSuccessful()).thenReturn(false);

        getFairsUseCase.addParameters(apiParameters);
        getFairsUseCase.registerListener(listener);
        getFairsUseCase.executeRequest();

        verify(call).enqueue(argumentCaptor.capture());
        argumentCaptor.getValue().onResponse(call, response);

        verify(listener).onError(statusCode, apiErrorWrapper);
    }

    @Test
    public void it_propagates_failure_on_invalid_json() throws IOException {

        String errorBodyString              = "foo";
        int statusCode                      = 400;
        Map<String, String> apiParameters   = new HashMap<>();
        ResponseBody responseBodyMock       = PowerMockito.mock(ResponseBody.class);

        IOException exception               = new IOException();

        Call<Fair[]> call                               = networkMockHelper.getCallMock();
        Response<Fair[]> response                       = networkMockHelper.getResponseMock();
        ArgumentCaptor<Callback<Fair[]>> argumentCaptor = networkMockHelper.getResponseCallbackCaptor();

        when(response.code()).thenReturn(statusCode);
        when(response.errorBody()).thenReturn(responseBodyMock);

        when(responseBodyMock.string()).thenReturn(errorBodyString);

        when(apiClientMock.getFairs(apiParameters)).thenReturn(call);

        when(moshiMock.adapter(eq(ApiErrorWrapper.class)))
                .thenReturn(errorApiJsonAdapterMock);

        when(errorApiJsonAdapterMock.fromJson(errorBodyString))
                .thenThrow(exception);

        when(response.isSuccessful()).thenReturn(false);

        getFairsUseCase.addParameters(apiParameters);
        getFairsUseCase.registerListener(listener);
        getFairsUseCase.executeRequest();

        verify(call).enqueue(argumentCaptor.capture());
        argumentCaptor.getValue().onResponse(call, response);

        verify(listener).onFailure(exception);
    }

    @Test
    public void it_propagates_failure_on_library_error() throws IOException {

        Map<String, String> apiParameters   = new HashMap<>();
        IOException exception               = new IOException();

        Call<Fair[]> call                               = networkMockHelper.getCallMock();
        ArgumentCaptor<Callback<Fair[]>> argumentCaptor = networkMockHelper.getResponseCallbackCaptor();

        when(apiClientMock.getFairs(apiParameters)).thenReturn(call);

        getFairsUseCase.addParameters(apiParameters);
        getFairsUseCase.registerListener(listener);
        getFairsUseCase.executeRequest();

        verify(call).enqueue(argumentCaptor.capture());
        argumentCaptor.getValue().onFailure(call, exception);

        verify(listener).onFailure(exception);
    }
}
