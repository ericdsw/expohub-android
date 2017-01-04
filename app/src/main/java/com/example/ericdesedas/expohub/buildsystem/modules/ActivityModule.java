package com.example.ericdesedas.expohub.buildsystem.modules;

import android.app.Activity;

import com.example.ericdesedas.expohub.buildsystem.scopes.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    Activity providesActivity() {
        return activity;
    }
}
