package com.lockmotor.global;

import com.lockmotor.implement.models.InfoRequest;
import com.lockmotor.implement.models.InfoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Tran Dinh Dat on 3/8/2016.
 */
public interface LockMotorAPI {
    @POST("get-info")
    Call<InfoResponse> getInfo(@Body InfoRequest request);
}
