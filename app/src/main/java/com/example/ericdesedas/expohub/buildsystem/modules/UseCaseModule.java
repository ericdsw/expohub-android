package com.example.ericdesedas.expohub.buildsystem.modules;

import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.example.ericdesedas.expohub.domain.interactors.GetEventsByFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairsUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetNewsByFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleUserUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetStandsByFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.LoginUseCase;
import com.example.ericdesedas.expohub.domain.interactors.RegisterUseCase;
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
    GetSingleUserUseCase providesSingleUserUseCase(ApiClient apiClient, Moshi moshi) {
        return new GetSingleUserUseCase(apiClient, moshi);
    }
}
