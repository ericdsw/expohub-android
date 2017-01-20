package com.example.ericdesedas.expohub.buildsystem.components;

import com.example.ericdesedas.expohub.buildsystem.modules.ActivityModule;
import com.example.ericdesedas.expohub.buildsystem.modules.AdapterModule;
import com.example.ericdesedas.expohub.buildsystem.modules.NavigationModule;
import com.example.ericdesedas.expohub.buildsystem.modules.PresenterModule;
import com.example.ericdesedas.expohub.buildsystem.modules.UseCaseModule;
import com.example.ericdesedas.expohub.buildsystem.scopes.PerActivity;
import com.example.ericdesedas.expohub.presentation.activities.EventsByFairActivity;
import com.example.ericdesedas.expohub.presentation.activities.FairDetailsActivity;
import com.example.ericdesedas.expohub.presentation.activities.LoginRegisterActivity;
import com.example.ericdesedas.expohub.presentation.activities.MainActivity;
import com.example.ericdesedas.expohub.presentation.activities.NewsByFairActivity;
import com.example.ericdesedas.expohub.presentation.activities.ProfileActivity;
import com.example.ericdesedas.expohub.presentation.activities.StandsByFairActivity;

import dagger.Component;

@PerActivity
@Component(
        dependencies    = { ApplicationComponent.class },
        modules         = {
                NavigationModule.class, PresenterModule.class, UseCaseModule.class, ActivityModule.class, AdapterModule.class
        }
)
public interface ActivityComponent {

    // Injection targets
    void inject(MainActivity activity);
    void inject(FairDetailsActivity activity);
    void inject(EventsByFairActivity activity);
    void inject(NewsByFairActivity activity);
    void inject(StandsByFairActivity activity);
    void inject(LoginRegisterActivity activity);
    void inject(ProfileActivity activity);
}
