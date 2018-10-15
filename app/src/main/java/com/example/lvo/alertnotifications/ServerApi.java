package com.example.lvo.alertnotifications;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import okhttp3.ResponseBody;

import com.example.lvo.alertnotifications.userInfoModel;
import com.example.lvo.alertnotifications.simpleResponceModel;

public interface ServerApi {

    @GET("/users/getbytoken")
    Call<userInfoModel> getUserByToken(@Query("token") String token);

    @FormUrlEncoded
    @POST("/users/new")
    Call<userInfoModel> registerUser(@Field("token") String token, @Field("login") String login, @Field("password") String password);

    @FormUrlEncoded
    @POST("/users/logout")
    Call<simpleResponceModel> logoutUser(@Field("token") String token);

    @FormUrlEncoded
    @POST("/incident/notificationstatus")
    Call<simpleResponceModel> checkNotification(@Field("token") String token, @Field("notification_id") String id, @Field("status") String status);

    @GET("/incident/getbynotification")
    Call<incidentModel> getincedentbynotification(@Query("notification_id") String id);

    @GET("/file/download")
    Call<ResponseBody> downloadFile(@Query("incident_id") String incident_id, @Query("filename") String filename);
}