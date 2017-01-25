package com.example.ericdesedas.expohub.data.models;

import com.squareup.moshi.Json;

import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "maps")
public class Map extends Resource {

    // Properties
    @Json(name = "name")    public String name;
    @Json(name = "image")   public String image;

    // Relationships
    @Json(name = "fair")    public HasOne<Fair> fair;

    public Fair getFair() {
        return fair.get(getContext());
    }
}
