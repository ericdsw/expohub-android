package com.example.ericdesedas.expohub.presentation.presenters;


import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.News;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleNewsUseCase;

import moe.banana.jsonapi2.Document;

public class NewsDetailPresenter extends Presenter {

    private GetSingleNewsUseCase useCase;
    private View view;

    private ApiUseCase.Listener<Document<News>> singleNewsListener = new ApiUseCase.Listener<Document<News>>() {
        @Override
        public void onResponse(int statusCode, Document<News> result) {
            view.toggleLoading(false);
            view.updateNews(result.get());
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

    public NewsDetailPresenter(GetSingleNewsUseCase useCase) {
        this.useCase = useCase;
        this.useCase.addParameter("include", "comments,comments.user");
    }

    @Override
    public void onStart() {
        super.onStart();
        useCase.registerListener(singleNewsListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        useCase.unregisterListener(singleNewsListener);
    }

    public void setView(View view) {
        this.view = view;
    }

    public void onLoadSingleNewsCommand(String newsId) {
        view.toggleLoading(true);
        useCase.executeRequest(newsId);
    }

    public interface View {
        void toggleLoading(boolean showLoading);
        void updateNews(News news);
        void showError(int code, String error);
    }
}
