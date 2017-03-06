package com.example.ericdesedas.expohub.data.models;

import com.squareup.moshi.Json;

import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "sponsors")
public class Sponsor extends Resource {

    // Properties
    @Json(name = "name")        public String name;
    @Json(name = "logo")        public String logo;
    @Json(name = "slogan")      public String slogan;
    @Json(name = "website")     public String website;

    // Relationships
    @Json(name = "fair")        public HasOne<Fair> fair;
    @Json(name = "sponsorRank") public HasOne<SponsorRank> sponsorRank;

    public Fair getFair() {
        return fair.get(getContext());
    }

    public SponsorRank getSponsorRank() {
        return sponsorRank.get(getContext());
    }
}
