package com.example.fwingy.littleflickr.GsonData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fwingy on 2017/2/10.
 */

public class Photos {
    @SerializedName("photo")
    private List<Photo> mPhotoList;

    public List<Photo> getPhotoList() {
        return mPhotoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        mPhotoList = photoList;
    }
}
