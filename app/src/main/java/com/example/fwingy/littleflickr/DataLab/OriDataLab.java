package com.example.fwingy.littleflickr.DataLab;

import android.content.Context;

import com.example.fwingy.littleflickr.GsonData.OriPhoto;
import com.example.fwingy.littleflickr.GsonData.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fwingy on 2017/2/16.
 */

public class OriDataLab {
    private static OriDataLab sOriDataLab;

    private List<OriPhoto> mAllOriPhotos;



    public static OriDataLab getOriDataLab(Context context) {
        if (sOriDataLab != null) {
            return sOriDataLab;
        } else {
            sOriDataLab = new OriDataLab(context);
            return sOriDataLab;
        }
    }

    private OriDataLab(Context context) {
        mAllOriPhotos = new ArrayList<>();
    }

    public List<OriPhoto> getAllOriPhotos() {
        return mAllOriPhotos;
    }

    public OriPhoto getOriPhoto(String id) {
        for (OriPhoto oriphoto : mAllOriPhotos) {
            if (oriphoto.getId().equals(id)) {
                return oriphoto;
            }
        }
        return null;
    }
}
