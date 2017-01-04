package com.example.ericdesedas.expohub.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.Stand;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetStandsByFairUseCase;

public class StandsByFairPresenter extends Presenter {

    private GetStandsByFairUseCase getStandsByFairUseCase;
    private View view;

    private ApiUseCase.Listener<Stand[]> listener = new ApiUseCase.Listener<Stand[]>() {
        @Override
        public void onResponse(int statusCode, Stand[] result) {
            view.toggleLoading(false);
            view.updateList(result);
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

    public StandsByFairPresenter(GetStandsByFairUseCase getStandsByFairUseCase) {
        this.getStandsByFairUseCase = getStandsByFairUseCase;
    }

    @Override
    public void onStop() {
        super.onStop();
        getStandsByFairUseCase.unregisterListener(listener);
    }

    public void setView(View view) {
        this.view = view;
    }

    public void initialize() {
        getStandsByFairUseCase.registerListener(listener);
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
