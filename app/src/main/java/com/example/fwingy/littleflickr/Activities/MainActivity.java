package com.example.fwingy.littleflickr.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.fwingy.littleflickr.Base.BaseActivity;
import com.example.fwingy.littleflickr.R;

public class MainActivity extends BaseActivity {

    @Override
    protected Fragment createFragment() {
        return PhotoWallFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_contain);
    }
}
