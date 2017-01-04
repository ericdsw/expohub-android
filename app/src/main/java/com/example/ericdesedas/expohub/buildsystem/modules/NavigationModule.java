package com.example.ericdesedas.expohub.buildsystem.modules;

import android.app.Activity;

import com.example.ericdesedas.expohub.buildsystem.scopes.PerActivity;
import com.example.ericdesedas.expohub.presentation.navigation.Navigator;

import dagger.Module;
import dagger.Provides;

@Module
public class NavigationModule {

    @Provides
    @PerActivity
    Navigator providesNavigator(Activity activity) {
        return new Navigator(activity);
    }
}
