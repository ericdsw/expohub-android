package com.example.ericdesedas.expohub.data.models;

import com.squareup.moshi.Json;

import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "speakers")
public class Speaker extends Resource {

    // Properties
    @Json(name = "name")        public String name;
    @Json(name = "picture")     public String picture;
    @Json(name = "description") public String description;

    // Relationships
    @Json(name = "fairEvent")   public HasOne<FairEvent> fairEvent;

    public FairEvent getFairEvent() {
        return fairEvent.get(getContext());
    }
}
