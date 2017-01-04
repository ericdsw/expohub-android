package com.example.ericdesedas.expohub.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetEventsByFairUseCase;

public class EventsByFairPresenter extends Presenter {

    private GetEventsByFairUseCase useCase;
    private View view;

    private ApiUseCase.Listener<FairEvent[]> listener = new ApiUseCase.Listener<FairEvent[]>() {
        @Override
        public void onResponse(int statusCode, FairEvent[] result) {
            view.toggleLoading(false);
            view.updateEventList(result);
        }

        @Override
        public void onError(int statusCode, ApiErrorWrapper apiError) {
            view.toggleLoading(false);
            if (apiError.hasUniqueError()) {
                view.showError(statusCode, apiError.getUniqueError().message);
            } else {
                String errorString = concatenateErrorString(apiError.errorList);
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
    }

    @Override
    public void onStop() {
        super.onStop();
        useCase.unregisterListener(listener);
    }

    public void setView(View view) {
        this.view = view;
    }

    /**
     * Initializes use cases
     */
    public void initialize() {
        useCase.registerListener(listener);
        useCase.addParameter("include", "eventType");
    }

    public void onLoadEventsCommand(String fairId) {
        view.toggleLoading(true);
        useCase.executeRequest(fairId);
    }

    public interface View {
        void toggleLoading(boolean showLoading);
        void updateEventList(FairEvent[] fairEvents);
        void showError(int code, String error);
    }
}
