package com.mohamedabdelrazek.bloggerapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Mohamed on 31/10/2017.
 */

public class HomeFragment extends Fragment {

    BlogAdapter mBlogAdapter;
    FirebaseDatabase mFireBaseDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView mBlogList = myView.findViewById(R.id.blog_recycler);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        Query query = mFireBaseDatabase.getReference().child("Blog");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        };
        query.addChildEventListener(childEventListener);

        FirebaseRecyclerOptions<BlogModel> options =
                new FirebaseRecyclerOptions.Builder<BlogModel>()
                        .setQuery(query, BlogModel.class)
                        .build();


        mBlogAdapter = new BlogAdapter(options, getContext());
        mBlogList.setAdapter(mBlogAdapter);
        return myView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBlogAdapter.startListening();
    }


}
