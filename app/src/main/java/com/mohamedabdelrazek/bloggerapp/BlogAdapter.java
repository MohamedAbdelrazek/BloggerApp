package com.mohamedabdelrazek.bloggerapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

/**
 * Created by Mohamed on 10/21/2017.
 */

public class BlogAdapter extends FirebaseRecyclerAdapter<BlogModel, BlogAdapter.BlogListViewHolder> {
    Context mContext;

    public BlogAdapter(FirebaseRecyclerOptions<BlogModel> options, Context context) {
        super(options);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(BlogListViewHolder blogListViewHolder, int i, BlogModel blogModel) {

        blogListViewHolder.postDesc.setText(blogModel.getPostDesc());
        blogListViewHolder.postTitle.setText(blogModel.getPostTitle());
        try {
            Picasso.with(mContext).load(blogModel.getImageURi()).into(blogListViewHolder.postImage);
        } catch (Exception r) {
        }
    }

    @Override
    public BlogListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.card_view_single_row, viewGroup, false);

        return new BlogListViewHolder(view);
    }

    public class BlogListViewHolder extends RecyclerView.ViewHolder {

        ImageView postImage;
        TextView postTitle;
        TextView postDesc;

        public BlogListViewHolder(View view) {
            super(view);
            postImage = view.findViewById(R.id.blog_image_id);
            postTitle = view.findViewById(R.id.blog_title_id);
            postDesc = view.findViewById(R.id.blog_desc_id);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

        }

    }
}
