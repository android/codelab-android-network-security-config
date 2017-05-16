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

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

public class MainPresenterTest {
    @Mock
    private MainContract.View mView;

    @Mock
    private DataSource mData;

    @Captor
    private ArgumentCaptor<DataSource.GetPostsCallback> mGetPostsCallbackbackCaptor;

    private MainPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mPresenter = new MainPresenter(mData, mView);
    }

    @Test
    public void loadPostsSuccess() throws Exception {
        Post[] posts = new Post[]{
                new Post("Annie", "Hello", null),
                new Post("Ann", "World", null),
                new Post("Droid", "Test", null)
        };
        mPresenter.loadPosts();

        // Data should be loaded from data source and loading indicator should be displayed
        verify(mData).getPosts(mGetPostsCallbackbackCaptor.capture());
        verify(mView).setLoadingPosts(true);

        // Trigger success
        mGetPostsCallbackbackCaptor.getValue().onPostsLoaded(posts);

        // Loading indicator should be hidden and posts should be displayed.
        verify(mView).setLoadingPosts(false);
        verify(mView).setPosts(posts);
        verify(mView).showNoPostsMessage(false);
        verify(mView).hideError();

    }

    @Test
    public void loadPostsError() throws Exception {
        String errorText = "Error!";

        mPresenter.loadPosts();

        // Data should be loaded from data source and loading indicator should be displayed
        verify(mData).getPosts(mGetPostsCallbackbackCaptor.capture());
        verify(mView).setLoadingPosts(true);

        // Trigger error
        mGetPostsCallbackbackCaptor.getValue().onPostsNotAvailable(errorText, null);

        // Loading indicator should be hidden and error should be displayed.
        verify(mView).setLoadingPosts(false);
        verify(mView).showError(anyString(), anyString());
        verify(mView).showNoPostsMessage(true);
        verify(mView).setPosts(null);

    }

}