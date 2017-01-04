package com.example.ericdesedas.expohub.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairsUseCase;

public class MainScreenPresenter extends Presenter {

    private GetFairsUseCase getFairsUseCase;
    private View view;

    // Listeners

    private ApiUseCase.Listener<Fair[]> getFairUseCaseListener = new ApiUseCase.Listener<Fair[]>() {
        @Override
        public void onResponse(int statusCode, Fair[] result) {
            view.toggleLoading(false);
            view.updateFairList(result);
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

    /**
     * Constructor
     * @param getFairsUseCase the {@link GetFairsUseCase} reference
     */
    public MainScreenPresenter(GetFairsUseCase getFairsUseCase) {
        this.getFairsUseCase = getFairsUseCase;
        this.view = null;
    }

    public void initialize() {
        this.getFairsUseCase.registerListener(getFairUseCaseListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        getFairsUseCase.unregisterListener(getFairUseCaseListener);
    }

    /**
     * Register the presenter's view
     * @param view the {@link View} that's going to be registered
     */
    public void setView(View view) {
        this.view = view;
    }

    // Commands

    /**
     * Loads fairs
     */
    public void onLoadFairsCommand() {
        view.toggleLoading(true);
        getFairsUseCase.executeRequest();
    }

    // View Interfaces

    public interface View {
        void toggleLoading(boolean showLoading);
        void updateFairList(Fair[] fairs);
        void showError(int code, String error);
    }
}
