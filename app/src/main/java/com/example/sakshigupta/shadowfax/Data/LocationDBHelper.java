package com.example.sakshigupta.shadowfax.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sakshigupta on 03/01/16.
 */
public class LocationDBHelper extends SQLiteOpenHelper {

    private static int DATABASE_VERSION = 1;

    private static String DATABASE_NAME = "LocationDataBase";

    public LocationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_USER_LOCATION_DETAILS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                LocationContract.UserLocationDetails.TABLE_NAME + "(" +
                LocationContract.UserLocationDetails._ID + " INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                LocationContract.UserLocationDetails.COLUMN_LATITUDE + " DOUBLE, " +
                LocationContract.UserLocationDetails.COLUMN_LONGITUDE + " DOUBLE, " +
                LocationContract.UserLocationDetails.COLUMN_TIMESTAMP + " TEXT, " +
                LocationContract.UserLocationDetails.COLUMN_UPLOAD_FLAG + " INTEGER, " +
                LocationContract.UserLocationDetails.COLUMN_USER_DEVICE_ID + " TEXT " +
                " );";

        db.execSQL(SQL_CREATE_USER_LOCATION_DETAILS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LocationContract.UserLocationDetails.TABLE_NAME);
        onCreate(db);
    }
}
