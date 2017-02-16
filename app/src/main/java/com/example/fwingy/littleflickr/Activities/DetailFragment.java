package com.example.fwingy.littleflickr.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fwingy.littleflickr.DataLab.DataLab;
import com.example.fwingy.littleflickr.GsonData.Photo;
import com.example.fwingy.littleflickr.R;
import com.squareup.picasso.Picasso;

/**
 * Created by fwingy on 2017/2/16.
 */

public class DetailFragment extends Fragment {

    private static final String PHOTO_ID =
            "com.example.fwingy.littleflickr.photo_id";

    private Photo mPhoto;
    private ImageView mPhotoImageView;

    private Toolbar mDetailToolBar;

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

        mPhoto = DataLab.getDataLab(getActivity()).getPhoto(getArguments().getString(PHOTO_ID));

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
                .load(mPhoto.getUrl())
                .into(mPhotoImageView);

        //int maxWidth = mPhotoImageView.getMaxWidth();
        //int maxHeight = mPhotoImageView.getMaxHeight();
        //mPhotoImageView.setMaxWidth(maxWidth);
        //mPhotoImageView.setMaxHeight(10dp);

        return view;
    }
}
