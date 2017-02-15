package com.example.fwingy.littleflickr;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by fwingy on 2017/2/14.
 */

public class SearchPreferences {
    private static final String SEARCH_DATA = "search_data";

    public static String getSearchData(Context context) {
        String searchData = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SEARCH_DATA, null);
        return searchData;
    }


    public static void setSearchData(Context context, String searchData) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(SEARCH_DATA, searchData)
                .apply();
    }
}