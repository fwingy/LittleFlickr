package com.example.fwingy.littleflickr.OAuth;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fwingy.littleflickr.Activities.MainActivity;
import com.example.fwingy.littleflickr.Activities.PhotoWallFragment;
import com.example.fwingy.littleflickr.R;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadUserTask extends AsyncTask<OAuth, Void, User> {
	/**
	 *
	 */
	private final MainActivity mMainActivity;
	private PhotoWallFragment mPhotoWallFragment;
	private ImageView userIconImage;
	private final Logger logger = LoggerFactory.getLogger(LoadUserTask.class);

	public LoadUserTask(MainActivity MainActivity) {
//						//ImageView userIconImage) {
		this.mMainActivity = MainActivity;
		//this.userIconImage = userIconImage;
	}

	/**
	 * The progress dialog before going to the browser.
	 */
	private ProgressDialog mProgressDialog;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mPhotoWallFragment = (PhotoWallFragment) mMainActivity.getSupportFragmentManager()
				.findFragmentById(R.id.fragment_container);
		mPhotoWallFragment.showLoadingUserDialog();

	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected User doInBackground(OAuth... params) {
		OAuth oauth = params[0];
		User user = oauth.getUser();
		OAuthToken token = oauth.getToken();
		try {
			Flickr f = FlickrHelper.getInstance()
					.getFlickrAuthed(token.getOauthToken(), token.getOauthTokenSecret());
			return f.getPeopleInterface().getInfo(user.getId());
		} catch (Exception e) {
			Toast.makeText(mMainActivity, e.toString(), Toast.LENGTH_LONG).show();
			logger.error(e.getLocalizedMessage(), e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(User user) {
		mPhotoWallFragment.closeLoadingUserDialog();
		if (user == null) {
			return;
		}
		mMainActivity.setUser(user);
//		if (user.getBuddyIconUrl() != null) {
//			String buddyIconUrl = user.getBuddyIconUrl();
//			if (userIconImage != null) {
//				//ImageDownloadTask task = new ImageDownloadTask(userIconImage);
//				//Drawable drawable = new DownloadedDrawable(task);
//				userIconImage.setImageDrawable(drawable);
//				task.execute(buddyIconUrl);
//			}
//		}
	}


}