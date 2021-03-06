package com.example.ericdesedas.expohub.data.network;

import com.example.ericdesedas.expohub.data.models.Comment;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.models.News;
import com.example.ericdesedas.expohub.data.models.Sponsor;
import com.example.ericdesedas.expohub.data.models.Stand;
import com.example.ericdesedas.expohub.data.models.Unknown;
import com.example.ericdesedas.expohub.data.models.User;

import java.util.Map;

import moe.banana.jsonapi2.Document;
import okhttp3.ResponseBody;
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

    @GET("users/{userId}/fairs")
    Call<Document<Fair>> getFairsByUser(@Path("userId") String userId, @QueryMap Map<String, String> parameters);

    // ======================================= FairEvents =================================== //

    @GET("fairEvents")
    Call<Document<FairEvent>> getFairEvents(@QueryMap Map<String, String> parameters);

    @GET("fairEvents/{id}")
    Call<Document<FairEvent>> getFairEvent(@Path("id") String id, @QueryMap Map<String, String> parameters);

    @GET("fairs/{fairId}/fairEvents")
    Call<Document<FairEvent>> getFairEventsByFair(@Path("fairId") String fairId, @QueryMap Map<String, String> parameters);

    @GET("users/{userId}/attendingFairEvents")
    Call<Document<FairEvent>> getFairEventsByAttendingUser(@Path("userId") String userId, @QueryMap Map<String, String> parameters);

    @POST("fairEvents/{id}/attend")
    Call<ResponseBody> attendFairEvent(@Path("id") String id);

    @POST("fairEvents/{id}/unAttend")
    Call<ResponseBody> unAttendFairEvent(@Path("id") String id);

    // ========================================== News ====================================== //

    @GET("fairs/{fairId}/news")
    Call<Document<News>> getNewsByFair(@Path("fairId") String fairId, @QueryMap Map<String, String> parameters);

    @GET("news/{newsId}")
    Call<Document<News>> getNewsById(@Path("newsId") String newsId, @QueryMap Map<String, String> parameters);

    // ========================================= Comments ==================================== //

    @FormUrlEncoded
    @POST("comments")
    Call<Document<Comment>> createComment(@Field("text") String text, @Field("news_id") String newsId);

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

    @POST("logout")
    Call<ResponseBody> logout();

    // =========================================== User ====================================== //

    @GET("users/{id}")
    Call<Document<User>> getUser(@Path("id") String id, @QueryMap Map<String, String> parameters);

    // ========================================= Sponsors ==================================== //

    @GET("fairs/{fairId}/sponsors")
    Call<Document<Sponsor>> getSponsorsByFair(@Path("fairId") String id, @QueryMap Map<String, String> parameters);
}
