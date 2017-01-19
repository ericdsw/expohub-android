package com.example.ericdesedas.expohub.presentation.presenters;

import android.util.Log;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.models.Session;
import com.example.ericdesedas.expohub.data.models.User;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairsUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleUserUseCase;

import java.io.IOException;
import java.util.List;

import moe.banana.jsonapi2.Document;

public class MainScreenPresenter extends Presenter {

    private GetFairsUseCase getFairsUseCase;
    private GetSingleUserUseCase getSingleUserUseCase;
    private SessionManager sessionManager;
    private View view;

    // ================================================= Listeners ================================================= //

    private ApiUseCase.Listener<Document<Fair>> getFairUseCaseListener = new ApiUseCase.Listener<Document<Fair>>() {
        @Override
        public void onResponse(int statusCode, Document<Fair> result) {

            List<Fair> fairsList = generateArrayFromDocument(result);
            Fair[] fairsArray = fairsList.toArray(new Fair[fairsList.size()]);

            view.toggleLoading(false);
            view.updateFairList(fairsArray);
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

    private ApiUseCase.Listener<Document<User>> getSingleUserUseCaseListener = new ApiUseCase.Listener<Document<User>>() {
        @Override
        public void onResponse(int statusCode, Document<User> result) {

            User user = result.get();
            try {
                sessionManager.refreshUserData(user);
                view.showIdentifiedUser(sessionManager.getLoggedUser());
                Log.d("MainScreenPresenter", "updated user data");
            } catch (IOException e) {
                sessionManager.logout();
                view.showUnidentifiedUser();
                Log.d("MainScreenPresenter", "could not update user data");
            }
        }

        @Override
        public void onError(int statusCode, ApiErrorWrapper apiError) {
            Log.d("MainScreenPresenter", "error on user request: " + apiError.getUniqueError().getDetail());
        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.d("MainScreenPresenter", "request crashed");
        }
    };

    // ================================================== General ================================================== //

    /**
     * Constructor
     *
     * @param getFairsUseCase       the {@link GetFairsUseCase} reference
     * @param getSingleUserUseCase  the {@link GetSingleUserUseCase} reference
     * @param sessionManager        the {@link SessionManager} reference
     */
    public MainScreenPresenter(GetFairsUseCase getFairsUseCase, GetSingleUserUseCase getSingleUserUseCase, SessionManager sessionManager) {

        this.getFairsUseCase        = getFairsUseCase;
        this.getSingleUserUseCase   = getSingleUserUseCase;
        this.sessionManager         = sessionManager;
        this.view                   = null;
    }

    /**
     * Initialization Logic
     */
    public void initialize() {

        if (sessionManager.isLoggedIn()) {
            try {
                view.showIdentifiedUser(sessionManager.getLoggedUser());
            } catch (IOException e) {
                sessionManager.logout();
                view.showUnidentifiedUser();
            }
        } else {
            view.showUnidentifiedUser();
        }
    }

    @Override
    public void onStart() {

        super.onStart();

        getFairsUseCase.registerListener(getFairUseCaseListener);
        getSingleUserUseCase.registerListener(getSingleUserUseCaseListener);
    }

    @Override
    public void onStop() {

        super.onStop();

        getFairsUseCase.unregisterListener(getFairUseCaseListener);
        getSingleUserUseCase.unregisterListener(getSingleUserUseCaseListener);
    }

    /**
     * Register the presenter's view
     * @param view the {@link View} that's going to be registered
     */
    public void setView(View view) {
        this.view = view;
    }

    // ================================================== Commands ================================================== //

    /**
     * Loads fairs
     */
    public void onLoadFairsCommand() {
        view.toggleLoading(true);
        getFairsUseCase.executeRequest();
    }

    /**
     * Refreshes logged user data
     */
    public void onRefreshUserDataCommand() {

        Log.d("MainScreenPresenter", "called onRefreshUserCommand");

        if (sessionManager.isLoggedIn()) {

            Log.d("MainScreenPresenter", "user was logged in");

            try {
                Session session = sessionManager.getLoggedUser();
                getSingleUserUseCase.executeRequest(session.id);

                Log.d("MainScreenPresenter", "executed the use case");

            } catch (IOException e) {

                Log.d("MainScreenPresenter", "got exception");

                sessionManager.logout();
                view.showUnidentifiedUser();
            }
        } else {

            Log.d("MainScreenPresenter", "user was NOT logged in");

            view.showUnidentifiedUser();
        }
    }

    // =============================================== View Interface =============================================== //

    public interface View {
        void toggleLoading(boolean showLoading);
        void updateFairList(Fair[] fairs);
        void showError(int code, String error);
        void showUnidentifiedUser();
        void showIdentifiedUser(Session session);
    }
}
