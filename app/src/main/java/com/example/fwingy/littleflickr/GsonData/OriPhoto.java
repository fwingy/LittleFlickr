package com.example.fwingy.littleflickr.GsonData;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fwingy on 2017/2/10.
 */

public class OriPhoto {
    @SerializedName("title")
    private String mCaption;

    @SerializedName("id")
    private String mId;

    @SerializedName("url_o")
    private String mUrl_o;

    public String getUrl_o() {
        return mUrl_o;
    }

    public void setUrl_o(String url_o) {
        mUrl_o = url_o;
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

    @Override
    public String toString() {
        return mCaption;
    }

}
