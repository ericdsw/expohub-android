package com.example.ericdesedas.expohub.data.models;

import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.util.*;

public class ApiMeta {

    public java.util.Map<String, String> metaElements;

    public ApiMeta() {
        this.metaElements = new HashMap<>();
    }

    public static class Adapter extends JsonAdapter<ApiMeta> {

        @Override
        public ApiMeta fromJson(JsonReader reader) throws IOException {

            Log.d("FOO", reader.toString());

            ApiMeta apiMeta = new ApiMeta();

            reader.beginObject();
            while (reader.hasNext()) {
                apiMeta.metaElements.put(reader.nextName(), reader.nextString());
            }
            reader.endObject();
            return apiMeta;
        }

        @Override
        public void toJson(JsonWriter writer, ApiMeta value) throws IOException {
            //
        }
    }
}
