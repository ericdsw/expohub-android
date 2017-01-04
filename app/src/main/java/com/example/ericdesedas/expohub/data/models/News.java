package com.example.ericdesedas.expohub.data.models;

import com.squareup.moshi.Json;

import org.joda.time.format.DateTimeFormat;

import moe.banana.jsonapi2.HasMany;
import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "news")
public class News extends Resource {

    // Properties
    @Json(name = "title")       public String title;
    @Json(name = "content")     public String content;
    @Json(name = "image")       public String image;
    @Json(name = "created_at")  public String createdAt;

    // Relationships
    @Json(name = "fair")        public HasOne<Fair> fair;
    @Json(name = "comments")    public HasMany<Comment> comments;

    public String formattedCreatedAt() {
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
                .parseDateTime(createdAt)
                .toString("MMM dd, yyyy");
    }
}
