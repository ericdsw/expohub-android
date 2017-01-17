package com.example.ericdesedas.expohub.data.models;

import com.squareup.moshi.Json;

import org.joda.time.format.DateTimeFormat;

import java.util.List;

import moe.banana.jsonapi2.HasMany;
import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "fairEvents")
public class FairEvent extends Resource {

    // Properties
    @Json(name = "title")       public String title;
    @Json(name = "image")       public String image;
    @Json(name = "description") public String description;
    @Json(name = "date")        public String date;
    @Json(name = "location")    public String location;

    // Relationships
    @Json(name = "fair")            public HasOne<Fair> fair;
    @Json(name = "eventType")       public HasOne<EventType> eventType;
    @Json(name = "speakers")        public HasMany<Speaker> speakers;
    @Json(name = "attendingUsers")  public HasMany<User> attendingUsers;
    @Json(name = "categories")      public HasMany<Category> categories;

    public Fair getFair() {
        return fair.get(getContext());
    }

    public EventType getEventType() {
        return eventType.get(getContext());
    }

    public List<Speaker> getSpeakers() {
        return speakers.get(getContext());
    }

    public List<User> getAttendingUsers() {
        return attendingUsers.get(getContext());
    }

    public List<Category> getCategories() {
        return categories.get(getContext());
    }

    public String parsedDate() {
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
                .parseDateTime(this.date)
                .toString("MMM dd, yyyy");
    }
}
