package com.mohamedabdelrazek.bloggerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageView mProfileImageView;
    TextView mProfileNameTextView;
    TextView mProfileEmailTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().performIdentifierAction(R.id.nav_home, 0);
        navigationView.getMenu().getItem(0).setChecked(true);
        View header = navigationView.getHeaderView(0);

        mProfileImageView = header.findViewById(R.id.header_profile_image);
        mProfileEmailTextView = header.findViewById(R.id.header_profile_email);
        mProfileNameTextView = header.findViewById(R.id.header_profile_name);

        fillNavigationHeaderWithData();


    }

    private void fillNavigationHeaderWithData() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mProfileNameTextView.setText(firebaseUser.getDisplayName());
        mProfileEmailTextView.setText(firebaseUser.getEmail());
        mProfileImageView.setImageResource(R.drawable.fb);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        // item.setChecked(true);
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_home) {

            fragmentManager.beginTransaction()
                    .replace(R.id.content_nav
                            , new HomeFragment())
                    .commit();
            setTitle("Home");
        } else if (id == R.id.nav_search) {
            setTitle("Search");

        } else if (id == R.id.nav_profile) {

            fragmentManager.beginTransaction()
                    .replace(R.id.content_nav
                            , new ProfileFragment())
                    .commit();
            setTitle("Profile");

        } else if (id == R.id.nav_setting) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_nav
                            , new SettingFragment())
                    .commit();
            setTitle("Settings");


        } else if (id == R.id.nav_log_out) {
            cleanUp();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cleanUp() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Signing you out");
        pd.show();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        finish();
    }
}
