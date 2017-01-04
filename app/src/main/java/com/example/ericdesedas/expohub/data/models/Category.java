package com.example.ericdesedas.expohub.data.models;

import com.squareup.moshi.Json;

import moe.banana.jsonapi2.HasMany;
import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "categories")
public class Category extends Resource {

    // Properties
    @Json(name = "name")   public String name;

    // Relationships
    @Json(name = "fair")        public HasOne<Fair> fair;
    @Json(name = "fairEvents")  public HasMany<FairEvent> fairEvents;
}
