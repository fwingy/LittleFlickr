package com.example.fwingy.littleflickr.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fwingy.littleflickr.R;

/**
 * Created by fwingy on 2017/2/14.
 */

public class TestProgressBarFragment extends Fragment {
    public static Fragment newInstance() {
        return new TestProgressBarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_progressbar, container, false);
        return view;
    }
}
