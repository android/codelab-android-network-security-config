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

package com.example.networksecurity.data.remote;


import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import com.example.networksecurity.data.DataSource;
import com.example.networksecurity.model.PostList;


/**
 * Makes a request to the json endpoint and parses the result as a {@link PostList} object.
 */
public class RemoteDataSource implements DataSource {

    private final String mUrl;
    private final RequestQueue mRequestQueue;

    public RemoteDataSource(String url) {
        mUrl = url;
        mRequestQueue = new RequestQueue(new NoCache(), new BasicNetwork(new HurlStack()));
        mRequestQueue.start();
    }


    @Override
    public void getPosts(@NonNull final GetPostsCallback getPostsCallback) {
        GsonRequest<PostList> gsonRequest = new GsonRequest<>(mUrl, PostList.class, null, new Response.Listener<PostList>() {
            @Override
            public void onResponse(PostList list) {
                getPostsCallback.onPostsLoaded(list.posts);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                getPostsCallback.onPostsNotAvailable(volleyError.getMessage(), volleyError.getCause());
                // + "\n" + volleyError.toString()
            }
        });

        mRequestQueue.add(gsonRequest);
    }

    @Override
    public void getPostImage(@NonNull String url, @NonNull GetImageCallback getImageCallback) {

    }


}
