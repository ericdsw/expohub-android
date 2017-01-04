package com.example.ericdesedas.expohub.buildsystem.components;

import com.example.ericdesedas.expohub.buildsystem.modules.AppModule;
import com.example.ericdesedas.expohub.buildsystem.modules.EventModule;
import com.example.ericdesedas.expohub.buildsystem.modules.ImageDownloaderModule;
import com.example.ericdesedas.expohub.buildsystem.modules.NetworkModule;
import com.example.ericdesedas.expohub.data.network.ApiClient;
import com.example.ericdesedas.expohub.helpers.image.ImageDownloader;
import com.squareup.moshi.Moshi;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class, NetworkModule.class, ImageDownloaderModule.class, EventModule.class
        }
)
public interface ApplicationComponent {

    // Exported for child components
    ApiClient apiClient();
    ImageDownloader imageDownloader();
    Moshi moshi();
    EventBus eventBus();
}
