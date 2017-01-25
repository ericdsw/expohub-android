package com.example.ericdesedas.expohub.unit.domain.interactors;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.helpers.NetworkMockHelper;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;

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

abstract class BaseApiUseCaseTest {

    @Mock ApiClient apiClientMock;
    @Mock Moshi moshiMock;
    @Mock JsonAdapter<ApiErrorWrapper> errorApiJsonAdapterMock;

    private NetworkMockHelper networkMockHelper;

    public void setUp() {
        networkMockHelper = new NetworkMockHelper();
    }

    /**
     * Executes a generic success expectation for the use case
     *
     * @param testedUseCase         a {@link K} instance representing the system under test
     * @param testedUseCaseListener a {@link ApiUseCase.Listener<T>} instance representing the use case listener
     * @param responseData          a {@link T} that represents what does the use case returns
     * @param callback              an {@link ApiClientRequestCallback<T, K>} instance that permits execution modification
     * @param <T>                   a {@link T} representing the resource that the use case works on
     * @param <K>                   a {@link K} representing the type of ApiUseCase that the class is testing
     */
    <T, K extends ApiUseCase<T>> void executeSuccessExpectations(
            K testedUseCase, ApiUseCase.Listener<T> testedUseCaseListener,
            T responseData,  ApiClientRequestCallback<T, K> callback
    ) {

        int statusCode                  = 200;
        Map<String, String> parameters  = new HashMap<>();

        Call<T> call                                = networkMockHelper.getCallMock();
        Response<T> response                        = networkMockHelper.getResponseMock();
        ArgumentCaptor<Callback<T>> argumentCaptor  = networkMockHelper.getResponseCallbackCaptor();

        // Configures the expectations for the ApiClient
        callback.setupExecutedApiRequest(apiClientMock, parameters, call);

        when(response.body()).thenReturn(responseData);
        when(response.code()).thenReturn(statusCode);
        when(response.isSuccessful()).thenReturn(true);

        testedUseCase.addParameters(parameters);
        testedUseCase.registerListener(testedUseCaseListener);

        // Calls the specified use case method to communicate with the ApiClient
        callback.triggerRequest(testedUseCase);

        // Configures additional expectations for processResponseData
        callback.onProcessResponseData(response);

        verify(call).enqueue(argumentCaptor.capture());
        argumentCaptor.getValue().onResponse(call, response);

        verify(testedUseCaseListener).onResponse(statusCode, responseData);
    }

    /**
     * Executes a generic error expectation for the use case
     *
     * @param testedUseCase         a {@link K} instance representing the system under test
     * @param testedUseCaseListener a {@link ApiUseCase.Listener<T>} instance representing the use case listener
     * @param callback              an {@link ApiClientRequestCallback<T, K>} instance that permits execution modification
     * @param <T>                   a {@link T} representing the resource that the use case works on
     * @param <K>                   a {@link K} representing the type of ApiUseCase that the class is testing
     */
    <T, K extends ApiUseCase<T>> void executeErrorExpectations(
            K testedUseCase, ApiUseCase.Listener<T> testedUseCaseListener,
            ApiClientRequestCallback<T, K> callback
    ) throws IOException {

        int statusCode                  = 400;
        Map<String, String> parameters  = new HashMap<>();

        String errorBody                = "foo";
        ApiErrorWrapper apiErrorWrapper = new ApiErrorWrapper();
        ResponseBody responseBodyMock   = PowerMockito.mock(ResponseBody.class);

        Call<T> call                                = networkMockHelper.getCallMock();
        Response<T> response                        = networkMockHelper.getResponseMock();
        ArgumentCaptor<Callback<T>> argumentCaptor  = networkMockHelper.getResponseCallbackCaptor();

        // Configures the expectations for the ApiClient
        callback.setupExecutedApiRequest(apiClientMock, parameters, call);

        when(response.errorBody()).thenReturn(responseBodyMock);
        when(response.code()).thenReturn(statusCode);
        when(response.isSuccessful()).thenReturn(false);
        when(responseBodyMock.string()).thenReturn(errorBody);
        when(moshiMock.adapter(eq(ApiErrorWrapper.class))).thenReturn(errorApiJsonAdapterMock);
        when(errorApiJsonAdapterMock.fromJson(errorBody)).thenReturn(apiErrorWrapper);

        testedUseCase.addParameters(parameters);
        testedUseCase.registerListener(testedUseCaseListener);

        // Calls the specified use case method to communicate with the ApiClient
        callback.triggerRequest(testedUseCase);

        verify(call).enqueue(argumentCaptor.capture());
        argumentCaptor.getValue().onResponse(call, response);

        // Configures additional expectations for processResponseData
        callback.onProcessResponseData(response);

        verify(testedUseCaseListener).onError(statusCode, apiErrorWrapper);
    }

