package com.example.ericdesedas.expohub.buildsystem.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.helpers.preferences.PreferenceHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreference() {
        return application.getSharedPreferences(application.getString(R.string.shared_pref_file), Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    PreferenceHelper providesPreferenceHelper(SharedPreferences sharedPreferences) {
        return new PreferenceHelper(sharedPreferences);
    }
}
