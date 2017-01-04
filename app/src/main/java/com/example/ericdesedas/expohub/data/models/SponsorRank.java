package com.example.ericdesedas.expohub.data.models;

import com.squareup.moshi.Json;

import moe.banana.jsonapi2.HasMany;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "sponsorRanks")
public class SponsorRank extends Resource {

    // Properties
    @Json(name = "name")    public String name;

    // Relationships
    @Json(name = "sponsors") public HasMany<Sponsor> sponsors;
}
