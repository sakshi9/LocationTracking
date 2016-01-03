package com.example.sakshigupta.shadowfax.NetworkCall;

import com.example.sakshigupta.shadowfax.NetworkCall.APIRequestModel;
import com.example.sakshigupta.shadowfax.NetworkCall.APIResponseModel;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by sakshigupta on 03/01/16.
 */
public interface APIService {


    @POST("/shadowfax/location")
    void sendLocation(@Body APIRequestModel.SendLocationModel param,
                     Callback<APIResponseModel.LocationResponseModel> callback);

}
