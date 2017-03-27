package com.example.fwingy.littleflickr.OAuth;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.fwingy.littleflickr.Activities.MainActivity;
import com.example.fwingy.littleflickr.Activities.PhotoWallFragment;
import com.example.fwingy.littleflickr.R;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.auth.Permission;
import com.googlecode.flickrjandroid.oauth.OAuthToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Created by fwingy on 2017/3/25.
 */

public class OAuthTask extends AsyncTask<Void, Integer, String> {

    private PhotoWallFragment mPhotoWallFragment;

    private Context mContext;

    private static final Logger logger = LoggerFactory
            .getLogger(OAuthTask.class);

    private static final Uri OAUTH_CALLBACK_URI = Uri.parse(MainActivity.CALLBACK_SCHEME
            + "://oauth"); //$NON-NLS-1$

    public OAuthTask(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            Flickr f = FlickrHelper.getInstance().getFlickr();
            OAuthToken oauthToken = f.getOAuthInterface().getRequestToken(
                    OAUTH_CALLBACK_URI.toString());
            Log.d("URI是 ", OAUTH_CALLBACK_URI.toString());
            saveTokenSecrent(oauthToken.getOauthTokenSecret());
            URL oauthUrl = f.getOAuthInterface().buildAuthenticationUrl(
                    Permission.READ, oauthToken);
            return oauthUrl.toString();
        } catch (Exception e) {
            logger.error("Error to oauth", e); //$NON-NLS-1$
            return "error:" + e.getMessage(); //$NON-NLS-1$
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("处理结果是 ", result);

        mPhotoWallFragment.closeOAuthingDialog();
        if (result != null && !result.startsWith("error") ) { //$NON-NLS-1$
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse(result)));
        } else {
            Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
        }
    }

    private void saveTokenSecrent(String tokenSecret) {
        logger.debug("request token: " + tokenSecret); //$NON-NLS-1$
        MainActivity act = (MainActivity) mContext;
        act.saveOAuthToken(null, null, null, tokenSecret);
        logger.debug("oauth token secret saved: {}", tokenSecret); //$NON-NLS-1$
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MainActivity mMainActivity = (MainActivity) mContext;
        mPhotoWallFragment = (PhotoWallFragment) mMainActivity.getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        mPhotoWallFragment.showOAuthingDialog();

    }
}
