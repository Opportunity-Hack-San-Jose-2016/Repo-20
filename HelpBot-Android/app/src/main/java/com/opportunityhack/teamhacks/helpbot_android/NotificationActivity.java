package com.opportunityhack.teamhacks.helpbot_android;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NotificationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] menuList;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        menuList = getResources().getStringArray(R.array.menu_list);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        String pushService = getIntent().getStringExtra("pushService");

        if (pushService != null) {
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            DetailFragment detailFragment = new DetailFragment();
            Bundle data = getIntent().getExtras();
            Log.i("data", data.toString());
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            detailFragment.setArguments(data);
            fragmentTransaction.replace(R.id.container, detailFragment);
            fragmentTransaction.commit();
            toolbar.setTitle("");

        } else {
            WelcomeFragment welcomeFragment = new WelcomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, welcomeFragment);
            fragmentTransaction.commit();
            toolbar.setTitle("Welcome");
        }
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.nav_list :
                RefugeeListFragment listFragment = new RefugeeListFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, listFragment);
                fragmentTransaction.commit();
                toolbar.setTitle("List");
                break;
            case R.id.nav_add :
                AddRefugeeFragment addRefugeeFragment = new AddRefugeeFragment();
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.container, addRefugeeFragment);
                fragmentTransaction1.commit();
                toolbar.setTitle("Help a Refugee");
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
        }
    }
}
