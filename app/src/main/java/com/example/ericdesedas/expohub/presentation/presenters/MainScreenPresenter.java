package com.example.ericdesedas.expohub.presentation.presenters;

import android.util.Log;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.models.Session;
import com.example.ericdesedas.expohub.data.models.User;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairEventsUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairsUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleUserUseCase;

import java.io.IOException;
import java.util.List;

import moe.banana.jsonapi2.Document;

public class MainScreenPresenter extends Presenter {

    private GetFairsUseCase getFairsUseCase;
    private GetSingleUserUseCase getSingleUserUseCase;
    private GetFairEventsUseCase getFairEventsUseCase;
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
            } catch (IOException e) {
                sessionManager.logout();
                view.showUnidentifiedUser();
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

    private ApiUseCase.Listener<Document<FairEvent>> getTrendingEventsListener = new ApiUseCase.Listener<Document<FairEvent>>() {
        @Override
        public void onResponse(int statusCode, Document<FairEvent> result) {

            view.toggleFairEventsLoading(false);

            List<FairEvent> fairEventsList = generateArrayFromDocument(result);
            FairEvent[] fairEvents = fairEventsList.toArray(new FairEvent[fairEventsList.size()]);

            view.showTrendingFairEvents(fairEvents);
        }

        @Override
        public void onError(int statusCode, ApiErrorWrapper apiError) {
            view.toggleFairEventsLoading(false);
            if (apiError.hasUniqueError()) {
                view.showFairEventsError(statusCode, apiError.getUniqueError().getDetail());
            } else {
                String errorString = concatenateErrorString(apiError.getErrorList());
                view.showFairEventsError(statusCode, errorString);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            view.toggleFairEventsLoading(false);
            view.showFairEventsError(500, "");
        }
    };

    // ================================================== General ================================================== //

    /**
     * Constructor
     *
     * @param getFairsUseCase       the {@link GetFairsUseCase} reference
     * @param getSingleUserUseCase  the {@link GetSingleUserUseCase} reference
     * @param getFairEventsUseCase  the {@link GetFairEventsUseCase} reference
     * @param sessionManager        the {@link SessionManager} reference
     */
    public MainScreenPresenter(GetFairsUseCase getFairsUseCase, GetSingleUserUseCase getSingleUserUseCase,
                               GetFairEventsUseCase getFairEventsUseCase, SessionManager sessionManager) {

        this.getFairsUseCase        = getFairsUseCase;
        this.getSingleUserUseCase   = getSingleUserUseCase;
        this.getFairEventsUseCase   = getFairEventsUseCase;
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

        // Trending event formatting
        getFairEventsUseCase.addParameter("sort", "-attendance");
        getFairEventsUseCase.addParameter("include", "eventType");
        getFairEventsUseCase.addParameter("page[cursor]", "0");
        getFairEventsUseCase.addParameter("page[limit]", "7");
    }

    @Override
    public void onStart() {

        super.onStart();

        getFairsUseCase.registerListener(getFairUseCaseListener);
        getSingleUserUseCase.registerListener(getSingleUserUseCaseListener);
        getFairEventsUseCase.registerListener(getTrendingEventsListener);
    }

    @Override
    public void onStop() {

        super.onStop();

        getFairsUseCase.unregisterListener(getFairUseCaseListener);
        getSingleUserUseCase.unregisterListener(getSingleUserUseCaseListener);
        getFairEventsUseCase.registerListener(getTrendingEventsListener);
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

    public void onLoadTrendingEventsCommand() {
        view.toggleFairEventsLoading(true);
        getFairEventsUseCase.executeRequest();
    }

    /**
     * Refreshes logged user data
     */
    public void onRefreshUserDataCommand() {

        if (sessionManager.isLoggedIn()) {
            try {
                Session session = sessionManager.getLoggedUser();
                getSingleUserUseCase.executeRequest(session.id);

            } catch (IOException e) {
                sessionManager.logout();
                view.showUnidentifiedUser();
            }
        } else {
            view.showUnidentifiedUser();
        }
    }

    // =============================================== View Interface =============================================== //

    public interface View {
        void toggleLoading(boolean showLoading);
        void toggleFairEventsLoading(boolean showLoading);
        void updateFairList(Fair[] fairs);
        void showError(int code, String error);
        void showFairEventsError(int code, String error);
        void showUnidentifiedUser();
        void showIdentifiedUser(Session session);
        void showTrendingFairEvents(FairEvent[] fairEvents);
    }
}
