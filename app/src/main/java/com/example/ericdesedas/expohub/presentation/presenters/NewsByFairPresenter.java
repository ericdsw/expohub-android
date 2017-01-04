package com.example.ericdesedas.expohub.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.News;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetNewsByFairUseCase;

public class NewsByFairPresenter extends Presenter {

    private GetNewsByFairUseCase newsByFairUseCase;
    private View view;

    private ApiUseCase.Listener<News[]> listener = new ApiUseCase.Listener<News[]>() {
        @Override
        public void onResponse(int statusCode, News[] result) {
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

    public NewsByFairPresenter(GetNewsByFairUseCase useCase) {
        this.newsByFairUseCase = useCase;
    }

    @Override
    public void onStop() {
        super.onStop();
        newsByFairUseCase.unregisterListener(listener);
    }

    public void setView(View view) {
        this.view = view;
    }

    public void initialize() {
        newsByFairUseCase.registerListener(listener);
    }

    public void onLoadEventsCommand(String fairId) {
        view.toggleLoading(true);
        newsByFairUseCase.executeRequest(fairId);
    }

    public interface View {
        void toggleLoading(boolean showLoading);
        void updateList(News[] newses);
        void showError(int code, String error);
    }
}
