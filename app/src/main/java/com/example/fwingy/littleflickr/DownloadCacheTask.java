package com.example.fwingy.littleflickr;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.fwingy.littleflickr.Interfaces.DownloadListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fwingy on 2017/2/24.
 */

public class DownloadCacheTask extends AsyncTask<String, Integer, Integer> {

    private static final String TAG = "DownloadCacheTask";

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;

    private int mlastProgress;

    private DownloadListener mDownloadListener;

    public DownloadCacheTask(DownloadListener downloadListener) {
        mDownloadListener = downloadListener;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        InputStream inputStream = null;
        RandomAccessFile savedFile = null;
        File file = null;
        try {
            long downloadedLength = 0; // 记录已下载的文件长度
            String downloadUrl = strings[0];
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
            Log.d(TAG, "下载地址是 " + directory);
            file = new File(directory + "/LittleFlickr/Cache" + fileName);
            Log.d(TAG, file.getPath());
            if (file.exists()) {
                downloadedLength = file.length();
            }
            long contentLength = getContentLength(downloadUrl);
            if (contentLength == 0) {
                return TYPE_FAILED;
            } else if (contentLength == downloadedLength) {
                // 已下载字节和文件总字节相等，说明已经下载完成了
                return TYPE_SUCCESS;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    // 断点下载，指定从哪个字节开始下载
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            if (response != null) {
                inputStream = response.body().byteStream();
                savedFile = new RandomAccessFile(file, "rw");
                savedFile.seek(downloadedLength); // 跳过已下载的字节
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = inputStream.read(b)) != -1) {
                        total += len;
                        savedFile.write(b, 0, len);
                        // 计算已下载的百分比
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                        publishProgress(progress);

                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > mlastProgress) {
            mDownloadListener.onProgress(progress);
            mlastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer) {
            case TYPE_SUCCESS:
                mDownloadListener.onSuccess();
                break;
            case TYPE_FAILED:
                mDownloadListener.onFailed();
                break;
            default:
                break;
        }
    }

    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }
}
