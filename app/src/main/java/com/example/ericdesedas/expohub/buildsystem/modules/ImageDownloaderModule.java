package com.example.ericdesedas.expohub.buildsystem.modules;

import android.content.Context;

import com.example.ericdesedas.expohub.helpers.image.ImageDownloader;
import com.example.ericdesedas.expohub.helpers.image.PicassoImageDownloader;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ImageDownloaderModule {

    @Provides
    @Singleton
    ImageDownloader providesImageDownloader(Context context) {
        return new PicassoImageDownloader(context);
    }
}
