package com.example.ericdesedas.expohub.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleFairEventUseCase;

import moe.banana.jsonapi2.Document;

public class FairEventDetailsPresenter extends Presenter {

    private GetSingleFairEventUseCase singleFairEventUseCase;
    private View view;

    private ApiUseCase.Listener<Document<FairEvent>> singleFairEventListener = new ApiUseCase.Listener<Document<FairEvent>>() {
        @Override
        public void onResponse(int statusCode, Document<FairEvent> result) {
            view.toggleLoading(false);
            view.updateFairEvent(result.get());
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

    public FairEventDetailsPresenter(GetSingleFairEventUseCase useCase) {
        this.singleFairEventUseCase = useCase;
    }

    public void initialize() {
        singleFairEventUseCase.addParameter("include", "speakers");
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        super.onStart();
        singleFairEventUseCase.registerListener(singleFairEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        singleFairEventUseCase.unregisterListener(singleFairEventListener);
    }

    // Commands

    public void onLoadFairEventCommand(String fairEventId) {
        view.toggleLoading(true);
        singleFairEventUseCase.executeRequest(fairEventId);
    }

    public interface View {
        void toggleLoading(boolean showLoading);
        void updateFairEvent(FairEvent fairEvent);
        void showError(int code, String error);
    }
}
