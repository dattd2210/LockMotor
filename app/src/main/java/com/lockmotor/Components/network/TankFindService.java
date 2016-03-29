package com.lockmotor.Components.network;

import com.google.gson.JsonElement;
import com.lockmotor.Components.network.base.CallbackListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import retrofit.mime.TypedFile;

/**
 * Created by Tran Dinh Dat on 3/8/2016.
 */
public interface TankFindService {
    @GET("/location/list")
    void getCityList(Callback<JsonElement> callback);

    @GET("/storage_type/list?tank_type=1&product_type=1")
    void getStorageTypeList(Callback<JsonElement> callback);




    @POST("/customers/login")
    void loginWithEmailAndPassword(@Body HashMap<String, Object> params, Callback<JsonElement> callback);

    @GET("/training_programs")
    void getTrainingPrograms(@QueryMap Map<String, Object> params, Callback<JsonElement> callback);

    @Multipart
    @PUT("/measurements/{id}")
    void uploadTrackingFrontImage(@Path("id") int tracking_id, @Part("front_image") TypedFile photo, Callback<JsonElement> callback);
}
