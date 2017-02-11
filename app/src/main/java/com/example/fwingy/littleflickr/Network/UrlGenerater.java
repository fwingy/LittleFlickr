package com.example.fwingy.littleflickr.Network;

import android.net.Uri;

import java.net.URL;

/**
 * Created by fwingy on 2017/2/10.
 */

public class UrlGenerater {
    private static final String TAG = "UrlGenerater";

    private static final String FLICKR_API_KEY = "7af9c8a47896697b18a39293b622d8f0";

    public static String getUrlStringWithFlickrGetRecent() {
        String url = Uri.parse("https://api.flickr.com/services/rest/")
                .buildUpon()
                .appendQueryParameter("method", "flickr.photos.getRecent")
                .appendQueryParameter("api_key", FLICKR_API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("extras", "url_s")
                .build().toString();
        return url;
    }
}
