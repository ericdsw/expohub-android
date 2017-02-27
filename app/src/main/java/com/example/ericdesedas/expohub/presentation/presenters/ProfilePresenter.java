package com.example.ericdesedas.expohub.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.models.Session;
import com.example.ericdesedas.expohub.data.models.Unknown;
import com.example.ericdesedas.expohub.data.models.User;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairEventsByAttendingUserUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairsByUserUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleUserUseCase;
import com.example.ericdesedas.expohub.domain.interactors.LogoutUseCase;
import com.example.ericdesedas.expohub.helpers.preferences.PreferenceHelper;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import moe.banana.jsonapi2.Document;
import okhttp3.ResponseBody;

public class ProfilePresenter extends Presenter {

    private GetSingleUserUseCase getSingleUserUseCase;
    private GetFairsByUserUseCase getFairsByUserUseCase;
    private GetFairEventsByAttendingUserUseCase getFairEventsByAttendingUserUseCase;
    private LogoutUseCase logoutUseCase;

    private PreferenceHelper preferenceHelper;
    private SessionManager sessionManager;
    private View view;

    private ApiUseCase.Listener<Document<User>> singleUserListener = new ApiUseCase.Listener<Document<User>>() {
        @Override
        public void onResponse(int statusCode, Document<User> result) {

            view.toggleLoading(false);
            User user = result.get();

            try {
                sessionManager.refreshUserData(user);
                view.showUser(user);
            } catch (IOException e) {
                sessionManager.logout();
                view.closeForInvalidUser();
            }
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

    private ApiUseCase.Listener<Document<Fair>> fairsByUserUseCaseListener = new ApiUseCase.Listener<Document<Fair>>() {
        @Override
        public void onResponse(int statusCode, Document<Fair> result) {

            view.toggleFairLoading(false);

            List<Fair> fairList = generateArrayFromDocument(result);
            Fair[] fairs = fairList.toArray(new Fair[fairList.size()]);

            view.showUserFairs(fairs);
        }

        @Override
        public void onError(int statusCode, ApiErrorWrapper apiError) {

            view.toggleFairLoading(false);
            if (apiError.hasUniqueError()) {
                view.showError(statusCode, apiError.getUniqueError().getDetail());
            } else {
                String errorString = concatenateErrorString(apiError.getErrorList());
                view.showError(statusCode, errorString);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {

            view.toggleFairLoading(false);
            view.showError(500, "");
        }
    };

    private ApiUseCase.Listener<Document<FairEvent>> getFairEventsByAttendingUseCase = new ApiUseCase.Listener<Document<FairEvent>>() {
        @Override
        public void onResponse(int statusCode, Document<FairEvent> result) {

            view.toggleFairEventsLoading(false);

            List<FairEvent> fairEventList   = generateArrayFromDocument(result);
            FairEvent[] fairEvents          = fairEventList.toArray(new FairEvent[fairEventList.size()]);

            // Write attending to shared preferences
            List<String> eventIds = new ArrayList<>();
            for (FairEvent fairEvent : fairEvents) {
                eventIds.add(fairEvent.getId());
            }

            preferenceHelper.writeStringPref(PreferenceHelper.ATTENDING_FAIR_EVENTS, StringUtils.join(eventIds, ","));
            view.showAttendingFairEvents(fairEvents);
        }

        @Override
        public void onError(int statusCode, ApiErrorWrapper apiError) {

            view.toggleFairEventsLoading(false);
            if (apiError.hasUniqueError()) {
                view.showError(statusCode, apiError.getUniqueError().getDetail());
            } else {
                String errorString = concatenateErrorString(apiError.getErrorList());
                view.showError(statusCode, errorString);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {

            view.toggleFairEventsLoading(false);
            view.showError(500, "");
        }
    };

    private ApiUseCase.Listener<ResponseBody> logoutUseCaseListener = new ApiUseCase.Listener<ResponseBody>() {
        @Override
        public void onResponse(int statusCode, ResponseBody result) {
            view.toggleLoggingOutLoading(false);
            sessionManager.logout();
            view.loggedOut();
        }

        @Override
        public void onError(int statusCode, ApiErrorWrapper apiError) {

            view.toggleLoggingOutLoading(false);
            sessionManager.logout();
            view.loggedOut();
        }

        @Override
        public void onFailure(Throwable throwable) {

            view.toggleLoggingOutLoading(false);
            sessionManager.logout();
            view.loggedOut();
        }
    };

    public ProfilePresenter(GetSingleUserUseCase getSingleUserUseCase,
                            GetFairsByUserUseCase getFairsByUserUseCase,
                            GetFairEventsByAttendingUserUseCase fairEventsByAttendingUserUseCase,
                            PreferenceHelper preferenceHelper,
                            LogoutUseCase logoutUseCase,
                            SessionManager sessionManager) {

        this.getSingleUserUseCase                   = getSingleUserUseCase;
        this.getFairsByUserUseCase                  = getFairsByUserUseCase;
        this.getFairEventsByAttendingUserUseCase    = fairEventsByAttendingUserUseCase;
        this.preferenceHelper                       = preferenceHelper;
        this.logoutUseCase                          = logoutUseCase;
        this.sessionManager                         = sessionManager;
        this.view                                   = null;

        getFairEventsByAttendingUserUseCase.addParameter("include", "eventType");
    }

    @Override
    public void onStart() {
        super.onStart();
        getSingleUserUseCase.registerListener(singleUserListener);
        getFairsByUserUseCase.registerListener(fairsByUserUseCaseListener);
        getFairEventsByAttendingUserUseCase.registerListener(getFairEventsByAttendingUseCase);
        logoutUseCase.registerListener(logoutUseCaseListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        getSingleUserUseCase.unregisterListener(singleUserListener);
        getFairsByUserUseCase.unregisterListener(fairsByUserUseCaseListener);
        getFairEventsByAttendingUserUseCase.unregisterListener(getFairEventsByAttendingUseCase);
        logoutUseCase.unregisterListener(logoutUseCaseListener);
    }

    public void setView(View view) {
        this.view = view;
    }

    // Commands

    public void onFetchUserDataCommand() {

        try {
            Session session = sessionManager.getLoggedUser();
            getSingleUserUseCase.executeRequest(session.id);
            view.toggleLoading(true);
            view.toggleFairLoading(false);
            view.toggleFairEventsLoading(false);
        } catch (IOException e) {
            sessionManager.logout();
            view.closeForInvalidUser();
        }
    }

    public void onFetchUserFairsCommand() {

        try {
            Session session = sessionManager.getLoggedUser();
            getFairsByUserUseCase.executeRequest(session.id);
            view.toggleFairLoading(true);
        } catch (IOException e) {
            sessionManager.logout();
            view.closeForInvalidUser();
        }
    }

    public void onFetchUserAttendingFairEventsCommand() {

        try {
            Session session = sessionManager.getLoggedUser();
            getFairEventsByAttendingUserUseCase.executeRequest(session.id);
            view.toggleFairEventsLoading(true);
        } catch (IOException e) {
            sessionManager.logout();
            view.closeForInvalidUser();
        }
    }

    public void onLogoutCommand() {
        logoutUseCase.executeRequest();
        view.toggleLoggingOutLoading(true);
    }

    public interface View {
        void toggleLoading(boolean showLoading);
        void toggleFairLoading(boolean showLoading);
        void toggleFairEventsLoading(boolean showLoading);
        void showError(int statusCode, String error);
        void showUser(User user);
        void showUserFairs(Fair[] fairs);
        void showAttendingFairEvents(FairEvent[] fairEvents);
        void closeForInvalidUser();
        void toggleLoggingOutLoading(boolean showLoading);
        void loggedOut();
    }
}