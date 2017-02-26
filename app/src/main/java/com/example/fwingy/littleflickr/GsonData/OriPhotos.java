package com.example.fwingy.littleflickr.GsonData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fwingy on 2017/2/10.
 */

public class OriPhotos {
    @SerializedName("photo")
    private List<OriPhoto> mOriPhotoList;

    public List<OriPhoto> getOriPhotoList() {
        return mOriPhotoList;
    }

    public void setOriPhotoList(List<OriPhoto> oriPhotoList) {
        mOriPhotoList = oriPhotoList;
    }
}
