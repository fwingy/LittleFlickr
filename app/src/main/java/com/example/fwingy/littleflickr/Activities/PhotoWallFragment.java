package com.example.fwingy.littleflickr.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.fwingy.littleflickr.DataLab.DataLab;
import com.example.fwingy.littleflickr.GsonData.Photo;
import com.example.fwingy.littleflickr.GsonData.Photos;
import com.example.fwingy.littleflickr.Network.GsonUtil;
import com.example.fwingy.littleflickr.Network.HTTPUtil;
import com.example.fwingy.littleflickr.Network.UrlGenerater;
import com.example.fwingy.littleflickr.R;
import com.example.fwingy.littleflickr.SearchPreferences;
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

    private TextView mHintText;

    private RelativeLayout mGalleryContentLayout;

    private RecyclerView mPhotoWallRecyclerView;

    private SwipeToLoadLayout mSwipeToLoadLayout;

    private PhotoAdapter mPhotoAdapter;

    private List<Photo> mPhotos;

    private Toolbar mToolbar;

    private ProgressBar mProgressBar;

    private int mCurrentPage;

    private boolean isRefreshing;

    private List<Photo> mNextPagePhotos;

    private List<Photo> mAllPhotos = DataLab.getDataLab(getActivity()).getAllPhotos();

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                        isRefreshing = true;
                        queryFromServer(UrlGenerater.getUrlStringWithFlickrSearch(getSearchData()));
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
        mHintText.setText("按“搜索”键搜索Flickr");
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
                final Photos photos = GsonUtil.handlePhotosResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mPhotos = photos.getPhotoList();
                        mAllPhotos.addAll(mPhotos);
                        mPhotoAdapter = new PhotoAdapter(mAllPhotos);
                        setupAdapter();
                        closeProgressDialog();
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
                final Photos NextPagePhotos = GsonUtil.handlePhotosResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mNextPagePhotos = NextPagePhotos.getPhotoList();
                        mAllPhotos.addAll(mNextPagePhotos);
                        mPhotoAdapter.notifyDataSetChanged();
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
                    .load(photo.getUrl())
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
                mAllPhotos.clear();
                queryFromServer(UrlGenerater.getUrlStringWithFlickrSearch(getSearchData()));
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
}
