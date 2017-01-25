package com.example.ericdesedas.expohub.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.News;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetNewsByFairUseCase;

import java.util.List;

import moe.banana.jsonapi2.Document;

public class NewsByFairPresenter extends Presenter {

    private GetNewsByFairUseCase newsByFairUseCase;
    private View view;

    private ApiUseCase.Listener<Document<News>> listener = new ApiUseCase.Listener<Document<News>>() {
        @Override
        public void onResponse(int statusCode, Document<News> result) {

            List<News> newsList = generateArrayFromDocument(result);
            News[] newsArray = newsList.toArray(new News[newsList.size()]);

            view.toggleLoading(false);
            view.updateList(newsArray);
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
