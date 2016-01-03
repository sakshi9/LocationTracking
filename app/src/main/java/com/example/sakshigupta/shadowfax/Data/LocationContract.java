package com.example.sakshigupta.shadowfax.Data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by sakshigupta on 03/01/16.
 */
public class LocationContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.sakshigupta.shadowfax.provider";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_USER_LOCATION_DETAILS = "user_location_details";

    /* Inner class that defines the table contents of the userDetails table */
    public static final class UserLocationDetails implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_LOCATION_DETAILS).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_USER_LOCATION_DETAILS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_USER_LOCATION_DETAILS;

        // Table name
        public static final String TABLE_NAME = "user_location_details";

        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_UPLOAD_FLAG = "upload_flag";
        public static final String COLUMN_USER_DEVICE_ID = "device_id";

        public static Uri buildUserDetailsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
