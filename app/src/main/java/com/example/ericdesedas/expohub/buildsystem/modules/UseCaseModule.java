package com.example.ericdesedas.expohub.buildsystem.modules;

import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.example.ericdesedas.expohub.domain.interactors.AttendFairEventUseCase;
import com.example.ericdesedas.expohub.domain.interactors.CreateCommentUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetEventsByFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairEventsByAttendingUserUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairEventsUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairsByUserUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairsUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetNewsByFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleFairEventUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleNewsUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleUserUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSponsorsByFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetStandsByFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.LoginUseCase;
import com.example.ericdesedas.expohub.domain.interactors.LogoutUseCase;
import com.example.ericdesedas.expohub.domain.interactors.RegisterUseCase;
import com.example.ericdesedas.expohub.domain.interactors.UnAttendFairEventUseCase;
import com.squareup.moshi.Moshi;

import dagger.Module;
import dagger.Provides;

@Module
public class UseCaseModule {

    @Provides
    GetFairsUseCase providesGetFairsUseCase(ApiClient apiClient, Moshi moshi) {
        return new GetFairsUseCase(apiClient, moshi);
    }

    @Provides
    GetSingleFairUseCase providesGetSingleFairUseCase(ApiClient apiClient, Moshi moshi) {
        return new GetSingleFairUseCase(apiClient, moshi);
    }

    @Provides
    GetFairEventsUseCase providesGetFairEventsUseCase(ApiClient apiClient, Moshi moshi) {
        return new GetFairEventsUseCase(apiClient, moshi);
    }

    @Provides
    GetEventsByFairUseCase providesGetEventsByFairUseCase(ApiClient apiClient, Moshi moshi) {
        return new GetEventsByFairUseCase(apiClient, moshi);
    }

    @Provides
    GetNewsByFairUseCase providesGetNewsByFairUseCase(ApiClient apiClient, Moshi moshi) {
        return new GetNewsByFairUseCase(apiClient, moshi);
    }

    @Provides
    GetStandsByFairUseCase providesGetStandsByFairUseCase(ApiClient apiClient, Moshi moshi) {
        return new GetStandsByFairUseCase(apiClient, moshi);
    }

    @Provides
    LoginUseCase providesLoginUseCase(ApiClient apiClient, Moshi moshi, SessionManager sessionManager) {
        return new LoginUseCase(apiClient, moshi, sessionManager);
    }

    @Provides
    RegisterUseCase providesRegisterUseCase(ApiClient apiClient, Moshi moshi, SessionManager sessionManager) {
        return new RegisterUseCase(apiClient, moshi, sessionManager);
    }

    @Provides
    LogoutUseCase providesLogoutUseCase(ApiClient apiClient, Moshi moshi, SessionManager sessionManager) {
        return new LogoutUseCase(apiClient, moshi, sessionManager);
    }

    @Provides
    GetSingleUserUseCase providesSingleUserUseCase(ApiClient apiClient, Moshi moshi) {
        return new GetSingleUserUseCase(apiClient, moshi);
    }

    @Provides
    GetFairsByUserUseCase providesGetFairsByUserUseCase(ApiClient apiClient, Moshi moshi) {
        return new GetFairsByUserUseCase(apiClient, moshi);
    }

    @Provides
    GetFairEventsByAttendingUserUseCase providesGetFairEventsByAttendingUserUseCase(ApiClient apiClient, Moshi moshi) {
        return new GetFairEventsByAttendingUserUseCase(apiClient, moshi);
    }

    @Provides
    GetSponsorsByFairUseCase providesSponsorByFairUseCase(ApiClient apiClient, Moshi moshi) {
        return new GetSponsorsByFairUseCase(apiClient, moshi);
    }

    @Provides
    GetSingleFairEventUseCase providesSingleFairEventUseCase(ApiClient apiClient, Moshi moshi) {
        return new GetSingleFairEventUseCase(apiClient, moshi);
    }

    @Provides
    AttendFairEventUseCase providesAttendFairEventUseCase(ApiClient apiClient, Moshi moshi) {
        return new AttendFairEventUseCase(apiClient, moshi);
    }

    @Provides
    UnAttendFairEventUseCase providesUnAttendFairEventUseCase(ApiClient apiClient, Moshi moshi) {
        return new UnAttendFairEventUseCase(apiClient, moshi);
    }

    @Provides
    GetSingleNewsUseCase providesGetSingleNewsUseCase(ApiClient apiClient, Moshi moshi) {
        return new GetSingleNewsUseCase(apiClient, moshi);
    }

    @Provides
    CreateCommentUseCase providesCreateCommentUseCase(ApiClient apiClient, Moshi moshi) {
        return new CreateCommentUseCase(apiClient, moshi);
    }
}
