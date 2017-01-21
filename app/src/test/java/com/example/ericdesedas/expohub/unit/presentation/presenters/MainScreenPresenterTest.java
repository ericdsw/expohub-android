package com.example.ericdesedas.expohub.unit.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairEventsUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairsUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleUserUseCase;
import com.example.ericdesedas.expohub.presentation.presenters.MainScreenPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import moe.banana.jsonapi2.Document;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MainScreenPresenterTest extends BasePresenterTest {

    private MainScreenPresenter mainScreenPresenter;

    @Mock GetFairsUseCase getFairsUseCase;
    @Mock GetSingleUserUseCase getSingleUserUseCase;
    @Mock GetFairEventsUseCase getFairEventsUseCase;
    @Mock SessionManager sessionManager;
    @Mock MainScreenPresenter.View view;

    @Before
    public void setUp() {
        mainScreenPresenter = new MainScreenPresenter(getFairsUseCase, getSingleUserUseCase,
                                                      getFairEventsUseCase, sessionManager);
    }

    @Test
    public void it_updates_view_on_success() {

        Document<Fair> document = new Document<>();
        int statusCode  = 200;

        document.add(new Fair());
        document.add(new Fair());

        Class<ApiUseCase.Listener<Document<Fair>>> listenerClass            = (Class<ApiUseCase.Listener<Document<Fair>>>) (Class) ApiUseCase.Listener.class;
        ArgumentCaptor<ApiUseCase.Listener<Document<Fair>>> argumentCaptor  = ArgumentCaptor.forClass(listenerClass);

        mainScreenPresenter.setView(view);
        mainScreenPresenter.initialize();
        mainScreenPresenter.onStart();

        verify(getFairsUseCase).registerListener(argumentCaptor.capture());

        mainScreenPresenter.onLoadFairsCommand();

        verify(view).toggleLoading(true);

        argumentCaptor.getValue().onResponse(statusCode, document);

        verify(view).toggleLoading(false);
        verify(view).updateFairList(any(Fair[].class));
    }

    @Test
    public void it_updates_view_on_single_error() {

        ApiErrorWrapper apiErrorWrapper = new ApiErrorWrapper();
        apiErrorWrapper.addError(createError("fooTitle", "fooDetail", "fooCode", "400"));

        Class<ApiUseCase.Listener<Document<Fair>>> listenerClass            = (Class<ApiUseCase.Listener<Document<Fair>>>) (Class) ApiUseCase.Listener.class;
        ArgumentCaptor<ApiUseCase.Listener<Document<Fair>>> argumentCaptor  = ArgumentCaptor.forClass(listenerClass);

        mainScreenPresenter.setView(view);
        mainScreenPresenter.initialize();
        mainScreenPresenter.onStart();

        verify(getFairsUseCase).registerListener(argumentCaptor.capture());

        mainScreenPresenter.onLoadFairsCommand();

        verify(view).toggleLoading(true);

        argumentCaptor.getValue().onError(400, apiErrorWrapper);

        verify(view).toggleLoading(false);
        verify(view).showError(400, "fooDetail");
    }

    @Test
    public void it_updates_view_on_multiple_errors() {

        ApiErrorWrapper apiErrorWrapper = new ApiErrorWrapper();
        apiErrorWrapper.addError(createError("fooTitle", "fooDetail", "fooCode", "400"));
        apiErrorWrapper.addError(createError("barTitle", "barDetail", "barCode",  "400"));

        Class<ApiUseCase.Listener<Document<Fair>>> listenerClass            = (Class<ApiUseCase.Listener<Document<Fair>>>) (Class) ApiUseCase.Listener.class;
        ArgumentCaptor<ApiUseCase.Listener<Document<Fair>>> argumentCaptor  = ArgumentCaptor.forClass(listenerClass);

        mainScreenPresenter.setView(view);
        mainScreenPresenter.initialize();
        mainScreenPresenter.onStart();

        verify(getFairsUseCase).registerListener(argumentCaptor.capture());

        mainScreenPresenter.onLoadFairsCommand();

        verify(view).toggleLoading(true);

        argumentCaptor.getValue().onError(400, apiErrorWrapper);

        verify(view).toggleLoading(false);
        verify(view).showError(400, "fooDetail, barDetail");
    }

    @Test
    public void it_updates_view_on_failure() {

        Exception exception = new Exception();

        Class<ApiUseCase.Listener<Document<Fair>>> listenerClass            = (Class<ApiUseCase.Listener<Document<Fair>>>) (Class) ApiUseCase.Listener.class;
        ArgumentCaptor<ApiUseCase.Listener<Document<Fair>>> argumentCaptor  = ArgumentCaptor.forClass(listenerClass);

        mainScreenPresenter.setView(view);
        mainScreenPresenter.initialize();
        mainScreenPresenter.onStart();

        verify(getFairsUseCase).registerListener(argumentCaptor.capture());

        mainScreenPresenter.onLoadFairsCommand();

        verify(view).toggleLoading(true);

        argumentCaptor.getValue().onFailure(exception);

        verify(view).toggleLoading(false);
    }

}
