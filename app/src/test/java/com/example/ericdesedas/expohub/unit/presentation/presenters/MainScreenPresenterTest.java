package com.example.ericdesedas.expohub.unit.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairsUseCase;
import com.example.ericdesedas.expohub.presentation.presenters.MainScreenPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MainScreenPresenterTest extends BasePresenterTest {

    MainScreenPresenter mainScreenPresenter;

    @Mock
    GetFairsUseCase getFairsUseCase;

    @Mock
    MainScreenPresenter.View view;

    @Before
    public void setUp() {
        mainScreenPresenter = new MainScreenPresenter(getFairsUseCase);
    }

//    @Test
//    public void it_updates_view_on_success() {
//
//        Fair[] fairs    = new Fair[] {};
//        int statusCode  = 200;
//
//        Class<ApiUseCase.Listener<Fair[]>> listenerClass            = (Class<ApiUseCase.Listener<Fair[]>>) (Class) ApiUseCase.Listener.class;
//        ArgumentCaptor<ApiUseCase.Listener<Fair[]>> argumentCaptor  = ArgumentCaptor.forClass(listenerClass);
//
//        mainScreenPresenter.setView(view);
//        mainScreenPresenter.initialize();
//        verify(getFairsUseCase).registerListener(argumentCaptor.capture());
//
//        mainScreenPresenter.onLoadFairsCommand();
//
//        verify(view).toggleLoading(true);
//
//        argumentCaptor.getValue().onResponse(statusCode, fairs);
//
//        verify(view).toggleLoading(false);
//        verify(view).updateFairList(fairs);
//    }
//
//    @Test
//    public void it_updates_view_on_single_error() {
//
//        ApiErrorWrapper apiErrorWrapper = new ApiErrorWrapper();
//        apiErrorWrapper.addError(createError("fooTitle", "fooDetail", "fooCode", "400"));
//
//        Class<ApiUseCase.Listener<Fair[]>> listenerClass            = (Class<ApiUseCase.Listener<Fair[]>>) (Class) ApiUseCase.Listener.class;
//        ArgumentCaptor<ApiUseCase.Listener<Fair[]>> argumentCaptor  = ArgumentCaptor.forClass(listenerClass);
//
//        mainScreenPresenter.setView(view);
//        mainScreenPresenter.initialize();
//        verify(getFairsUseCase).registerListener(argumentCaptor.capture());
//
//        mainScreenPresenter.onLoadFairsCommand();
//
//        verify(view).toggleLoading(true);
//
//        argumentCaptor.getValue().onError(400, apiErrorWrapper);
//
//        verify(view).toggleLoading(false);
//        verify(view).showError(400, "fooDetail");
//    }
//
//    @Test
//    public void it_updates_view_on_multiple_errors() {
//
//        ApiErrorWrapper apiErrorWrapper = new ApiErrorWrapper();
//        apiErrorWrapper.addError(createError("fooTitle", "fooDetail", "fooCode", "400"));
//        apiErrorWrapper.addError(createError("barTitle", "barDetail", "barCode",  "400"));
//
//        Class<ApiUseCase.Listener<Fair[]>> listenerClass            = (Class<ApiUseCase.Listener<Fair[]>>) (Class) ApiUseCase.Listener.class;
//        ArgumentCaptor<ApiUseCase.Listener<Fair[]>> argumentCaptor  = ArgumentCaptor.forClass(listenerClass);
//
//        mainScreenPresenter.setView(view);
//        mainScreenPresenter.initialize();
//        verify(getFairsUseCase).registerListener(argumentCaptor.capture());
//
//        mainScreenPresenter.onLoadFairsCommand();
//
//        verify(view).toggleLoading(true);
//
//        argumentCaptor.getValue().onError(400, apiErrorWrapper);
//
//        verify(view).toggleLoading(false);
//        verify(view).showError(400, "fooDetail, barDetail");
//    }
//
//    @Test
//    public void it_updates_view_on_failure() {
//
//        Exception exception = new Exception();
//
//        Class<ApiUseCase.Listener<Fair[]>> listenerClass            = (Class<ApiUseCase.Listener<Fair[]>>) (Class) ApiUseCase.Listener.class;
//        ArgumentCaptor<ApiUseCase.Listener<Fair[]>> argumentCaptor  = ArgumentCaptor.forClass(listenerClass);
//
//        mainScreenPresenter.setView(view);
//        mainScreenPresenter.initialize();
//        verify(getFairsUseCase).registerListener(argumentCaptor.capture());
//
//        mainScreenPresenter.onLoadFairsCommand();
//
//        verify(view).toggleLoading(true);
//
//        argumentCaptor.getValue().onFailure(exception);
//
//        verify(view).toggleLoading(false);
//    }

}
