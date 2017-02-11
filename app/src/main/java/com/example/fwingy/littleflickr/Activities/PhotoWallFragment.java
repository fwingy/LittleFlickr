package com.example.fwingy.littleflickr.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.example.fwingy.littleflickr.GsonData.Photo;
import com.example.fwingy.littleflickr.GsonData.Photos;
import com.example.fwingy.littleflickr.Network.GsonUtil;
import com.example.fwingy.littleflickr.Network.HTTPUtil;
import com.example.fwingy.littleflickr.Network.UrlGenerater;
import com.example.fwingy.littleflickr.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.fwingy.littleflickr.R.id.swipeToLoadLayout;

/**
 * Created by fwingy on 2017/2/10.
 */

public class PhotoWallFragment extends Fragment {

    private String TAG = "PhotoWallFragment";

    private RecyclerView mPhotoWallRecyclerView;

    private SwipeToLoadLayout mSwipeToLoadLayout;

    private PhotoAdapter mPhotoAdapter;

    private List<Photo> mPhotos;

    private void setupAdapter() {
        if (isAdded()) {
            mPhotoWallRecyclerView.setAdapter(mPhotoAdapter);
        }
    }

    public static PhotoWallFragment newInstance() {
        return new PhotoWallFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        queryFromServer(UrlGenerater.getUrlStringWithFlickrGetRecent());
        //autoRefresh();
        Log.i(TAG, UrlGenerater.getUrlStringWithFlickrGetRecent());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mPhotoWallRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        mSwipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        mPhotoWallRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mSwipeToLoadLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeToLoadLayout.setLoadingMore(false);
                        queryFromServer(UrlGenerater.getUrlStringWithFlickrGetRecent());
                        //mPhotoAdapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeToLoadLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeToLoadLayout.setRefreshing(false);
                        queryFromServer(UrlGenerater.getUrlStringWithFlickrGetRecent());

                    }
                }, 2000);
            }
        });

        if (savedInstanceState != null) {
            setupAdapter();
        }

        autoRefresh();
        return view;
    }

    private void autoRefresh() {
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    private void queryFromServer(String address) {
        HTTPUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.i(TAG, "加载的内容是:" + responseText);
                final Photos photos = GsonUtil.handlePhotosResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPhotos = photos.getPhotoList();
                        mPhotoAdapter = new PhotoAdapter(mPhotos);
                        setupAdapter();
                    }
                });
                }
            });
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        //private TextView mTitleTextView;
        private ImageView mImageView;

        public PhotoHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.fragment_photo_item_imageview);
        }

        public void bindPhotoItem(Photo item) {
            //mTitleTextView.setText(item.toString());
            Picasso.with(getContext())
                    .load(item.getUrl())
                    .into(mImageView);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<Photo> mPhotoList;

        public PhotoAdapter(List<Photo> photoList) {
            mPhotoList = photoList;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.photo_item, viewGroup, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            Photo photoItem = mPhotoList.get(position);
            photoHolder.bindPhotoItem(photoItem);
        }

        @Override
        public int getItemCount() {
            return mPhotoList.size();
        }
    }
    }
