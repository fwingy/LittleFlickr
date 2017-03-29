package com.example.fwingy.littleflickr.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.fwingy.littleflickr.R;

/**
 * Created by fwingy on 2017/3/27.
 */

public class SplashActivity extends AppCompatActivity {

    public static final long SPLASH_TIME = 1500;
    public static final int JUMP_TO_NEXT_ACTIVITY = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case JUMP_TO_NEXT_ACTIVITY:
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splashview);

        ImageView imageView = (ImageView) findViewById(R.id.splashphoto);
        imageView.setImageResource(R.mipmap.flickr_icon);
        Message message = new Message();
        message.what = JUMP_TO_NEXT_ACTIVITY;
        mHandler.sendMessageDelayed(message, SPLASH_TIME);

    }


}
