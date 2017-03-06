package com.example.ericdesedas.expohub.buildsystem.modules;

import com.example.ericdesedas.expohub.buildsystem.scopes.PerActivity;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.example.ericdesedas.expohub.domain.interactors.AttendFairEventUseCase;
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
import com.example.ericdesedas.expohub.helpers.preferences.PreferenceHelper;
import com.example.ericdesedas.expohub.presentation.presenters.EventsByFairPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.FairDetailsPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.FairEventDetailsPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.LoginRegisterPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.MainScreenPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.NewsByFairPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.NewsDetailPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.ProfilePresenter;
import com.example.ericdesedas.expohub.presentation.presenters.SponsorsByFairPresenter;
import com.example.ericdesedas.expohub.presentation.presenters.StandsByFairPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    @PerActivity
    MainScreenPresenter providesMainScreenPresenter(GetFairsUseCase useCase,
                                                    GetSingleUserUseCase userUseCase,
                                                    GetFairEventsUseCase getFairEventsUseCase,
                                                    PreferenceHelper preferenceHelper,
                                                    SessionManager sessionManager) {

        return new MainScreenPresenter(useCase, userUseCase, getFairEventsUseCase, preferenceHelper, sessionManager);
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

    @Provides
    @PerActivity
    ProfilePresenter providesProfilePresenter(GetSingleUserUseCase userUseCase,
                                              GetFairsByUserUseCase fairsByUserUseCase,
                                              GetFairEventsByAttendingUserUseCase getFairEventsByAttendingUserUseCase,
                                              PreferenceHelper preferenceHelper,
                                              LogoutUseCase logoutUseCase,
                                              SessionManager sessionManager) {

        return new ProfilePresenter(userUseCase, fairsByUserUseCase, getFairEventsByAttendingUserUseCase,
                preferenceHelper, logoutUseCase, sessionManager);
    }

    @Provides
    @PerActivity
    SponsorsByFairPresenter providesSponsorsByFairPresenter(GetSponsorsByFairUseCase useCase) {
        return new SponsorsByFairPresenter(useCase);
    }

    @Provides
    @PerActivity
    FairEventDetailsPresenter providesFairEventDetailsPresenter(GetSingleFairEventUseCase singleFairEventUseCase,
                                                                AttendFairEventUseCase attendFairEventUseCase,
                                                                UnAttendFairEventUseCase unAttendFairEventUseCase,
                                                                PreferenceHelper preferenceHelper,
                                                                SessionManager sessionManager) {

        return new FairEventDetailsPresenter(singleFairEventUseCase, attendFairEventUseCase, unAttendFairEventUseCase,
                                             preferenceHelper, sessionManager);
    }

    @Provides
    @PerActivity
    NewsDetailPresenter providesNewsDetailPresenter(GetSingleNewsUseCase useCase) {
        return new NewsDetailPresenter(useCase);
    }
}
