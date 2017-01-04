package com.example.ericdesedas.expohub;

import android.app.Application;

import com.example.ericdesedas.expohub.buildsystem.components.ApplicationComponent;
import com.example.ericdesedas.expohub.buildsystem.components.DaggerApplicationComponent;
import com.example.ericdesedas.expohub.buildsystem.modules.AppModule;
import com.example.ericdesedas.expohub.buildsystem.modules.NetworkModule;

public class App extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {

        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(this))
                .build();
    }

    /**
     * Returns the internal app component instance
     * @return the {@link ApplicationComponent} reference
     */
    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
