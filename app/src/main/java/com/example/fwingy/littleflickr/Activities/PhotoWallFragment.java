package com.example.fwingy.littleflickr.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.example.fwingy.littleflickr.Base.ActivitiesCollecter;
import com.example.fwingy.littleflickr.DataLab.DataLab;
import com.example.fwingy.littleflickr.DataLab.OriDataLab;
import com.example.fwingy.littleflickr.GsonData.OriPhoto;
import com.example.fwingy.littleflickr.GsonData.OriPhotos;
import com.example.fwingy.littleflickr.GsonData.Photo;
import com.example.fwingy.littleflickr.GsonData.Photos;
import com.example.fwingy.littleflickr.Network.JsonHandleUtil;
import com.example.fwingy.littleflickr.Network.HTTPUtil;
import com.example.fwingy.littleflickr.Network.UrlGenerater;
import com.example.fwingy.littleflickr.OAuth.OAuthTask;
import com.example.fwingy.littleflickr.R;
import com.example.fwingy.littleflickr.SearchPreferences;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.people.User;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 *
 * Created by fwingy on 2017/2/10.
 */

public class PhotoWallFragment extends Fragment {

    private String TAG = "PhotoWallFragment";

    private TextView mUserName;
    private TextView mUserRealName;
    private ImageView mUserIcon;

    private TextView mHintText;

    private ProgressDialog mOAuthingDialog;

    private ProgressDialog mLoadingUserDialog;

    private DrawerLayout mDrawerLayout;

    private RelativeLayout mGalleryContentLayout;

    private RecyclerView mPhotoWallRecyclerView;

    private SwipeToLoadLayout mSwipeToLoadLayout;

    private PhotoAdapter mPhotoAdapter;

    private Toolbar mToolbar;

    private ProgressBar mProgressBar;

    private int mCurrentPage;

    private boolean isRefreshing;

    private List<Photo> mPhotos;

    private List<OriPhoto> mOriPhotos;

    private List<Photo> mNextPagePhotos;

    private List<OriPhoto> mNextPageOriPhotos;

    private List<Photo> mAllPhotos = DataLab.getDataLab(getActivity()).getAllPhotos();

    private List<OriPhoto> mAllOriPhotos = OriDataLab.getOriDataLab(getActivity()).getAllOriPhotos();

    private void setupAdapter() {
        if (isAdded()) {
            mPhotoWallRecyclerView.setAdapter(mPhotoAdapter);
        }
    }

    public static PhotoWallFragment newInstance() {
        return new PhotoWallFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        //mAllPhotos.clear();

        String searchData = SearchPreferences.getSearchData(getActivity());

        //autoRefresh();
        Log.i(TAG, UrlGenerater.getUrlStringWithFlickrSearch(getSearchData()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //showProgressDialog();
        //queryFromServer(UrlGenerater.getUrlStringWithFlickrSearch(getSearchData()));
        //closeProgressDialog();
    }

    public void setUser(User user) {
        mUserName.setText(user.getUsername());
        mUserRealName.setText(user.getRealName());
    }

    public void loadUserIcon(OAuth oAuth) {
        Picasso.with(getActivity())
                .load(oAuth.getUser().getBuddyIconUrl())
                .into(mUserIcon);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);



        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        mHintText = (TextView) view.findViewById(R.id.hint_text);
        mPhotoWallRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        mSwipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        mPhotoWallRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawerlayout);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }

