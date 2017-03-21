package com.example.fwingy.littleflickr.Activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.example.fwingy.littleflickr.DataLab.OriDataLab;
import com.example.fwingy.littleflickr.GsonData.OriPhoto;
import com.example.fwingy.littleflickr.GsonData.Photo;
import com.example.fwingy.littleflickr.R;
import com.example.fwingy.littleflickr.Services.DownloadCacheService;
import com.example.fwingy.littleflickr.Services.DownloadService;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.logging.SocketHandler;

import static com.example.fwingy.littleflickr.Activities.DetailActivity.isToolBarShowed;

/**
 * Created by fwingy on 2017/2/16.
 */

public class DetailFragment extends Fragment {

    private static final String TAG = "DetailFragment";

    private static final String PHOTO_ID =
            "com.example.fwingy.littleflickr.photo_id";

    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    //public static DetailFragment currentDetailFragment;

    private String mPhotoFileName;

    private String mPhotoUrl_m;

    private String mPhotoCaption;

    private Photo mPhoto;

    private OriPhoto mOriPhoto;

    private ImageView mPhotoImageView;

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


        getActivity().startService(DownloadService.newIntent(getActivity()));
        getActivity().bindService(DownloadService.newIntent(getActivity()), mServiceConnection, Context.BIND_AUTO_CREATE);




        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }

        setHasOptionsMenu(true);
        mPhoto = DataLab.getDataLab(getActivity()).getPhoto(getArguments().getString(PHOTO_ID));

        Log.d(TAG, "标题 " + mPhoto.getCaption());
        mOriPhoto = OriDataLab.getOriDataLab(getActivity()).getOriPhoto(getArguments().getString(PHOTO_ID));

        mPhotoFileName = mPhoto.getUrl_m().substring(mPhoto.getUrl_m().lastIndexOf("/"));
        mPhotoUrl_m = mPhoto.getUrl_m();
        mPhotoCaption = mPhoto.getCaption();
        //mDownloadCacheBinder.startDownload(mPhoto.getUrl_m());

        //currentDetailFragment = (DetailFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_detail, container, false);



//        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //mDownloadCacheBinder.startDownload(mPhoto.getUrl_m());
//
//                String fileName = mPhoto.getUrl_m().substring(mPhoto.getUrl_m().lastIndexOf("/"));
//                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
//                Log.d(TAG, "下载地址是 " + directory);
//                File file = new File(directory + "/LittleFlickr/Cache" + fileName);
//                Uri uri = Uri.fromFile(file);
//
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                shareIntent.setType("image/jpeg");
//                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
//                Toast.makeText(getActivity(), "asdfgh", Toast.LENGTH_SHORT);
//            }
//        });

        //getActivity().startService(DownloadCacheService.newIntent(getActivity()));
        //getActivity().bindService(DownloadCacheService.newIntent(getActivity()), mCacheServiceConnection, Context.BIND_AUTO_CREATE);



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
        mPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isToolBarShowed) {  //import static com.example.fwingy.littleflickr.Activities.DetailActivity.isToolBarShowed;
                    ((DetailActivity)getActivity()).startHideAnimation();
                    isToolBarShowed = false;
                } else {
                    ((DetailActivity)getActivity()).startShowAnimation();
                    isToolBarShowed = true;
                }
            }
        });
        Picasso.with(getActivity())
                .load(mPhoto.getUrl_m())
                .into(mPhotoImageView);

//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile());

        int maxWidth = mPhotoImageView.getMaxWidth();
        int maxHeight = mPhotoImageView.getMaxHeight();
        mPhotoImageView.setMaxWidth(maxWidth);
        mPhotoImageView.setMaxHeight(maxHeight);

        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        String fileName = mPhoto.getUrl_m().substring(mPhoto.getUrl_m().lastIndexOf("/"));
//        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
//        Log.d(TAG, "下载地址是 " + directory);
//        File file = new File(directory + "/LittleFlickr/Cache" + fileName);
//        if (!file.exists()) {
//
//            mDownloadCacheBinder.startDownload(mPhoto.getUrl_m());
//        }
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.photo_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_download:
                //mDownloadCacheBinder.startDownload(mPhoto.getUrl_m());
                mDownloadBinder.startDownload(mOriPhoto.getUrl_o());
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

    public Intent newItentForShare() {
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        Log.d(TAG, "下载地址是 " + directory);
        File file = new File(directory + "/LittleFlickr/Cache" + mPhotoFileName);
        Uri uri = Uri.fromFile(file);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");

        return shareIntent;
    }

    public String getUrl_m() {
        return mPhotoUrl_m;
    }

    public String getPhotoCaption() {
        return mPhotoCaption;
    }

    //    public static void downloadCache() {
//        mDownloadCacheBinder.startDownload(mPhoto.getUrl_m());
//    }
}
