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
    private String mUrl_m;

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

    public String getUrl_m() {
        return mUrl_m;
    }

    public void setUrl_m(String url) {
        mUrl_m = url;
    }

    @Override
    public String toString() {
        return mCaption;
    }

}
