package com.example.ericdesedas.expohub.buildsystem.modules;

import com.example.ericdesedas.expohub.buildsystem.scopes.PerActivity;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.example.ericdesedas.expohub.domain.interactors.GetEventsByFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairsUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetNewsByFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleUserUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetStandsByFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.LoginUseCase;
import com.example.ericdesedas.expohub.domain.interactors.RegisterUseCase;
import com.example.ericdesedas.expohub.presentation.presenters.EventsByFairPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.FairDetailsPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.LoginRegisterPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.MainScreenPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.NewsByFairPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.StandsByFairPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    @PerActivity
    MainScreenPresenter providesMainScreenPresenter(GetFairsUseCase useCase, GetSingleUserUseCase userUseCase, SessionManager sessionManager) {
        return new MainScreenPresenter(useCase, userUseCase, sessionManager);
    }

    @Provides
    @PerActivity
    FairDetailsPresenter providesFairDetailPresenter(GetSingleFairUseCase useCase) {
        return new FairDetailsPresenter(useCase);
    }

    @Provides
    @PerActivity
    EventsByFairPresenter providesEventsByFairPresenter(GetEventsByFairUseCase useCase) {
        return new EventsByFairPresenter(useCase);
    }

    @Provides
    @PerActivity
    NewsByFairPresenter providesNewsByFairPresenter(GetNewsByFairUseCase useCase) {
        return new NewsByFairPresenter(useCase);
    }

    @Provides
    @PerActivity
    StandsByFairPresenter providesStandsByFairPresenter(GetStandsByFairUseCase useCase) {
        return new StandsByFairPresenter(useCase);
    }

    @Provides
    @PerActivity
    LoginRegisterPresenter providesLoginRegisterPresenter(LoginUseCase loginUseCase, RegisterUseCase registerUseCase) {
        return new LoginRegisterPresenter(loginUseCase, registerUseCase);
    }
}
