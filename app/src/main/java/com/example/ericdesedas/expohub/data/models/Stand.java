package com.example.ericdesedas.expohub.data.models;

import com.squareup.moshi.Json;

import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "stands")
public class Stand extends Resource {

    // Properties
    @Json(name = "name")        public String name;
    @Json(name = "description") public String description;
    @Json(name = "image")       public String image;

    // Relationships
    @Json(name = "fair")        public HasOne<Fair> fair;
}
