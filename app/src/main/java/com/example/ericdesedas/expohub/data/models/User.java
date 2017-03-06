package com.example.ericdesedas.expohub.data.models;

import com.squareup.moshi.Json;

import java.util.List;

import moe.banana.jsonapi2.HasMany;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "users")
public class User extends Resource {

    // Properties
    @Json(name = "name")                public String name;
    @Json(name = "username")            public String username;
    @Json(name = "email")               public String email;

    // Relationships
    @Json(name = "fairs")               public HasMany<Fair> fairs;
    @Json(name = "helpingFairs")        public HasMany<Fair> helpingFairs;
    @Json(name = "bannedFairs")         public HasMany<Fair> bannedFairs;
    @Json(name = "attendingFairEvents") public HasMany<FairEvent> attendingFairEvents;
    @Json(name = "comments")            public HasMany<Comment> comments;

    public List<Fair> getFairs() {
        return fairs.get(getContext());
    }

    public List<Fair> getHelpingFairs() {
        return helpingFairs.get(getContext());
    }

    public List<Fair> getBannedFairs() {
        return bannedFairs.get(getContext());
    }

    public List<FairEvent> getAttendingFairEvents() {
        return attendingFairEvents.get(getContext());
    }

    public List<Comment> getComments() {
        return comments.get(getContext());
    }
}
