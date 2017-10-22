package com.mohamedabdelrazek.bloggerapp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Mohamed on 10/22/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
