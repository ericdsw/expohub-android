package com.example.ericdesedas.expohub.data.network.factories;


import com.example.ericdesedas.expohub.data.models.Category;
import com.example.ericdesedas.expohub.data.models.Comment;
import com.example.ericdesedas.expohub.data.models.EventType;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.models.Map;
import com.example.ericdesedas.expohub.data.models.News;
import com.example.ericdesedas.expohub.data.models.Speaker;
import com.example.ericdesedas.expohub.data.models.Sponsor;
import com.example.ericdesedas.expohub.data.models.SponsorRank;
import com.example.ericdesedas.expohub.data.models.Stand;
import com.example.ericdesedas.expohub.data.models.User;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import javax.inject.Inject;

import moe.banana.jsonapi2.ResourceAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MoshiFactory {

    private Moshi.Builder moshiBuilder;

    @Inject
    public MoshiFactory(Moshi.Builder moshiBuilder) {
        this.moshiBuilder = moshiBuilder;
    }

    public MoshiConverterFactory create() {

        JsonAdapter.Factory factory = ResourceAdapterFactory.builder()
                .add(Category.class)
                .add(Comment.class)
                .add(EventType.class)
                .add(Fair.class)
                .add(FairEvent.class)
                .add(Map.class)
                .add(News.class)
                .add(Speaker.class)
                .add(Sponsor.class)
                .add(SponsorRank.class)
                .add(Stand.class)
                .add(User.class)
                .strict()
                .build();

        Moshi moshi = moshiBuilder.add(factory)
                .build();

        return MoshiConverterFactory.create(moshi);
    }
}
