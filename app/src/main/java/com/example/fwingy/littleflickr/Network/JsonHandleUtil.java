package com.example.fwingy.littleflickr.Network;

import com.example.fwingy.littleflickr.GsonData.OriPhotos;
import com.example.fwingy.littleflickr.GsonData.Photos;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by fwingy on 2017/2/10.
 */

public class JsonHandleUtil {
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

    public static OriPhotos handleOriPhotosResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject photosJsonObject = jsonObject.getJSONObject("photos");
            String photoContent = photosJsonObject.toString();
            return new Gson().fromJson(photoContent, OriPhotos.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String handleOriginalUrlResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject SizesJsonObject = jsonObject.getJSONObject("sizes");
            JSONArray sizeJsonArray = SizesJsonObject.getJSONArray("size");
            for (int i = 0; i <sizeJsonArray.length(); i ++) {
                if (sizeJsonArray.getJSONObject(i).getString("label").equals("Original")) {
                    return sizeJsonArray.getJSONObject(i).getString("source");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String handleLargeUrlResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject SizesJsonObject = jsonObject.getJSONObject("sizes");
            JSONArray sizeJsonArray = SizesJsonObject.getJSONArray("size");
            for (int i = 0; i <sizeJsonArray.length(); i ++) {
                if (sizeJsonArray.getJSONObject(i).getString("label").equals("Large")) {
                    return sizeJsonArray.getJSONObject(i).getString("source");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
