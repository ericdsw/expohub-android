package com.example.ericdesedas.expohub.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.Stand;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetStandsByFairUseCase;

import java.util.List;

import moe.banana.jsonapi2.Document;

public class StandsByFairPresenter extends Presenter {

    private GetStandsByFairUseCase getStandsByFairUseCase;
    private View view;

    private ApiUseCase.Listener<Document<Stand>> listener = new ApiUseCase.Listener<Document<Stand>>() {
        @Override
        public void onResponse(int statusCode, Document<Stand> result) {

            List<Stand> standList   = generateArrayFromDocument(result);
            Stand[] standsArray     = standList.toArray(new Stand[standList.size()]);

            view.toggleLoading(false);
            view.updateList(standsArray);
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

    public StandsByFairPresenter(GetStandsByFairUseCase getStandsByFairUseCase) {
        this.getStandsByFairUseCase = getStandsByFairUseCase;
    }

    @Override
    public void onStart() {
        super.onStart();
        getStandsByFairUseCase.registerListener(listener);
    }

    @Override
    public void onStop() {
        super.onStop();
        getStandsByFairUseCase.unregisterListener(listener);
    }

    public void setView(View view) {
        this.view = view;
    }

    public void onLoadStandsCommand(String fairId) {
        view.toggleLoading(true);
        getStandsByFairUseCase.executeRequest(fairId);
    }

    public interface View {
        void toggleLoading(boolean showLoading);
        void updateList(Stand[] stands);
        void showError(int code, String error);
    }
}
