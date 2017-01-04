package com.example.ericdesedas.expohub.unit.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleFairUseCase;
import com.example.ericdesedas.expohub.presentation.presenters.FairDetailsPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FairDetailsPresenterTest {

    FairDetailsPresenter fairDetailsPresenter;

    @Mock
    GetSingleFairUseCase getSingleFairUseCaseMock;

    @Mock
    FairDetailsPresenter.View viewMock;

    @Before
    public void setUp() {
        fairDetailsPresenter = new FairDetailsPresenter(getSingleFairUseCaseMock);
    }

    @Test
    public void it_updates_views_on_success() {

        String fairId   = "1";
        Fair fair       = new Fair();
        int statusCode  = 200;

        Class<ApiUseCase.Listener<Fair>> listenerClass              = (Class<ApiUseCase.Listener<Fair>>) (Class) ApiUseCase.Listener.class;
        ArgumentCaptor<ApiUseCase.Listener<Fair>> argumentCaptor    = ArgumentCaptor.forClass(listenerClass);

        fairDetailsPresenter.setView(viewMock);
        fairDetailsPresenter.initialize();
        verify(getSingleFairUseCaseMock).registerListener(argumentCaptor.capture());

        fairDetailsPresenter.onLoadFairCommand(fairId);

        verify(viewMock).toggleLoading(true);

        argumentCaptor.getValue().onResponse(statusCode, fair);

        verify(viewMock).toggleLoading(false);
        verify(viewMock).updateFair(fair);
    }

    @Test
    public void it_updates_views_on_single_error() {

        String fairId   = "1";
        int statusCode  = 400;

        ApiErrorWrapper apiErrorWrapper = new ApiErrorWrapper();
        apiErrorWrapper.errorList.add(new ApiErrorWrapper.Error("fooTitle", "fooMessage", statusCode));

        Class<ApiUseCase.Listener<Fair>> listenerClass              = (Class<ApiUseCase.Listener<Fair>>) (Class) ApiUseCase.Listener.class;
        ArgumentCaptor<ApiUseCase.Listener<Fair>> argumentCaptor    = ArgumentCaptor.forClass(listenerClass);

        fairDetailsPresenter.setView(viewMock);
        fairDetailsPresenter.initialize();
        verify(getSingleFairUseCaseMock).registerListener(argumentCaptor.capture());

        fairDetailsPresenter.onLoadFairCommand(fairId);

        verify(viewMock).toggleLoading(true);

        argumentCaptor.getValue().onError(statusCode, apiErrorWrapper);

        verify(viewMock).toggleLoading(false);
        verify(viewMock).showError(statusCode, "fooMessage");
    }

    @Test
    public void it_updates_views_on_multiple_errors() {

        String fairId   = "1";
        int statusCode  = 400;

        ApiErrorWrapper apiErrorWrapper = new ApiErrorWrapper();
        apiErrorWrapper.errorList.add(new ApiErrorWrapper.Error("fooTitle", "fooMessage", statusCode));
        apiErrorWrapper.errorList.add(new ApiErrorWrapper.Error("barTitle", "barMessage", statusCode));

        Class<ApiUseCase.Listener<Fair>> listenerClass              = (Class<ApiUseCase.Listener<Fair>>) (Class) ApiUseCase.Listener.class;
        ArgumentCaptor<ApiUseCase.Listener<Fair>> argumentCaptor    = ArgumentCaptor.forClass(listenerClass);

        fairDetailsPresenter.setView(viewMock);
        fairDetailsPresenter.initialize();
        verify(getSingleFairUseCaseMock).registerListener(argumentCaptor.capture());

        fairDetailsPresenter.onLoadFairCommand(fairId);

        verify(viewMock).toggleLoading(true);

        argumentCaptor.getValue().onError(statusCode, apiErrorWrapper);

        verify(viewMock).toggleLoading(false);
        verify(viewMock).showError(statusCode, "fooMessage, barMessage");
    }

    @Test
    public void it_updates_views_on_failure() {

        String fairId       = "1";
        Exception exception = new Exception();

        Class<ApiUseCase.Listener<Fair>> listenerClass              = (Class<ApiUseCase.Listener<Fair>>) (Class) ApiUseCase.Listener.class;
        ArgumentCaptor<ApiUseCase.Listener<Fair>> argumentCaptor    = ArgumentCaptor.forClass(listenerClass);

        fairDetailsPresenter.setView(viewMock);
        fairDetailsPresenter.initialize();
        verify(getSingleFairUseCaseMock).registerListener(argumentCaptor.capture());

        fairDetailsPresenter.onLoadFairCommand(fairId);

        verify(viewMock).toggleLoading(true);

        argumentCaptor.getValue().onFailure(exception);

        verify(viewMock).toggleLoading(false);
    }
}
