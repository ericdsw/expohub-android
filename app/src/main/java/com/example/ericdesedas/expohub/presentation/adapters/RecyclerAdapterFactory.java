package com.example.ericdesedas.expohub.presentation.adapters;

import com.example.ericdesedas.expohub.helpers.image.ImageDownloader;

public class RecyclerAdapterFactory {

    private ImageDownloader imageDownloader;

    public RecyclerAdapterFactory(ImageDownloader imageDownloader) {
        this.imageDownloader = imageDownloader;
    }

    public FairListAdapter createFairListAdapter() {
        return new FairListAdapter(imageDownloader);
    }

    public EventListAdapter createEventListAdapter() {
        return new EventListAdapter(imageDownloader);
    }

    public NewsListAdapter createNewsListAdapter() {
        return new NewsListAdapter(imageDownloader);
    }

    public StandListAdapter createStandListAdapter() {
        return new StandListAdapter(imageDownloader);
    }
}
