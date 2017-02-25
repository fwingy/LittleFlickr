package com.example.fwingy.littleflickr.Interfaces;

/**
 * Created by fwingy on 2017/2/24.
 */

public interface DownloadListener {

    public abstract void onProgress(int progress);

    public abstract void onSuccess();

    public abstract void onFailed();

}
