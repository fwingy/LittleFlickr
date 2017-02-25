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

    @SerializedName("url_s")
    private String mUrl_s;

    @SerializedName("url_l")
    private String mUrl_l;

    @SerializedName("url_o")
    private String mUrl_o;

    public String getUrl_o() {
        return mUrl_o;
    }

    public void setUrl_o(String url_o) {
        mUrl_o = url_o;
    }

    public String getUrl_s() {
        return mUrl_s;
    }

    public void setUrl_s(String url_s) {
        mUrl_s = url_s;
    }

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
