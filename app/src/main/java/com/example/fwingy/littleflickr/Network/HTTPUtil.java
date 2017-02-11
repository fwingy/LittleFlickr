package com.example.fwingy.littleflickr.Network;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by fwingy on 2017/2/10.
 */

public class HTTPUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
