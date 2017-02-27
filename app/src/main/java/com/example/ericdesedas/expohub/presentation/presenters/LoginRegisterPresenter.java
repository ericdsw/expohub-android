package com.example.ericdesedas.expohub.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.User;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.LoginUseCase;
import com.example.ericdesedas.expohub.domain.interactors.RegisterUseCase;

import moe.banana.jsonapi2.Document;

public class LoginRegisterPresenter extends Presenter {

    private LoginUseCase loginUseCase;
    private RegisterUseCase registerUseCase;
    private View view;

    private ApiUseCase.Listener<Document<User>> loginUseCaseListener = new ApiUseCase.Listener<Document<User>>() {
        @Override
        public void onResponse(int statusCode, Document<User> result) {
            view.toggleLoginLoading(false);
            view.showLoggedMessage(result.get());
            view.closeView();
        }

        @Override
        public void onError(int statusCode, ApiErrorWrapper apiError) {
            view.toggleLoginLoading(false);
            if (apiError.hasUniqueError()) {
                view.showError(statusCode, apiError.getUniqueError().getDetail());
            } else {
                String errorString = concatenateErrorString(apiError.getErrorList());
                view.showError(statusCode, errorString);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            view.toggleLoginLoading(false);
            view.showError(500, "");
        }
    };

    private ApiUseCase.Listener<Document<User>> registerUseCaseListener = new ApiUseCase.Listener<Document<User>>() {
        @Override
        public void onResponse(int statusCode, Document<User> result) {
            view.toggleRegisterLoading(false);
            view.showRegisteredMessage(result.get());
            view.closeView();
        }

        @Override
        public void onError(int statusCode, ApiErrorWrapper apiError) {
            view.toggleRegisterLoading(false);
            if (apiError.hasUniqueError()) {
                view.showError(statusCode, apiError.getUniqueError().getDetail());
            } else {
                String errorString = concatenateErrorString(apiError.getErrorList());
                view.showError(statusCode, errorString);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            view.toggleRegisterLoading(false);
            view.showError(500, "");
        }
    };

    public LoginRegisterPresenter(LoginUseCase loginUseCase, RegisterUseCase registerUseCase) {
        this.loginUseCase       = loginUseCase;
        this.registerUseCase    = registerUseCase;
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loginUseCase.registerListener(loginUseCaseListener);
        registerUseCase.registerListener(registerUseCaseListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        loginUseCase.unregisterListener(loginUseCaseListener);
        registerUseCase.unregisterListener(registerUseCaseListener);
    }

    // Commands

    public void onLoginCommand(String loginParam, String password) {
        loginUseCase.executeRequest(loginParam, password);
        view.toggleLoginLoading(true);
    }

    public void onRegisterCommand(String name, String username, String email, String password) {
        registerUseCase.executeRequest(name, username, email, password);
        view.toggleRegisterLoading(true);
    }

    // View Interface

    public interface View {
        void toggleLoginLoading(boolean showLoading);
        void toggleRegisterLoading(boolean showLoading);
        void showError(int code, String error);
        void showLoggedMessage(User user);
        void showRegisteredMessage(User user);
        void closeView();
    }
}
