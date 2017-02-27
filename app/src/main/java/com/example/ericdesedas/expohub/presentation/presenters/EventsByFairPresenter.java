package com.example.ericdesedas.expohub.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetEventsByFairUseCase;

import java.util.List;

import moe.banana.jsonapi2.Document;

public class EventsByFairPresenter extends Presenter {

    private GetEventsByFairUseCase useCase;
    private View view;

    private ApiUseCase.Listener<Document<FairEvent>> listener = new ApiUseCase.Listener<Document<FairEvent>>() {
        @Override
        public void onResponse(int statusCode, Document<FairEvent> result) {

            List<FairEvent> fairEvents  = generateArrayFromDocument(result);
            FairEvent[] fairEventsArray = fairEvents.toArray(new FairEvent[fairEvents.size()]);

            view.toggleLoading(false);
            view.updateEventList(fairEventsArray);
        }

        @Override
        public void onError(int statusCode, ApiErrorWrapper apiError) {
            view.toggleLoading(false);
            if (apiError.hasUniqueError()) {
                view.showError(statusCode, apiError.getUniqueError().getDetail());
            } else {
                String errorString = concatenateErrorString(apiError.getErrorList());
                view.showError(statusCode, errorString);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            view.toggleLoading(false);
            view.showError(500, "");
        }
    };

    public EventsByFairPresenter(GetEventsByFairUseCase useCase) {
        this.useCase = useCase;
        useCase.addParameter("include", "eventType");
    }

    @Override
    public void onStart() {
        super.onStart();
        useCase.registerListener(listener);
    }

    @Override
    public void onStop() {
        super.onStop();
        useCase.unregisterListener(listener);
    }

    public void setView(View view) {
        this.view = view;
    }

    // Commands

    public void onLoadEventsCommand(String fairId) {
        view.toggleLoading(true);
        useCase.executeRequest(fairId);
    }

    // View Interface

    public interface View {
        void toggleLoading(boolean showLoading);
        void updateEventList(FairEvent[] fairEvents);
        void showError(int code, String error);
    }
}
