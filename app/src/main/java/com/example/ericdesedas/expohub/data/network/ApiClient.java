package com.example.ericdesedas.expohub.data.network;

import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.models.News;
import com.example.ericdesedas.expohub.data.models.Stand;
import com.example.ericdesedas.expohub.data.models.User;

import java.util.Map;

import moe.banana.jsonapi2.Document;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    Call<Document<Fair>> getFairs(@QueryMap Map<String, String> parameters);

    @GET("fairs/{id}")
    Call<Document<Fair>> getFair(@Path("id") String id, @QueryMap Map<String, String> parameters);

    // ======================================= FairEvents =================================== //

    @GET("fairEvents")
    Call<Document<FairEvent>> getFairEvents(@QueryMap Map<String, String> parameters);

    @GET("fairEvents/{id}")
    Call<Document<FairEvent>> getFairEvent(@Path("id") String id, @QueryMap Map<String, String> parameters);

    @GET("fairs/{fairId}/fairEvents")
    Call<Document<FairEvent>> getFairEventsByFair(@Path("fairId") String fairId, @QueryMap Map<String, String> parameters);

    // ========================================== News ====================================== //

    @GET("fairs/{fairId}/news")
    Call<Document<News>> getNewsByFair(@Path("fairId") String fairId, @QueryMap Map<String, String> parameters);

    // ========================================== Stands ===================================== //

    @GET("fairs/{fairId}/stands")
    Call<Document<Stand>>getStandsByFair(@Path("fairId") String fairId, @QueryMap Map<String, String> parameters);

    // =========================================== Auth ====================================== //

    @FormUrlEncoded
    @POST("login")
    Call<Document<User>> login(@Field("login_param") String loginParam, @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<Document<User>> register(@Field("name") String name, @Field("username") String username,
                                  @Field("email") String email, @Field("password") String password);

    // =========================================== User ====================================== //

    @GET("users/{id}")
    Call<Document<User>> getUser(@Path("id") String id);
}
