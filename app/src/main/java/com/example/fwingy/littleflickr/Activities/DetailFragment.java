package com.example.fwingy.littleflickr.Activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fwingy.littleflickr.DataLab.DataLab;
import com.example.fwingy.littleflickr.GsonData.Photo;
import com.example.fwingy.littleflickr.GsonData.Size;
import com.example.fwingy.littleflickr.Network.HTTPUtil;
import com.example.fwingy.littleflickr.Network.JsonHandleUtil;
import com.example.fwingy.littleflickr.Network.UrlGenerater;
import com.example.fwingy.littleflickr.R;
import com.example.fwingy.littleflickr.Services.DownloadService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by fwingy on 2017/2/16.
 */

public class DetailFragment extends Fragment {

    private static final String TAG = "DetailFragment";

    private static final String PHOTO_ID =
            "com.example.fwingy.littleflickr.photo_id";

    private static final String CURRENT_INDEX =
            "com.example.fwingy.littleflickr.current_index";

    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private Photo mPhoto;

    private ImageView mPhotoImageView;

    private String DownloadUrl;

    private DownloadService.DownloadBinder mDownloadBinder;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mDownloadBinder = (DownloadService.DownloadBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public static Fragment newInstance(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(PHOTO_ID, id);

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        mPhoto = DataLab.getDataLab(getActivity()).getPhoto(getArguments().getString(PHOTO_ID));
        //queryLargeDownloadUrlFromServer(UrlGenerater.getUrlStringWithFlickrGetSize(mPhoto.getId()));
        //queryOriginalDownloadUrlFromServer(UrlGenerater.getUrlStringWithFlickrGetSize(mPhoto.getId()));
        Log.d(TAG, "图片id是" + mPhoto.getId());
        Log.d(TAG, "图片url_s是" + mPhoto.getUrl_s());
        Log.d(TAG, "图片url_l是" + mPhoto.getUrl_l());
        Log.d(TAG, "图片url_o是" + mPhoto.getUrl_o());
        getActivity().startService(DownloadService.newIntent(getActivity()));
        getActivity().bindService(DownloadService.newIntent(getActivity()), mServiceConnection, Context.BIND_AUTO_CREATE);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_detail, container, false);

//        mDetailToolBar = (Toolbar) view.findViewById(R.id.detail_toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(mDetailToolBar);
//        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
//
//
//        mDetailToolBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().onBackPressed();
//            }
//        });
        mPhotoImageView = (ImageView) view.findViewById(R.id.photo_detail_view);
        Picasso.with(getActivity())
                .load(mPhoto.getUrl_l())
                .into(mPhotoImageView);

        int maxWidth = mPhotoImageView.getMaxWidth();
        int maxHeight = mPhotoImageView.getMaxHeight();
        mPhotoImageView.setMaxWidth(maxWidth);
        mPhotoImageView.setMaxHeight(maxHeight);

        return view;
    }


    private void queryOriginalDownloadUrlFromServer(String address) {
        Log.d(TAG, "getSize地址是" + address);
        HTTPUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.i(TAG, "加载的Size内容是:" + responseText);
                DownloadUrl = JsonHandleUtil.handleOriginalUrlResponse(responseText);
                mPhoto.setUrl_o(DownloadUrl);

                Log.d(TAG, "mPhoto的url_o是" + mPhoto.getUrl_o());
            }
        });
    }

    private void queryLargeDownloadUrlFromServer(String address) {
        Log.d(TAG, "getSize地址是" + address);
        HTTPUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.i(TAG, "加载的Size内容是:" + responseText);
                DownloadUrl = JsonHandleUtil.handleLargeUrlResponse(responseText);
                mPhoto.setUrl_l(DownloadUrl);

                Log.d(TAG, "mPhoto的url_l是" + mPhoto.getUrl_l());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.photo_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_action_download:
                Toast.makeText(getActivity(), R.string.notification_downloading, Toast.LENGTH_SHORT);

                    mDownloadBinder.startDownload(mPhoto.getUrl_o());

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "拒绝权限将无法下载", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(mServiceConnection);
    }
}
