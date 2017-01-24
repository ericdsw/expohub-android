package com.example.ericdesedas.expohub.presentation.adapters;

import com.example.ericdesedas.expohub.helpers.image.ImageDownloader;

/**
 * RecyclerAdapterFactory class
 *
 * This helper is used to simplify the adapter's injection process
 */
public class RecyclerAdapterFactory {

    private ImageDownloader imageDownloader;

    /**
     * Constructor
     *
     * @param imageDownloader an {@link ImageDownloader} reference
     */
    public RecyclerAdapterFactory(ImageDownloader imageDownloader) {
        this.imageDownloader = imageDownloader;
    }

    /**
     * Creates FairListAdapter
     *
     * @return a {@link FairListAdapter} instance
     */
    public FairListAdapter createFairListAdapter() {
        return new FairListAdapter(imageDownloader);
    }

    /**
     * Creates EventListAdapter
     *
     * @return a {@link EventListAdapter} instance
     */
    public EventListAdapter createEventListAdapter() {
        return new EventListAdapter(imageDownloader);
    }

    /**
     * Creates NewsListAdapter
     *
     * @return a {@link NewsListAdapter} instance
     */
    public NewsListAdapter createNewsListAdapter() {
        return new NewsListAdapter(imageDownloader);
    }

    /**
     * Creates StandListAdapter
     *
     * @return a {@link StandListAdapter} instance
     */
    public StandListAdapter createStandListAdapter() {
        return new StandListAdapter(imageDownloader);
    }

    /**
     * Creates SponsorListAdapter
     *
     * @return a {@link SponsorListAdapter} instance
     */
    public SponsorListAdapter createSponsorListAdapter() {
        return new SponsorListAdapter(imageDownloader);
    }
}
