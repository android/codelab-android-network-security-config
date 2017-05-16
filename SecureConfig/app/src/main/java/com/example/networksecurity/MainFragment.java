/*
 * Copyright 2017 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.networksecurity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.networksecurity.model.Post;
import com.example.networksecurity.ui.PostAdapter;

public class MainFragment extends Fragment implements MainContract.View {

    private static final String TAG = MainFragment.class.getSimpleName();
    private MainContract.Presenter mPresenter;

    private ProgressBar mProgressBar;
    private CoordinatorLayout mLayout;
    private RecyclerView mPostList;
    private PostAdapter mPostAdapter;
    private View mEmptyView;
    private Snackbar mErrorSnackbar;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        PostAdapter.PostAdapterCallback mPostLoadingListener = new PostAdapter.PostAdapterCallback() {

            @Override
            public void onPostImageLoadingError(String error, Exception e) {
                mPresenter.onLoadPostImageError(error, e);
            }
        };
        mPostAdapter = new PostAdapter(getContext(), mPostLoadingListener);

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        mProgressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
        mLayout = (CoordinatorLayout) layout.findViewById(R.id.layout_coordinator);
        mPostList = (RecyclerView) layout.findViewById(R.id.post_list);
        mEmptyView = layout.findViewById(R.id.empty_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mPostList.setLayoutManager(layoutManager);
        mPostList.setAdapter(mPostAdapter);

        layout.findViewById(R.id.load_posts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadPostsClick();
            }
        });

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    private void onLoadPostsClick() {
        if (mPresenter == null) {
            return;
        }
        mPresenter.loadPosts();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingPosts(boolean isLoading) {
        mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setPosts(Post[] posts) {
        mPostAdapter.setPosts(posts);
        if (posts == null || posts.length < 1) {
            mPostList.setVisibility(View.GONE);
        } else {
            mPostList.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void showError(@NonNull String title, String error) {
        Log.e(TAG, error != null ? error : title);
        mErrorSnackbar = Snackbar.make(mLayout, title, Snackbar.LENGTH_INDEFINITE);
        mErrorSnackbar.show();

    }

    @Override
    public void hideError() {
        if (mErrorSnackbar != null) {
            mErrorSnackbar.dismiss();
        }
    }

    @Override
    public void showNoPostsMessage(boolean showMessage) {
        mEmptyView.setVisibility(showMessage ? View.VISIBLE : View.GONE);
    }
}
