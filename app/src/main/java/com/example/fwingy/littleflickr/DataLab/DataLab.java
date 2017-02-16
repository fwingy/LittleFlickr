package com.example.fwingy.littleflickr.DataLab;

import android.content.Context;

import com.example.fwingy.littleflickr.GsonData.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fwingy on 2017/2/16.
 */

public class DataLab {
    private static DataLab sDataLab;

    private List<Photo> mAllPhotos;

    public static DataLab getDataLab(Context context) {
        if (sDataLab != null) {
            return sDataLab;
        } else {
            sDataLab = new DataLab(context);
            return sDataLab;
        }
    }

    private DataLab(Context context) {
        mAllPhotos = new ArrayList<>();
    }

    public List<Photo> getAllPhotos() {
        return mAllPhotos;
    }

    public Photo getPhoto(String id) {
        for (Photo photo : mAllPhotos) {
            if (photo.getId().equals(id)) {
                return photo;
            }
        }
        return null;
    }
}
