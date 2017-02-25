package com.example.fwingy.littleflickr.GsonData;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fwingy on 2017/2/23.
 */

public class Size {
    @SerializedName("label")
    private String mLabel;

    @SerializedName("source")
    private String mSource;

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }
}
