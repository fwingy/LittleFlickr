package com.example.fwingy.littleflickr.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.fwingy.littleflickr.Activities.MainActivity;
import com.example.fwingy.littleflickr.DownloadCacheTask;
import com.example.fwingy.littleflickr.DownloadTask;
import com.example.fwingy.littleflickr.Interfaces.DownloadListener;
import com.example.fwingy.littleflickr.R;

/**
 * Created by fwingy on 2017/2/24.
 */

public class DownloadCacheService extends Service implements DownloadListener{
    private static final String TAG = "DownloadCacheService";

    private DownloadCacheTask mDownloadTask;

    private String mDownloadUrl;

    DownloadCacheBinder downloadCacheBinder = new DownloadCacheBinder();

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, DownloadCacheService.class);
        return intent;
    }


    @Override
    public void onProgress(int progress) {
        //getNotificationManager().notify(1, getNotification(getString(R.string.notification_downloading), progress));
    }

    @Override
    public void onSuccess() {
        mDownloadTask = null;
        // 下载成功时将前台服务通知关闭，并创建一个下载成功的通知
        //stopForeground(true);
        //getNotificationManager().notify(1, getNotification(getString(R.string.notification_download_suceess), -1));
        //Toast.makeText(DownloadCacheService.this, getString(R.string.notification_download_suceess), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed() {
        mDownloadTask = null;
        // 下载失败时将前台服务通知关闭，并创建一个下载失败的通知
        //stopForeground(true);
        //getNotificationManager().notify(1, getNotification(getString(R.string.notification_download_failed), -1));
        //Toast.makeText(DownloadCacheService.this, getString(R.string.notification_download_failed), Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return downloadCacheBinder;
    }

    public class DownloadCacheBinder extends Binder {
        public void startDownload(String url) {
            if (mDownloadTask == null) {
                mDownloadUrl = url;
                mDownloadTask = new DownloadCacheTask(DownloadCacheService.this);
                mDownloadTask.execute(mDownloadUrl);
                //startForeground(1, getNotification(getString(R.string.notification_downloading), 0));
                //Toast.makeText(DownloadCacheService.this, getString(R.string.notification_downloading), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.flickr_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.flickr_icon));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if (progress >= 0) {
            // 当progress大于或等于0时才需显示下载进度
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }
}
