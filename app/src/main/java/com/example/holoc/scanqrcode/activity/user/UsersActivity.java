package com.example.holoc.scanqrcode.activity.user;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.fragment.FragmentSuKien;
import com.example.holoc.scanqrcode.fragment.FragmentThongBao;
import com.example.holoc.scanqrcode.fragment.FragmentTrangChu;

public class UsersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    Fragment fragment;
    BottomNavigationView naviBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

//        frameLayout = (FrameLayout) findViewById(R.id.frameLayoutUser);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBarUsr);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loadFragment(new FragmentTrangChu());

        naviBottom = (BottomNavigationView) findViewById(R.id.navigation);
        naviBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fragment = new FragmentTrangChu();
                        break;
                    case R.id.navigation_dashboard:
                        fragment = new FragmentSuKien();
                        break;
                    case R.id.navigation_notifications:
                        fragment = new FragmentThongBao();
                        break;
                }
                return loadFragment(fragment);
            }
        });

        SetUpBottomNavigationCounter(20);
    }
    private void SetUpBottomNavigationCounter(int count) {
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) naviBottom.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.notificiation_badge, bottomNavigationMenuView, false);

        itemView.addView(badge);
        TextView txtnotification = (TextView) findViewById(R.id.txtnotificationsbadge);

        // set count
        if (count >= 99){
            txtnotification.setText(99+"+");
            // set Background
            txtnotification.setBackgroundResource(R.drawable.custom_circle_shape);

        }else{
            if(count == 0){

            }else{
                // set count
                txtnotification.setText(count+"");
                // set Background
                txtnotification.setBackgroundResource(R.drawable.custom_circle_shape);


            }
        }

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean loadFragment(Fragment fragment){
        if (fragment!=null){
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayoutUser, fragment)
                    .commit();
            return true;
        }
        return false;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
