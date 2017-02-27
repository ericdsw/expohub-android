package com.example.ericdesedas.expohub.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.example.ericdesedas.expohub.domain.interactors.ApiUseCase;
import com.example.ericdesedas.expohub.domain.interactors.AttendFairEventUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleFairEventUseCase;
import com.example.ericdesedas.expohub.domain.interactors.UnAttendFairEventUseCase;
import com.example.ericdesedas.expohub.helpers.preferences.PreferenceHelper;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import moe.banana.jsonapi2.Document;
import okhttp3.ResponseBody;

public class FairEventDetailsPresenter extends Presenter {

    private GetSingleFairEventUseCase singleFairEventUseCase;
    private AttendFairEventUseCase attendUseCase;
    private UnAttendFairEventUseCase unAttendUseCase;
    private SessionManager sessionManager;
    private PreferenceHelper preferenceHelper;
    private View view;

    private FairEvent lastFairEvent;

    private ApiUseCase.Listener<Document<FairEvent>> singleFairEventListener = new ApiUseCase.Listener<Document<FairEvent>>() {
        @Override
        public void onResponse(int statusCode, Document<FairEvent> result) {
            view.toggleLoading(false);
            view.updateFairEvent(result.get());
            lastFairEvent = result.get();
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

    private ApiUseCase.Listener<ResponseBody> attendListener = new ApiUseCase.Listener<ResponseBody>() {
        @Override
        public void onResponse(int statusCode, ResponseBody result) {

            String favoritesString      = preferenceHelper.readStringPref(PreferenceHelper.ATTENDING_FAIR_EVENTS);
            List<String> favoritesList  = new ArrayList(Arrays.asList(favoritesString.split(",")));

            favoritesList.add(lastFairEvent.getId());
            String newFavoritesString = StringUtils.join(favoritesList, ",");
            preferenceHelper.writeStringPref(PreferenceHelper.ATTENDING_FAIR_EVENTS, newFavoritesString);
        }

        @Override
        public void onError(int statusCode, ApiErrorWrapper apiError) {

        }

        @Override
        public void onFailure(Throwable throwable) {

        }
    };

    private ApiUseCase.Listener<ResponseBody> unAttendListener = new ApiUseCase.Listener<ResponseBody>() {
        @Override
        public void onResponse(int statusCode, ResponseBody result) {

            String favoritesString      = preferenceHelper.readStringPref(PreferenceHelper.ATTENDING_FAIR_EVENTS);
            List<String> favoritesList  = new ArrayList(Arrays.asList(favoritesString.split(",")));

            if (favoritesList.contains(lastFairEvent.getId())) {
                favoritesList.remove(lastFairEvent.getId());
            }

            String newFavoritesString = StringUtils.join(favoritesList, ",");
            preferenceHelper.writeStringPref(PreferenceHelper.ATTENDING_FAIR_EVENTS, newFavoritesString);
        }

        @Override
        public void onError(int statusCode, ApiErrorWrapper apiError) {

        }

        @Override
        public void onFailure(Throwable throwable) {

        }
    };

    public FairEventDetailsPresenter(GetSingleFairEventUseCase useCase,
                                     AttendFairEventUseCase attendUseCase,
                                     UnAttendFairEventUseCase unAttendUseCase,
                                     PreferenceHelper preferenceHelper,
                                     SessionManager sessionManager) {

        this.singleFairEventUseCase = useCase;
        this.attendUseCase          = attendUseCase;
        this.unAttendUseCase        = unAttendUseCase;
        this.preferenceHelper       = preferenceHelper;
        this.sessionManager         = sessionManager;
    }

    public void initialize() {
        singleFairEventUseCase.addParameter("include", "speakers");
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        super.onStart();
        singleFairEventUseCase.registerListener(singleFairEventListener);
        attendUseCase.registerListener(attendListener);
        unAttendUseCase.registerListener(unAttendListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        singleFairEventUseCase.unregisterListener(singleFairEventListener);
        attendUseCase.unregisterListener(attendListener);
        unAttendUseCase.unregisterListener(unAttendListener);
    }

    // Commands

    public void onLoadFairEventCommand(String fairEventId) {
        view.toggleLoading(true);
        singleFairEventUseCase.executeRequest(fairEventId);
    }

    public void onAttendFairEventCommand(String fairEventId) {
        if (sessionManager.isLoggedIn()) {
            attendUseCase.executeRequest(fairEventId);
            view.showAttending(true);
        } else {
            view.showUnidentifiedError();
        }
    }

    public void onUnAttendFairEventCommand(String fairEventId) {
        if (sessionManager.isLoggedIn()) {
            unAttendUseCase.executeRequest(fairEventId);
            view.showAttending(false);
        } else {
            view.showUnidentifiedError();
        }
    }

    public boolean onCheckForIsFavorite(String fairEventId) {

        String favoritesString = preferenceHelper.readStringPref(PreferenceHelper.ATTENDING_FAIR_EVENTS);
        return Arrays.asList(favoritesString.split(",")).contains(fairEventId);
    }

    public interface View {
        void toggleLoading(boolean showLoading);
        void updateFairEvent(FairEvent fairEvent);
        void showError(int code, String error);
        void showUnidentifiedError();
        void showAttending(boolean isAttending);
    }
}
