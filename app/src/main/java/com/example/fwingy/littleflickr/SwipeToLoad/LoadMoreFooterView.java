package com.example.fwingy.littleflickr.SwipeToLoad;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.example.fwingy.littleflickr.R;

/**
 * Created by fwingy on 2017/2/11.
 */

public class LoadMoreFooterView extends TextView implements SwipeTrigger, SwipeLoadMoreTrigger {
    public LoadMoreFooterView(Context context) {
        super(context);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onLoadMore() {
        setText(R.string.loading_more);
    }

    @Override
    public void onPrepare() {
        setText("");
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled <= -getHeight()) {
                setText(R.string.release_to_load_more);
            } else {
                setText(R.string.swipe_to_load_more);
            }
        } else {
            setText(R.string.load_more_returning);
        }
    }

    @Override
    public void onRelease() {
        setText(R.string.loading_more);
    }

    @Override
    public void onComplete() {
        setText(R.string.load_complete);
    }

    @Override
    public void onReset() {
        setText("");
    }
}
