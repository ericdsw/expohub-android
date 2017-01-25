package com.example.ericdesedas.expohub.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.ericdesedas.expohub.App;
import com.example.ericdesedas.expohub.buildsystem.components.ActivityComponent;
import com.example.ericdesedas.expohub.buildsystem.components.ApplicationComponent;
import com.example.ericdesedas.expohub.buildsystem.components.DaggerActivityComponent;
import com.example.ericdesedas.expohub.buildsystem.modules.ActivityModule;
import com.example.ericdesedas.expohub.buildsystem.modules.NavigationModule;
import com.example.ericdesedas.expohub.buildsystem.modules.PresenterModule;
import com.example.ericdesedas.expohub.buildsystem.modules.UseCaseModule;

import icepick.Icepick;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Instantiates activity component
        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .navigationModule(new NavigationModule())
                .presenterModule(new PresenterModule())
                .useCaseModule(new UseCaseModule())
                .build();

        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    /**
     * Gets internal ApplicationComponent
     * @return the {@link ApplicationComponent} reference
     */
    protected ApplicationComponent getApplicationComponent() {
        App app = (App) getApplication();
        return app.getApplicationComponent();
    }

    /**
     * Gets internal ActivityComponent
     * @return the {@link ActivityComponent} reference
     */
    protected ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}
