package com.example.ericdesedas.expohub.data.models;

import com.squareup.moshi.Json;

import org.joda.time.format.DateTimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import moe.banana.jsonapi2.HasMany;
import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "fairs")
public class Fair extends Resource {

    // Properties
    @Json(name = "name")            public String name;
    @Json(name = "image")           public String image;
    @Json(name = "description")     public String description;
    @Json(name = "website")         public String website;
    @Json(name = "starting_date")   public String startingDate;
    @Json(name = "ending_date")     public String endingDate;
    @Json(name = "address")         public String address;
    @Json(name = "latitude")        public double latitude;
    @Json(name = "longitude")       public double longitude;

    // Relationships
    @Json(name = "user")            public HasOne<User> user;
    @Json(name = "bannedUsers")     public HasMany<User> bannedUsers;
    @Json(name = "helperUsers")     public HasMany<User> helperUsers;
    @Json(name = "sponsors")        public HasMany<Sponsor> sponsors;
    @Json(name = "maps")            public HasMany<Map> maps;
    @Json(name = "categories")      public HasMany<Category> categories;
    @Json(name = "fairEvents")      public HasMany<FairEvent> fairEvents;
    @Json(name = "news")            public HasMany<News> news;
    @Json(name = "stands")          public HasMany<Stand> stands;

    public String getImage() {
        return image;
    }

    public String getShortDescription() {
        if (description.length() >= 70) {
            return description.substring(0, 70) + "...";
        } else {
            return description;
        }
    }

    public String parsedStartingDate() {
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
                .parseDateTime(startingDate)
                .toString("MMM dd, yyyy");
    }

    public String parsedEndingDate() {
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
                .parseDateTime(endingDate)
                .toString("MMM dd, yyyy");
    }
}
