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
import android.widget.Toast;

import com.example.fwingy.littleflickr.Network.GsonUtil;
import com.example.fwingy.littleflickr.Network.HTTPUtil;
import com.example.fwingy.littleflickr.Network.UrlGenerater;
import com.example.fwingy.littleflickr.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by fwingy on 2017/2/10.
 */

public class PhotoWallFragment extends Fragment {

    private String TAG = "PhotoWallFragment";

    private RecyclerView mPhotoWallRecyclerView;

    public static PhotoWallFragment newInstance() {
        return new PhotoWallFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        queryFromServer(UrlGenerater.getUrlStringWithFlickrGetRecent());
        Log.i(TAG, UrlGenerater.getUrlStringWithFlickrGetRecent());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mPhotoWallRecyclerView = (RecyclerView) view.findViewById(R.id.photo_wall_recyclerview);
        mPhotoWallRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return view;
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
                GsonUtil.handlePhotosResponse(responseText);
                }
            });
    }
}
