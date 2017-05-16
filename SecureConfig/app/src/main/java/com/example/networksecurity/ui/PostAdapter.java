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

package com.example.networksecurity.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.networksecurity.R;
import com.example.networksecurity.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private static final String TAG = "PostAdapter";
    private final PostAdapterCallback mCallback;
    private Post[] mPosts = new Post[0];
    private final ImageLoader mImageLoader;

    public interface PostAdapterCallback {
        void onPostImageLoadingError(String error, Exception e);
    }

    public PostAdapter(Context context, PostAdapterCallback callback) {
        mCallback = callback;

        // Set up a new ImageLoader that does not cache any requests.
        mImageLoader = new ImageLoader(Volley.newRequestQueue(context), new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return null;
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                // Do not cache this bitmap.
            }
        }) {
            @Override
            protected void onGetImageError(String cacheKey, VolleyError error) {
                super.onGetImageError(cacheKey, error);
                mCallback.onPostImageLoadingError(error.getMessage(), error);
            }
        };
    }


    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        PostViewHolder holder = new PostViewHolder(layout);
        holder.image.setDefaultImageResId(R.drawable.ic_loading);
        holder.image.setErrorImageResId(R.drawable.ic_error);
        return holder;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = mPosts[position];
        holder.name.setText(post.name);
        holder.text.setText(post.message);
        holder.image.setImageUrl(post.profileImage, mImageLoader);

        Log.d(TAG, "loading image: " + post.profileImage);
    }

    public void setPosts(Post[] posts) {
        mPosts = posts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPosts.length;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView image;
        TextView name;
        TextView text;

        public PostViewHolder(View itemView) {
            super(itemView);
            image = (NetworkImageView) itemView.findViewById(R.id.post_image);
            name = (TextView) itemView.findViewById(R.id.post_name);
            text = (TextView) itemView.findViewById(R.id.post_text);
        }
    }
}
