package com.example.ericdesedas.expohub.buildsystem.modules;

import com.example.ericdesedas.expohub.buildsystem.scopes.PerActivity;
import com.example.ericdesedas.expohub.domain.interactors.GetEventsByFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetFairsUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetNewsByFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetSingleFairUseCase;
import com.example.ericdesedas.expohub.domain.interactors.GetStandsByFairUseCase;
import com.example.ericdesedas.expohub.presentation.presenters.EventsByFairPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.FairDetailsPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.MainScreenPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.NewsByFairPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.StandsByFairPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    @PerActivity
    MainScreenPresenter providesMainScreenPresenter(GetFairsUseCase useCase) {
        return new MainScreenPresenter(useCase);
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
}
