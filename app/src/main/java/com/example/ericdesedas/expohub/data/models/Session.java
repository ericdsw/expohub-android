package com.example.ericdesedas.expohub.data.models;

import com.squareup.moshi.Json;

public class Session {

    @Json(name = "id")      public String id;
    @Json(name = "name")    public String name;
    @Json(name = "token")   public String token;
}
