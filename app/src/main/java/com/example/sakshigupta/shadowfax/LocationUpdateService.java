package com.example.sakshigupta.shadowfax;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.example.sakshigupta.shadowfax.Data.LocationContract;
import com.example.sakshigupta.shadowfax.NetworkCall.APIRequestModel;
import com.example.sakshigupta.shadowfax.NetworkCall.APIResponseModel;
import com.example.sakshigupta.shadowfax.NetworkCall.APIService;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by sakshigupta on 03/01/16.
 */
public class LocationUpdateService extends IntentService {

    double latitude, longitude;
    String timeStamp;
    String deviceId;

    public LocationUpdateService() {
        super(LocationUpdateService.class.getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        super.onStartCommand(intent, flags, startID);
        Log.d("LocationUpdateService", "Location received");
        return START_REDELIVER_INTENT;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle b = intent.getExtras();
        latitude = b.getDouble("latitude");
        longitude = b.getDouble("longitude");
        timeStamp = b.getString("timeStamp");
        deviceId = b.getString("deviceId");

        Cursor cursor = getContentResolver().query(LocationContract.UserLocationDetails.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (!(cursor.moveToFirst()) || (cursor.getCount() ==0)) {
            insertLocationDetailsIntoDb();
            sendLocationDetailsToServer();
            cursor.close();
        } else {
            cursor.moveToLast();
            double previousLat = cursor.getDouble(cursor.getColumnIndex("latitude"));
            double previousLong = cursor.getDouble(cursor.getColumnIndex("longitude"));
            Float distance = distFrom(previousLat, previousLong, latitude, longitude);
            if (distance > 100) {
                insertLocationDetailsIntoDb();
                sendLocationDetailsToServer();
            }
            else{
                //No uploading required since distance between previous uploaded location and current location is less than 100m
            }
            cursor.close();
        }
    }

    public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
        Location locationA = new Location("point A");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lng1);
        Location locationB = new Location("point B");
        locationB.setLatitude(lat2);
        locationB.setLongitude(lng2);
        return locationA.distanceTo(locationB);
    }

    public void insertLocationDetailsIntoDb() {

        ContentValues values = new ContentValues();
        values.put(LocationContract.UserLocationDetails.COLUMN_LATITUDE, latitude);
        values.put(LocationContract.UserLocationDetails.COLUMN_LONGITUDE, longitude);
        values.put(LocationContract.UserLocationDetails.COLUMN_TIMESTAMP, timeStamp);
        values.put(LocationContract.UserLocationDetails.COLUMN_UPLOAD_FLAG, 0);
        values.put(LocationContract.UserLocationDetails.COLUMN_USER_DEVICE_ID, deviceId);

        getContentResolver().insert(LocationContract.UserLocationDetails.CONTENT_URI, values);
    }

    private void sendLocationDetailsToServer() {
        APIRequestModel requestModel = new APIRequestModel();
        final APIRequestModel.SendLocationModel setParamsRequestModel = requestModel.new SendLocationModel();
        setParamsRequestModel.latitude = latitude;
        setParamsRequestModel.longitude = longitude;
        setParamsRequestModel.timeStamp = timeStamp;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://www.sakshi-gupta.com")
                .build();
        APIService apiService = restAdapter.create(APIService.class);
        apiService.sendLocation(setParamsRequestModel, new retrofit.Callback<APIResponseModel.LocationResponseModel>() {
            @Override
            public void success(APIResponseModel.LocationResponseModel providersResponseModel, retrofit.client.Response response) {
                boolean success = providersResponseModel.success;
                if (success == true) {
                    ContentValues value = new ContentValues();
                    value.put(LocationContract.UserLocationDetails.COLUMN_UPLOAD_FLAG, 1);

                    getContentResolver().update(LocationContract.UserLocationDetails.CONTENT_URI,
                            value, LocationContract.UserLocationDetails.COLUMN_USER_DEVICE_ID + " =? ", new String[]{deviceId});
                } else {
                    Log.i("error message", providersResponseModel.message);
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }
}
