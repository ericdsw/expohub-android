package com.example.ericdesedas.expohub.presentation.presenters;


import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.Comment;
import com.example.ericdesedas.expohub.data.models.News;
import com.example.ericdesedas.expohub.data.models.Session;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.CreateCommentUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleNewsUseCase;

import moe.banana.jsonapi2.Document;

public class NewsDetailPresenter extends Presenter {

    private GetSingleNewsUseCase getSingleNewsUseCase;
    private CreateCommentUseCase createCommentUseCase;
    private SessionManager sessionManager;
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

    private ApiUseCase.Listener<Document<Comment>> createCommentListener = new ApiUseCase.Listener<Document<Comment>>() {
        @Override
        public void onResponse(int statusCode, Document<Comment> result) {
            view.toggleCommentCreating(false);
            view.showCommentCreationMessage();
        }

        @Override
        public void onError(int statusCode, ApiErrorWrapper apiError) {
            view.toggleCommentCreating(false);
            if (apiError.hasUniqueError()) {
                view.showCommentCreationErrorMessage(statusCode, apiError.getUniqueError().getDetail());
            } else {
                String errorString = concatenateErrorString(apiError.getErrorList());
                view.showCommentCreationErrorMessage(statusCode, errorString);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            view.toggleCommentCreating(false);
            view.showCommentCreationErrorMessage(500, "");
        }
    };

    /**
     * Constructor
     *
     * @param getSingleNewsUseCase  the {@link GetSingleNewsUseCase} instance
     * @param createCommentUseCase  the {@link CreateCommentUseCase} instance
     * @param sessionManager        the {@link SessionManager} instance
     */
    public NewsDetailPresenter(GetSingleNewsUseCase getSingleNewsUseCase, CreateCommentUseCase createCommentUseCase, SessionManager sessionManager) {

        this.getSingleNewsUseCase   = getSingleNewsUseCase;
        this.createCommentUseCase   = createCommentUseCase;
        this.sessionManager         = sessionManager;

        this.getSingleNewsUseCase.addParameter("include", "comments,comments.user");
    }

    @Override
    public void onStart() {
        super.onStart();
        getSingleNewsUseCase.registerListener(singleNewsListener);
        createCommentUseCase.registerListener(createCommentListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        getSingleNewsUseCase.unregisterListener(singleNewsListener);
        createCommentUseCase.unregisterListener(createCommentListener);
    }

    public void setView(View view) {
        this.view = view;
    }

    public void onLoadSingleNewsCommand(String newsId) {
        view.toggleLoading(true);
        getSingleNewsUseCase.executeRequest(newsId);
    }

    public void onCreateCommentCommand(String text, String newsId) {
        view.toggleCommentCreating(true);
        createCommentUseCase.executeRequest(text, newsId);
    }

    public boolean canRequestNewComment() {
        return sessionManager.isLoggedIn();
    }

    public interface View {
        void toggleLoading(boolean showLoading);
        void toggleCommentCreating(boolean isCreating);
        void showCommentCreationMessage();
        void showCommentCreationErrorMessage(int code, String message);
        void updateNews(News news);
        void showError(int code, String error);
    }
}