    /**
     * Executes a generic error expectation for the use case
     *
     * @param testedUseCase         a {@link K} instance representing the system under test
     * @param testedUseCaseListener a {@link ApiUseCase.Listener<T>} instance representing the use case listener
     * @param byInvalidJson         a {@link boolean} value that determines if the error is caused by the
     * @param callback              an {@link ApiClientRequestCallback<T, K>} instance that permits execution modification
     * @param <T>                   a {@link T} representing the resource that the use case works on
     * @param <K>                   a {@link K} representing the type of ApiUseCase that the class is testing
     */
    <T, K extends ApiUseCase<T>> void executeFailureExpectation(
            K testedUseCase, ApiUseCase.Listener<T> testedUseCaseListener,
            boolean byInvalidJson, ApiClientRequestCallback<T, K> callback
    ) throws IOException {

        int statusCode                  = 400;
        Map<String, String> parameters  = new HashMap<>();

        String errorBody                = "foo";
        ResponseBody responseBodyMock   = PowerMockito.mock(ResponseBody.class);
        IOException exception           = new IOException();

        Call<T> call                                = networkMockHelper.getCallMock();
        Response<T> response                        = networkMockHelper.getResponseMock();
        ArgumentCaptor<Callback<T>> argumentCaptor  = networkMockHelper.getResponseCallbackCaptor();

        // Configures the expectations for the ApiClient
        callback.setupExecutedApiRequest(apiClientMock, parameters, call);

        if (byInvalidJson) {
            when(response.errorBody()).thenReturn(responseBodyMock);
            when(response.code()).thenReturn(statusCode);
            when(response.isSuccessful()).thenReturn(false);
            when(responseBodyMock.string()).thenReturn(errorBody);
            when(moshiMock.adapter(eq(ApiErrorWrapper.class))).thenReturn(errorApiJsonAdapterMock);
            when(errorApiJsonAdapterMock.fromJson(errorBody)).thenThrow(exception);
        }

        testedUseCase.addParameters(parameters);
        testedUseCase.registerListener(testedUseCaseListener);

        // Calls the specified use case method to communicate with the ApiClient
        callback.triggerRequest(testedUseCase);

        verify(call).enqueue(argumentCaptor.capture());

        if (byInvalidJson) {
            argumentCaptor.getValue().onResponse(call, response);
        } else {
            argumentCaptor.getValue().onFailure(call, exception);
        }

        // Configures additional expectations for processResponseData
        callback.onProcessResponseData(response);

        verify(testedUseCaseListener).onFailure(exception);
    }

    /**
     * This interface exposes key points in the expectation lifecycle defined in this class
     *
     * @param <T>   the resource type managed by this use case
     * @param <K>   the type extending {@link ApiUseCase<T>}
     */
    protected interface ApiClientRequestCallback <T, K extends ApiUseCase<T>> {
        void setupExecutedApiRequest(ApiClient apiClient, Map<String, String> parameters, Call<T> call);
        void triggerRequest(K apiUseCase);
        void onProcessResponseData(Response<T> response);
    }
}
