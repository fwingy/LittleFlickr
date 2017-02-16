package com.example.fwingy.littleflickr.Network;

import android.net.Uri;

import com.example.fwingy.littleflickr.SearchPreferences;

import java.net.URL;

/**
 * Created by fwingy on 2017/2/10.
 */

public class UrlGenerater {
    private static final String TAG = "UrlGenerater";

    private static final String FLICKR_API_KEY = "7af9c8a47896697b18a39293b622d8f0";

    private static final String FLICKR_SEARCH_METHOD = "flickr.photos.search";

    private static final Uri BASEUri = Uri.parse("https://api.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("method", FLICKR_SEARCH_METHOD)
            .appendQueryParameter("api_key", FLICKR_API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s")
            .appendQueryParameter("per_page", "30")
            .appendQueryParameter("safe_search", "1")
            .build();

    public static String getUrlStringWithFlickrSearch(String searchText) {
        //return buildUrl("toyota RAV");  //只是测试
        return buildUrl(searchText).build().toString();
    }

    public static Uri.Builder buildUrl(String query) {
        Uri.Builder builder = BASEUri.buildUpon()
                .appendQueryParameter("text", query);
                //.appendQueryParameter("title", query);

        return builder;
    }

    public static String getNextPageUrl(String searchText, int Page) {
        Integer page = Page + 1;
        return buildUrl(searchText)
                .appendQueryParameter("page", page.toString())
                .build()
                .toString();
    }

}
