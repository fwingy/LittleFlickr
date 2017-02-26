package com.example.fwingy.littleflickr.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.fwingy.littleflickr.DataLab.DataLab;
import com.example.fwingy.littleflickr.GsonData.Photo;
import com.example.fwingy.littleflickr.R;
import com.example.fwingy.littleflickr.Services.DownloadCacheService;

import java.util.List;

/**
 * Created by fwingy on 2017/2/16.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String INTENT_EXTRA_PHOTO_ID =
            "com.example.fwingy.littleflickr.photo_id";

    private ViewPager mViewPager;
    private List<Photo> mPhotoList;
    private Toolbar mDetailToolBar;
    private FragmentManager fm;

    private FloatingActionButton mFloatingActionButton;

    private DownloadCacheService.DownloadCacheBinder mDownloadCacheBinder;

    private ServiceConnection mCacheServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mDownloadCacheBinder = (DownloadCacheService.DownloadCacheBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public static Intent newIntent(Context packageContext, String id) {
        Intent intent = new Intent(packageContext, DetailActivity.class);
        intent.putExtra(INTENT_EXTRA_PHOTO_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_detai_viewpager);

        bindService(DownloadCacheService.newIntent(DetailActivity.this), mCacheServiceConnection, Context.BIND_AUTO_CREATE);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.share_floatingButton);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url_m = DetailFragment.getUrl_m();
                mDownloadCacheBinder.startDownload(url_m);
                Intent shareIntent = DetailFragment.newItentForShare();
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
            }
        });

        mDetailToolBar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(mDetailToolBar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        mDetailToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String photo_id = getIntent().getStringExtra(INTENT_EXTRA_PHOTO_ID);

        mViewPager = (ViewPager) findViewById(R.id.photo_detail_viewpager);
        mPhotoList = DataLab.getDataLab(this).getAllPhotos();

        fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Photo photo = mPhotoList.get(position);
                return DetailFragment.newInstance(photo.getId());
            }

            @Override
            public int getCount() {
                return mPhotoList.size();
            }
        });

        for (int i = 0; i < mPhotoList.size(); i++) {
            if (mPhotoList.get(i).getId().equals(photo_id)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }



}
