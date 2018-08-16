package com.example.lvo.alertnotifications;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

import com.example.lvo.alertnotifications.userInfoModel;
import com.example.lvo.alertnotifications.simpleResponceModel;

public interface ServerApi {

    @GET("/users/getbytoken")
    Call<userInfoModel> getUserByToken(@Query("token") String token);

    @FormUrlEncoded
    @POST("/users/new")
    Call<userInfoModel> registerUser(@Field("token") String token, @Field("login") String login, @Field("password") String password);

    @FormUrlEncoded
    @POST("/incedent/checknotification")
    Call<simpleResponceModel> checkNotification(@Field("token") String token, @Field("notification_id") String id);

    @GET("/incedent/getbynotification")
    Call<incidentModel> getincedentbynotification(@Query("notification_id") String id);


}