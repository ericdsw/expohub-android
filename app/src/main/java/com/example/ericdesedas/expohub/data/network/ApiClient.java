package com.example.ericdesedas.expohub.data.network;

import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.models.News;
import com.example.ericdesedas.expohub.data.models.Stand;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * ApiClient implementation
 * This interface defines all available endpoints the app can query to the server
 *
 * Note: All methods must accept a {@link QueryMap} parameter
 */
public interface ApiClient {

    // ======================================== Fairs ======================================== //

    @GET("fairs")
    Call<Fair[]> getFairs(@QueryMap Map<String, String> parameters);

    @GET("fairs/{id}")
    Call<Fair> getFair(@Path("id") String id, @QueryMap Map<String, String> parameters);

    // ======================================= FairEvents =================================== //

    @GET("fairEvents")
    Call<FairEvent[]> getFairEvents(@QueryMap Map<String, String> parameters);

    @GET("fairEvents/{id}")
    Call<FairEvent> getFairEvent(@Path("id") String id, @QueryMap Map<String, String> parameters);

    @GET("fairs/{fairId}/fairEvents")
    Call<FairEvent[]> getFairEventsByFair(@Path("fairId") String fairId, @QueryMap Map<String, String> parameters);

    // ========================================== News ====================================== //

    @GET("fairs/{fairId}/news")
    Call<News[]> getNewsByFair(@Path("fairId") String fairId, @QueryMap Map<String, String> parameters);

    // ========================================== Stands ===================================== //

    @GET("fairs/{fairId}/stands")
    Call<Stand[]> getStandsByFair(@Path("fairId") String fairId, @QueryMap Map<String, String> parameters);
}
