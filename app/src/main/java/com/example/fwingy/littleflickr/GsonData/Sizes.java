package com.example.fwingy.littleflickr.GsonData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fwingy on 2017/2/23.
 */

public class Sizes {
    @SerializedName("size")
    private List<Size> mSizes;

    public List<Size> getSizes() {
        return mSizes;
    }

    public void setSizes(List<Size> sizes) {
        mSizes = sizes;
    }
}
