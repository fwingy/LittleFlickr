package com.example.fwingy.littleflickr.GsonData;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fwingy on 2017/2/10.
 */

public class Photo {
    @SerializedName("title")
    private String mCaption;

    @SerializedName("id")
    private String mId;

    @SerializedName("url_m")
    private String mUrl;

    @SerializedName("url_l")
    private String mUrl_l;

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getUrl_l() {
        return mUrl_l;
    }

    public void setUrl_l(String url_l) {
        mUrl_l = url_l;
    }

    @Override
    public String toString() {
        return mCaption;
    }

}
