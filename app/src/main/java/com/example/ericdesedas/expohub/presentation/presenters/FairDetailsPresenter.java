package com.example.ericdesedas.expohub.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleFairUseCase;

import java.util.List;

import moe.banana.jsonapi2.Document;

public class FairDetailsPresenter extends Presenter {

    private GetSingleFairUseCase getSingleFairUseCase;
    private View view;

    private ApiUseCase.Listener<Document<Fair>> getSingleFairsUseCaseListener = new ApiUseCase.Listener<Document<Fair>>() {
        @Override
        public void onResponse(int statusCode, Document<Fair> result) {
            view.toggleLoading(false);
            view.updateFair(result.get());
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

    /**
     * Constructor
     * @param getSingleFairUseCase the {@link GetSingleFairUseCase} reference execute the request
     */
    public FairDetailsPresenter(GetSingleFairUseCase getSingleFairUseCase) {
        this.getSingleFairUseCase = getSingleFairUseCase;
    }

    @Override
    public void onStop() {
        super.onStop();
        getSingleFairUseCase.unregisterListener(getSingleFairsUseCaseListener);
    }

    public void setView(View view) {
        this.view = view;
    }

    public void initialize() {
        this.getSingleFairUseCase.registerListener(getSingleFairsUseCaseListener);
    }

    // Commands

    public void onLoadFairCommand(String fairId) {
        view.toggleLoading(true);
        getSingleFairUseCase.executeRequest(fairId);
    }

    public interface View {
        void toggleLoading(boolean showLoading);
        void updateFair(Fair fair);
        void showError(int code, String error);
    }
}
