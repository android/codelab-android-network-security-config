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

import com.example.networksecurity.data.DataSource;
import com.example.networksecurity.model.Post;

public class MainPresenter implements MainContract.Presenter {
    private final DataSource mData;
    private final MainContract.View mView;

    public MainPresenter(DataSource mData, MainContract.View mView) {
        this.mData = mData;
        this.mView = mView;
    }

    @Override
    public void start() {
        // Load posts when the app is started.
        loadPosts();
    }

    @Override
    public void loadPosts() {
        mView.setLoadingPosts(true);

        mData.getPosts(new DataSource.GetPostsCallback() {
            @Override
            public void onPostsLoaded(Post[] posts) {
                mView.setLoadingPosts(false);
                mView.hideError();
                if (posts != null && posts.length > 0) {
                    mView.showNoPostsMessage(false);
                    mView.setPosts(posts);
                } else {
                    mView.showNoPostsMessage(true);
                }
            }

            @Override
            public void onPostsNotAvailable(String error, Throwable cause) {
                String fullError = error;
                if (cause != null) {
                    // Append the exception to the error
                    StringBuilder buffer = new StringBuilder();
                    buffer.append(error);
                    buffer.append('\n');
                    buffer.append(cause.toString());
                    fullError = buffer.toString();
                }
                mView.showError(error, fullError);
                mView.setLoadingPosts(false);
                mView.showNoPostsMessage(true);
                mView.setPosts(null);

            }
        });
    }

    @Override
    public void onLoadPostImageError(String title, Exception e) {
        String fullError = title;
        if (e != null) {
            // Append the exception to the error
            StringBuilder buffer = new StringBuilder();
            buffer.append(title);
            buffer.append('\n');
            buffer.append(e.toString());
            fullError = buffer.toString();
        }
        mView.showError(title, fullError);
    }


}
