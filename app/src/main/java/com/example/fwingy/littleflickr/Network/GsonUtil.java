package com.example.fwingy.littleflickr.Network;

import com.example.fwingy.littleflickr.GsonData.Photos;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by fwingy on 2017/2/10.
 */

public class GsonUtil {
    public static Photos handlePhotosResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject photosJsonObject = jsonObject.getJSONObject("photos");
            String photoContent = photosJsonObject.toString();
            return new Gson().fromJson(photoContent, Photos.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
