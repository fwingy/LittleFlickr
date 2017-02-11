package com.example.fwingy.littleflickr.SwipeToLoad;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.example.fwingy.littleflickr.R;

/**
 * Created by fwingy on 2017/2/11.
 */

public class RefreshHeaderView extends TextView implements SwipeRefreshTrigger, SwipeTrigger {

    public RefreshHeaderView(Context context) {
        super(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onRefresh() {
        setText("拼命刷新中...");
    }

    @Override
    public void onPrepare() {
        setText("");
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled >= getHeight()) {
                setText(R.string.release_to_refresh);
            } else {
                setText(R.string.swipe_to_refresh);
            }
        } else {
            setText(R.string.refresh_returning);
        }
    }

    @Override
    public void onRelease() {
    }

    @Override
    public void onComplete() {
        setText(R.string.refresh_complete);
    }

    @Override
    public void onReset() {
        setText("");
    }
}