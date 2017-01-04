package com.example.ericdesedas.expohub.data.models;

import com.squareup.moshi.Json;

import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "comments")
public class Comment extends Resource {

    // Properties
    @Json(name = "text")    public String text;

    // Relationships
    @Json(name = "ownerNews")   public HasOne<News> ownerNews;
    @Json(name = "user")        public HasOne<User> user;
}