        NavigationView navigationView = (NavigationView) view.findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        mUserName = (TextView) header.findViewById(R.id.username);
        mUserRealName = (TextView) header.findViewById(R.id.userrealname);
        mUserIcon = (ImageView) header.findViewById(R.id.icon_image);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.login:
                        ((MainActivity)getActivity()).startOAuth();
                        break;
                    case R.id.about:
                        break;
                    case R.id.exit:
                        ActivitiesCollecter.finishAllActivities();
                }
                return true;
            }
        });

        mSwipeToLoadLayout.setRefreshEnabled(false);
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mSwipeToLoadLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeToLoadLayout.setLoadingMore(false);
                        String searchData = SearchPreferences.getSearchData(getActivity());
                        mCurrentPage += 2;
                        continueQueryNextPage(UrlGenerater.getNextPageUrl(getSearchData(), mCurrentPage));
                        continueQueryNextPageOri(UrlGenerater.getNextOriPageUrl(getSearchData(), mCurrentPage));

                        //queryFromServer(UrlGenerater.getUrlStringWithFlickrSearch(getSearchData()));
                        //mPhotoAdapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeToLoadLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeToLoadLayout.setRefreshing(false);
                        mAllPhotos.clear();
                        mAllOriPhotos.clear();
                        isRefreshing = true;
                        queryFromServer(UrlGenerater.getUrlStringWithFlickrSearch(getSearchData()));
                        queryOriFromServer(UrlGenerater.getOriUrlStringWithFlickrSearch(getSearchData()));
                        mCurrentPage = 1;
                        isRefreshing = false;
                    }
                }, 2000);
            }
        });

        if (savedInstanceState != null) {
            setupAdapter();
        }

        autoRefresh();
        mGalleryContentLayout = (RelativeLayout) inflater.inflate(R.layout.gallery_content, null);
        mGalleryContentLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mHintText.setText("按右上角“搜索”键搜索Flickr");

        return view;

    }

    private void autoRefresh() {
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(false);
            }
        });
    }

    private void queryFromServer(String address) {
        mHintText.setVisibility(View.GONE);
        if (!isRefreshing) {
            showProgressDialog();
        }
        mGalleryContentLayout.setVisibility(View.VISIBLE);
        HTTPUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加载失败," +
                                "请检查网络连接." +
                                "大陆用户请确保已使用vpn等代理连接.", Toast.LENGTH_LONG).show();
                        closeProgressDialog();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.i(TAG, "加载的内容是:" + responseText);
                final Photos photos = JsonHandleUtil.handlePhotosResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mPhotos = photos.getPhotoList();
                            mAllPhotos.addAll(mPhotos);
                            Log.d(TAG, "测试photo " + mAllPhotos.get(0).getUrl_m());
                            mPhotoAdapter = new PhotoAdapter(mAllPhotos);
                            setupAdapter();
                            closeProgressDialog();
                        }catch (IndexOutOfBoundsException e) {
                            closeProgressDialog();
                            mHintText.setText("糟糕，找不到任何结果！");
                            mHintText.setVisibility(View.VISIBLE);
                        }
                    }
                });
                }
            });
    }

    private void queryOriFromServer(String address) {
        HTTPUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加载失败," +
                                "请检查网络连接." +
                                "大陆用户请确保已使用vpn等代理连接.", Toast.LENGTH_LONG).show();
                        closeProgressDialog();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.i(TAG, "加载的内容是:" + responseText);
                final OriPhotos oriPhotos = JsonHandleUtil.handleOriPhotosResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mOriPhotos = oriPhotos.getOriPhotoList();
                            mAllOriPhotos.addAll(mOriPhotos);
                            Log.d(TAG, "测试Oriphoto " + mAllOriPhotos.get(0).getUrl_o());
                        }
                        catch (IndexOutOfBoundsException e) {
                            closeProgressDialog();
                        }
                    }
                });
            }
        });
    }

    private void continueQueryNextPage(String address) {
        HTTPUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加载失败," +
                                "请检查网络连接." +
                                "大陆用户请确保已使用vpn等代理连接.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.i(TAG, "加载的内容是:" + responseText);
                final Photos NextPagePhotos = JsonHandleUtil.handlePhotosResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mNextPagePhotos = NextPagePhotos.getPhotoList();
                            mAllPhotos.addAll(mNextPagePhotos);
                            mPhotoAdapter.notifyDataSetChanged();
                            Log.d(TAG, "测试NextPage " + mNextPagePhotos.get(0));
                        }catch (IndexOutOfBoundsException e) {
                            closeProgressDialog();
                            Toast.makeText(getActivity(), "没有更多", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

    private void continueQueryNextPageOri(String address) {
        HTTPUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加载失败," +
                                "请检查网络连接." +
                                "大陆用户请确保已使用vpn等代理连接.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.i(TAG, "加载的内容是:" + responseText);
                final OriPhotos NextPageOriPhotos = JsonHandleUtil.handleOriPhotosResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mNextPageOriPhotos = NextPageOriPhotos.getOriPhotoList();
                            mAllOriPhotos.addAll(mNextPageOriPhotos);
                            Log.d(TAG, "测试NextPageOri " + mNextPageOriPhotos.get(0));
                        }catch (IndexOutOfBoundsException e) {
                            closeProgressDialog();
                        }
                    }
                });
            }
        });


    }

    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //private TextView mTitleTextView;
        private ImageView mImageView;
        private Photo photo;

        public PhotoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mImageView = (ImageView) itemView.findViewById(R.id.fragment_photo_item_imageview);
        }

        public void bindPhotoItem(Photo item) {
            photo = item;
            //mTitleTextView.setText(item.toString());
            Picasso.with(getContext())
                    .load(photo.getUrl_m())
                    .placeholder(R.drawable.flickr_icon_meitu_1)
                    .into(mImageView);
        }

        @Override
        public void onClick(View view) {
            Intent intent = DetailActivity.newIntent(getActivity(), photo.getId());
            startActivity(intent);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<Photo> mPhotoList;

        public PhotoAdapter(List<Photo> photoList) {
            mPhotoList = photoList;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.photo_wall_item, viewGroup, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            Photo photoItem = mPhotoList.get(position);
            photoHolder.bindPhotoItem(photoItem);
        }

        @Override
        public int getItemCount() {
            return mPhotoList.size();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.photo_wall, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            /**
             * Called when the user submits the query. This could be due to a key press on the keyboard or due to pressing a submit button.
             * The listener can override the standard behavior by returning true to indicate that it has handled the submit request.
             * Otherwise return false to let the SearchView handle the submission by launching any associated intent.
             * @param query
             * @return
             */
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("提交了：", query);
                SearchPreferences.setSearchData(getActivity(), query);

                //隐藏搜索框和键盘
                searchItem.collapseActionView();
                searchView.clearFocus();
//                View view = getActivity().getCurrentFocus();
//                if (view != null) {
//                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
                if (!mSwipeToLoadLayout.isLoadMoreEnabled()) {
                    mSwipeToLoadLayout.setLoadMoreEnabled(true);
                }
                if (!mSwipeToLoadLayout.isRefreshEnabled()) {
                    mSwipeToLoadLayout.setRefreshEnabled(true);
                }
                mToolbar.setSubtitle("搜索结果：" + query);
                mAllPhotos.clear();
                mAllOriPhotos.clear();
                queryFromServer(UrlGenerater.getUrlStringWithFlickrSearch(getSearchData()));
                queryOriFromServer(UrlGenerater.getOriUrlStringWithFlickrSearch(getSearchData()));
                //Log.d(TAG, "测试搜索OriPhoto" + mAllOriPhotos.get(0).getUrl_o());
                mCurrentPage = 1;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("输入了：", newText);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //Toast.makeText(getContext(), "点击了search按钮", Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);

        }
        return super.onOptionsItemSelected(item);

    }

    private String getSearchData() {
        String searchData = SearchPreferences.getSearchData(getActivity());
        return searchData;
    }

    private void showProgressDialog() {
        if (mProgressBar.getVisibility() == View.GONE) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

    }

    private void closeProgressDialog() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void showLoadingUserDialog() {
        mLoadingUserDialog = new ProgressDialog(getActivity());
        mLoadingUserDialog.setMessage(getString(R.string.loading_user_info));
        mLoadingUserDialog.show();
    }

    public void closeLoadingUserDialog() {
        if (mLoadingUserDialog != null) {
            mLoadingUserDialog.dismiss();
        }
    }

    public void showOAuthingDialog() {
        mOAuthingDialog = new ProgressDialog(getActivity());
        mOAuthingDialog.setMessage(getString(R.string.please_oauth));
        mOAuthingDialog.show();
    }

    public void closeOAuthingDialog() {
        if (mOAuthingDialog != null) {
            mOAuthingDialog.dismiss();
        }
    }
}
