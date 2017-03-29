package com.example.fwingy.littleflickr.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fwingy.littleflickr.Base.ActivitiesCollecter;
import com.example.fwingy.littleflickr.Base.BaseActivity;
import com.example.fwingy.littleflickr.OAuth.GetOAuthTokenTask;
import com.example.fwingy.littleflickr.OAuth.LoadUserTask;
import com.example.fwingy.littleflickr.OAuth.OAuthTask;
import com.example.fwingy.littleflickr.R;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;
import com.squareup.picasso.Picasso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    public static final String CALLBACK_SCHEME = "sample-oauth"; //$NON-NLS-1$
    public static final String PREFS_NAME = "flickrj-android-sample-pref"; //$NON-NLS-1$
    public static final String KEY_OAUTH_TOKEN = "flickrj-android-oauthToken"; //$NON-NLS-1$
    public static final String KEY_TOKEN_SECRET = "flickrj-android-tokenSecret"; //$NON-NLS-1$
    public static final String KEY_USER_NAME = "flickrj-android-userName"; //$NON-NLS-1$
    public static final String KEY_USER_ID = "flickrj-android-userId"; //$NON-NLS-1$

    private TextView mUserName;
    private TextView mUserRealName;
    private ImageView mUserIcon;

    private PhotoWallFragment mPhotoWallFragment;

    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

    @Override
    protected Fragment createFragment() {
        //return TestProgressBarFragment.newInstance();
        return PhotoWallFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_contain);
        ActivitiesCollecter.addActivities(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        loadOAuth();
    }

    public void startOAuth() {
        OAuth oauth = getOAuthToken();
        if (oauth == null || oauth.getUser() == null) {
            OAuthTask task = new OAuthTask(this);
            task.execute();
        }
    }

    public void loadOAuth() {
        OAuth oAuth = getOAuthToken();
        if (oAuth == null || oAuth.getUser() == null) {
        } else {
            load(oAuth);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //this is very important, otherwise you would get a null Scheme in the onResume later on.
        setIntent(intent);
    }

    public String getString() {
        return "阿西，试试啦";
    }

    public void setUser(User user) {
        mPhotoWallFragment = (PhotoWallFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        mPhotoWallFragment.setUser(user);
    }

    public void loadUserIcon(OAuth oAuth) {
        mPhotoWallFragment.loadUserIcon(oAuth);
    }

    public void saveOAuthToken(String userName, String userId, String token, String tokenSecret) {
        //logger.debug("Saving userName=%s, userId=%s, oauth token={}, and token secret={}", new String[]{userName, userId, token, tokenSecret}); //$NON-NLS-1$
        SharedPreferences sp = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_OAUTH_TOKEN, token);
        editor.putString(KEY_TOKEN_SECRET, tokenSecret);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public void onOAuthDone(OAuth result) {
        if (result == null) {
            Toast.makeText(this,
                    "Authorization failed", //$NON-NLS-1$
                    Toast.LENGTH_LONG).show();
        } else {
            User user = result.getUser();
            OAuthToken token = result.getToken();
            if (user == null || user.getId() == null || token == null
                    || token.getOauthToken() == null
                    || token.getOauthTokenSecret() == null) {
                Toast.makeText(this,
                        "Authorization failed", //$NON-NLS-1$
                        Toast.LENGTH_LONG).show();
                return;
            }
            String message = String.format(Locale.US, "Authorization Succeed: user=%s, userId=%s, oauthToken=%s, tokenSecret=%s", //$NON-NLS-1$
                    user.getUsername(), user.getId(), token.getOauthToken(), token.getOauthTokenSecret());
            Toast.makeText(this,
                    message,
                    Toast.LENGTH_LONG).show();
            saveOAuthToken(user.getUsername(), user.getId(), token.getOauthToken(), token.getOauthTokenSecret());
            load(result);
        }
    }

    protected void load(OAuth oauth) {
        if (oauth != null) {
            if (oauth.getUser() == null) {
                new LoadUserTask(this).execute(oauth);
                //new LoadPhotostreamTask(this, listView).execute(oauth);
                String iconUrl = "flickr.com/buddyicons/"
                        + oauth.getUser().getId()
                        + ".jpg";
                Log.d(TAG, iconUrl);
                loadUserIcon(oauth);
            }
            else {
                setUser(oauth.getUser());
                loadUserIcon(oauth);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadOAuth();
        Intent intent = getIntent();
        String scheme = intent.getScheme();
        OAuth savedToken = getOAuthToken();
        if (CALLBACK_SCHEME.equals(scheme) && (savedToken == null || savedToken.getUser() == null)) {
            Uri uri = intent.getData();
            String query = uri.getQuery();

            String[] data = query.split("&"); //$NON-NLS-1$
            if (data != null && data.length == 2) {
                String oauthToken = data[0].substring(data[0].indexOf("=") + 1); //$NON-NLS-1$
                String oauthVerifier = data[1]
                        .substring(data[1].indexOf("=") + 1); //$NON-NLS-1$
                logger.debug("OAuth Token: {}; OAuth Verifier: {}", oauthToken, oauthVerifier); //$NON-NLS-1$

                OAuth oauth = getOAuthToken();
                if (oauth != null && oauth.getToken() != null && oauth.getToken().getOauthTokenSecret() != null) {
                    GetOAuthTokenTask task = new GetOAuthTokenTask(this);
                    task.execute(oauthToken, oauth.getToken().getOauthTokenSecret(), oauthVerifier);
                }
            }
        }
    }

    public OAuth getOAuthToken() {
        //Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String oauthTokenString = settings.getString(KEY_OAUTH_TOKEN, null);
        String tokenSecret = settings.getString(KEY_TOKEN_SECRET, null);
        if (oauthTokenString == null && tokenSecret == null) {
            logger.warn("No oauth token retrieved"); //$NON-NLS-1$
            return null;
        }
        OAuth oauth = new OAuth();
        String userName = settings.getString(KEY_USER_NAME, null);
        String userId = settings.getString(KEY_USER_ID, null);
        if (userId != null) {
            User user = new User();
            user.setUsername(userName);
            user.setId(userId);
            oauth.setUser(user);
        }
        OAuthToken oauthToken = new OAuthToken();
        oauth.setToken(oauthToken);
        oauthToken.setOauthToken(oauthTokenString);
        oauthToken.setOauthTokenSecret(tokenSecret);
        logger.debug("Retrieved token from preference store: oauth token={}, and token secret={}", oauthTokenString, tokenSecret); //$NON-NLS-1$
        return oauth;
    }
}
