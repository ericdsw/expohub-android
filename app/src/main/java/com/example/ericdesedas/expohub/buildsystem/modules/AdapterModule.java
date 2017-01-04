package com.example.ericdesedas.expohub.buildsystem.modules;

import com.example.ericdesedas.expohub.helpers.image.ImageDownloader;
import com.example.ericdesedas.expohub.presentation.adapters.RecyclerAdapterFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class AdapterModule {

    @Provides
    RecyclerAdapterFactory providesRecyclerAdapterFactory(ImageDownloader imageDownloader) {
        return new RecyclerAdapterFactory(imageDownloader);
    }
}
